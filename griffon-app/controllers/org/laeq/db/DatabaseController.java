package org.laeq.db;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class DatabaseController extends AbstractGriffonController {

    @Inject private DatabaseService service;
    @Inject private DialogService dialogService;

    private ProcessBuilder builder;
    private Process dbProcess;


    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
//            builder = createProcess();
//            dbProcess = builder.start();
            service.init();
        } catch (Exception e) {
            getLog().error("Cannot create the database process." + e.getMessage());
        }

        getApplication().getEventRouter().addEventListener(listeners());
        publishEvent("database.video_user.findAll", service.getVideoUserList());
    }

    @Override
    public void mvcGroupDestroy() {
        System.out.println("Database controller destruction");
//        dbProcess.destroy();
    }

    private ProcessBuilder createProcess() throws URISyntaxException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        URL hslqdbPath = getClass().getClassLoader().getResource("db/lib/hsqldb.jar");

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),
                "org.hsqldb.server.Server", "--database.1",  "file:hsqldb/vifecodb",  "--dbname.1", " vifecodb");
        builder.redirectErrorStream(true);

        return builder;
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("database.video_user.load", objects -> {
            System.out.println("database.video_user.load");
            VideoUser videoUser = (VideoUser) objects[0];
            try {
                service.set(videoUser);
                publishEvent("player.video_user.load", videoUser);
                publishEvent("category.video_user.load", videoUser);
            } catch (SQLException e) {
                dialogService.dialog("DBController: Cannot retrieve datas for " + videoUser);
            }
        });

        return list;
    }

    private void publishEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(object));
    }
}
