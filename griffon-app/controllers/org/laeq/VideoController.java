package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.control.TableColumn;
import org.laeq.icon.IconService;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.dao.VideoDAO;
import org.laeq.user.PreferencesService;
import org.laeq.video.ExportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class VideoController extends CRUDController<Video> {
    @MVCMember @Nonnull private VideoModel model;
    @MVCMember @Nonnull private VideoView view;

    @Inject private DatabaseService dbService;
    @Inject private ExportService exportService;
    @Inject private IconService iconService;
    @Inject private PreferencesService prefService;

    private TranslationService translationService;


    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
//        model.setPrefs(prefService.getPreferences());
//        setTranslationService();
        try{
            model.videoList.addAll(dbService.videoDAO.findAll());
            model.getUserSet().addAll(dbService.userDAO.findAll());
            model.getCollectionSet().addAll(dbService.collectionDAO.findAll());
            model.categorySet.addAll(dbService.categoryDAO.findAll());
        } catch (Exception e){
            System.out.println("Cannot fetch videos");
        }


        view.initForm();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void getVideoDuration(Video video) {
//        getLog().info("Calculating the video duration: should be known already. Fix this issue");
//        File file = new File(video.getPath());
//
//        if (file.exists()) {
//            try {
//                Media media = new Media(file.getCanonicalFile().toURI().toString());
//                MediaPlayer mediaPlayer = new MediaPlayer(media);
//
//                mediaPlayer.setOnReady(()-> {
//                    video.setDuration(mediaPlayer.getMedia().getDuration().toMillis());
//                    try {
//                        videoDAO.updateDuration(video);
//
//                    } catch (SQLException | DAOException e) {
//                        getLog().error(e.getMessage());
//                    }
//                });
//            } catch (IOException e) {
//                getLog().error(e.getMessage());
//            } finally {
//                view.refresh();
//            }
//        }
    }

    public void clear(){
        runInsideUISync(() -> view.reset());
    }

    public void delete(){
        if(model.selectedVideo == null){
            return;
        }

//        runInsideUISync(() -> {
//            Boolean confirmation = confirm(translationService.getMessage("org.laeq.video.delete.confirm"));
//            if(confirmation){
//                try {
//                    videoDAO.delete(model.getSelectedVideo());
//                    model.deleteVideo();
//                    view.reset();
//                } catch (DAOException e) {
//                    getLog().error(e.getMessage());
//                }
//            }
//        });
    }

    public void edit(){
        if(model.selectedVideo == null){
            return;
        }

        publishEvent("video.edit", this.model.selectedVideo);
    }

    public void export(){
//        if(model.getSelectedVideo() == null){
//            alert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.video.no_selection"));
//            return;
//        }
//
//        try {
//            String filename = exportService.export(model.getSelectedVideo());
//            alert(translationService.getMessage("org.laeq.title.success"),
//                    String.format(translationService.getMessage("org.laeq.export.success"), filename)
//            );
//        } catch (IOException e) {
//            alert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.title.error"));
//        }
    }

    public void editVideo(Video video) {
        if(model.selectedVideo == null){
            return;
        }

        publishEvent("video.edit", video);
    }

    private void publishEvent(String eventName, Video video){
        getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(video));
    }

    public void updateUser(TableColumn.CellEditEvent<Video, User> event) {
//        try {
//            Boolean confirm = confirm(translationService.getMessage("org.laeq.video.user.confirm"));
//
//            if(confirm){
//                videoDAO.updateUser(event.getRowValue(), event.getNewValue());
//                event.getRowValue().setUser(event.getNewValue());
//            }
//        } catch (SQLException | DAOException e) {
//            alert(translationService.getMessage("org.laeq.title.error"), e.getMessage());
//        }
    }

    public void updateCollection(TableColumn.CellEditEvent<Video, Collection> event) {
//        try {
//            Boolean confirm = confirm(translationService.getMessage("org.laeq.video.collection.confirm"));
//
//            if(confirm){
//                videoDAO.updateCollection(event.getRowValue(), event.getNewValue());
//                event.getRowValue().setCollection(event.getNewValue());
//                setCategories(event.getRowValue());
//                pointDAO.updateOnCollectionChange(event.getRowValue());
//                event.getRowValue().getPointSet().clear();
//                event.getRowValue().setTotal(pointDAO.findByVideo(event.getRowValue()).size());
//                runInsideUISync(() -> view.reset());
//            }
//
//        } catch (SQLException | DAOException e) {
//            alert(translationService.getMessage("org.laeq.title.error"), e.getMessage());
//        }
    }

    public void showDetail() {
//        if(model.getSelectedVideo() != null){
//            setPoints(model.getSelectedVideo());
//            setCategories(model.getSelectedVideo());
//            view.showDetails();
//        }
    }

    private void setPoints(Video video){
//        if(video.getPointSet().size() == 0){
//            model.getSelectedVideo().getPointSet().addAll(pointDAO.findByVideo(model.getSelectedVideo()));
//        }
    }

    private void setCategories(Video video){
//        if(video.getCollection().getCategorySet().size() == 0){
//            Set<Category> categories = categoryDAO.findByCollection(video.getCollection());
//            video.getCollection().getCategorySet().addAll(categories);
//        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();
//
//        list.put("video.import.success", objects -> {
//            model.getVideoList().clear();
//            model.getVideoList().addAll(videoDAO.findAll());
//            view.refresh();
//        });
//
//        list.put("video.created", objects -> {
//            Video video = (Video) objects[0];
//            runInsideUISync(() -> {
//                model.getVideoList().add(video);
//                getVideoDuration(video);
//            });
//        });
//
//        list.put("change.language", objects -> {
//            Locale locale = (Locale) objects[0];
//            model.getPrefs().locale = locale;
//            view.changeLocale(locale);
//            setTranslationService();
//        });
//
        return list;
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}
