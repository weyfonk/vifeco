package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.env.Metadata;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Preferences;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VifecoController extends AbstractGriffonController {
    private VifecoModel model;

    @Inject private PreferencesService preferencesService;
    @Inject private HelperService helperService;
    @Inject private Metadata metadata;

    private Preferences preference;

    @MVCMember
    public void setModel(@Nonnull VifecoModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        preference = preferencesService.getPreferences();
        getApplication().setLocale(preference.getLocale());

        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> createGroup("user"));
        list.put("category.section", objects -> createGroup("category"));
        list.put("collection.section", objects -> createGroup("collection"));
        list.put("video.section", objects -> createGroup("video"));
        list.put("statistic.section", objects -> createGroup("statistic"));
        list.put("about.section", objects -> createGroup("about"));
        list.put("video.import", objects -> createGroup("import"));
        list.put("config.section", objects -> createGroup("config"));
        list.put("locale.set", objects -> setLocale((String) objects[0]));
        list.put("mvc.clean", objects -> cleanAndDestroy((String) objects[0]));

        return list;
    }

    private void setLocale(String locale) {
        preference.setLocale(locale);
        getApplication().setLocale(preference.getLocale());
        createGroup(model.currentGroup);
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        destroyMVC(model.currentGroup);
        destroyMVC(groupName);

        try{
            createMVCGroup(groupName, args);
            model.currentGroup = groupName;
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName){
        destroyMVC(model.currentGroup);

        try{
            createMVCGroup(groupName);
            model.currentGroup = groupName;
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    /**
     * Clean and destroy
     * @param name
     */
    private void cleanAndDestroy(String name){
        destroyMVC(name);
        closeWindow(name);
    }

    private void closeWindow(String name){
        try{
            Stage window = (Stage) getApplication().getWindowManager().findWindow(name);
            getApplication().getWindowManager().detach(name);
            window.close();
        } catch (Exception e){

        }
    }

    private void destroyMVC(String name){
        try{
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(group != null){
                group.destroy();
            }
        }catch (Exception e){

        }
    }
}