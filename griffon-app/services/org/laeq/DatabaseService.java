package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.dao.*;
import org.laeq.settings.Settings;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DatabaseService extends AbstractGriffonService{
    private final HibernateUtil hbu;
    public final UserDAO userDAO;
    public final CategoryDAO categoryDAO;
    public final CollectionDAO collectionDAO;
    public final VideoDAO videoDAO;
    public final PointDAO pointDAO;

    public DatabaseService(){
        this.hbu = new HibernateUtil("hibernate.cfg.xml");
        this.userDAO = new UserDAO(this.hbu);
        this.categoryDAO = new CategoryDAO(this.hbu);
        this.videoDAO = new VideoDAO(this.hbu);
        this.collectionDAO = new CollectionDAO(this.hbu);
        this.pointDAO = new PointDAO(this.hbu);

        try {
            int total = this.userDAO.findAll().size();
            if(total == 0){
                setUpDefaults();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpDefaults() throws Exception {
        User defaultUser = new User();
        defaultUser.setFirstName("default");
        defaultUser.setLastName("default");
        defaultUser.setDefault(Boolean.TRUE);
        userDAO.create(defaultUser);

        Collection collection = new Collection();
        collection.setName("Transport");
        collection.setDefault(Boolean.TRUE);

        List<Category> categories = getCategoryFixtures();

        for(Category category: categories){
            categoryDAO.create(category);
        }

        categories.forEach(category -> {
            collection.addCategory(category);
        });

        this.collectionDAO.create(collection);

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resource = classLoader.getResourceAsStream("sample/sample.mp4");
        String filePath = String.format("%s%s%s", Settings.videoPath, File.separator, "sample.mp4");
        Files.copy(resource, Paths.get(filePath));

        File file = new File(filePath);
        Video video = new Video();
        video.setPath(file.getAbsolutePath());
        video.setCollection(collection);
        video.setUser(defaultUser);
        video.setDuration(Duration.seconds(23));

        videoDAO.create(video);
    }
    
    private List<Category> getCategoryFixtures(){
        List<String[]> datas = new ArrayList<>();
        datas.add(new String[]{"Moving car", "M11 11v-3h1.247c.882 0 1.235.297 1.828.909.452.465 1.925 2.091 1.925 2.091h-5zm-1-3h-2.243c-.688 0-1.051.222-1.377.581-.316.348-.895.948-1.506 1.671 1.719.644 4.055.748 5.126.748v-3zm14 5.161c0-2.823-2.03-3.41-2.794-3.631-1.142-.331-1.654-.475-3.031-.794-.55-.545-2.052-2.036-2.389-2.376l-.089-.091c-.666-.679-1.421-1.269-3.172-1.269h-7.64c-.547 0-.791.456-.254.944-.534.462-1.944 1.706-2.34 2.108-1.384 1.402-2.291 2.48-2.291 4.603 0 2.461 1.361 4.258 3.179 4.332.41 1.169 1.512 2.013 2.821 2.013 1.304 0 2.403-.838 2.816-2h6.367c.413 1.162 1.512 2 2.816 2 1.308 0 2.409-.843 2.82-2.01 1.934-.056 3.181-1.505 3.181-3.829zm-18 4.039c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm12 0c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm2.832-2.15c-.399-1.188-1.509-2.05-2.832-2.05-1.327 0-2.44.868-2.836 2.062h-6.328c-.396-1.194-1.509-2.062-2.836-2.062-1.319 0-2.426.857-2.829 2.04-.586-.114-1.171-1.037-1.171-2.385 0-1.335.47-1.938 1.714-3.199.725-.735 1.31-1.209 2.263-2.026.34-.291.774-.432 1.222-.43h5.173c1.22 0 1.577.385 2.116.892.419.393 2.682 2.665 2.682 2.665s2.303.554 3.48.895c.84.243 1.35.479 1.35 1.71 0 1.196-.396 1.826-1.168 1.888z", "#000000", "A"});
        datas.add(new String[]{"Stopped car", "M7 13.5c0-.828-.672-1.5-1.5-1.5s-1.5.672-1.5 1.5.672 1.5 1.5 1.5 1.5-.672 1.5-1.5zm9 1c0-.276-.224-.5-.5-.5h-7c-.276 0-.5.224-.5.5s.224.5.5.5h7c.276 0 .5-.224.5-.5zm4-1c0-.828-.672-1.5-1.5-1.5s-1.5.672-1.5 1.5.672 1.5 1.5 1.5 1.5-.672 1.5-1.5zm-17.298-6.5h-2.202c-.276 0-.5.224-.5.5v.511c0 .793.926.989 1.616.989l1.086-2zm19.318 3.168c-.761-1.413-1.699-3.17-2.684-4.812-.786-1.312-1.37-1.938-2.751-2.187-1.395-.25-2.681-.347-4.585-.347s-3.19.097-4.585.347c-1.381.248-1.965.875-2.751 2.187-.981 1.637-1.913 3.382-2.684 4.812-.687 1.273-.98 2.412-.98 3.806 0 1.318.42 2.415 1 3.817v2.209c0 .552.448 1 1 1h1.5c.552 0 1-.448 1-1v-1h13v1c0 .552.448 1 1 1h1.5c.552 0 1-.448 1-1v-2.209c.58-1.403 1-2.499 1-3.817 0-1.394-.293-2.533-.98-3.806zm-15.641-3.784c.67-1.117.852-1.149 1.39-1.246 1.268-.227 2.455-.316 4.231-.316s2.963.088 4.231.316c.538.097.72.129 1.39 1.246.408.681.81 1.388 1.195 2.081-1.456.22-4.02.535-6.816.535-3.048 0-5.517-.336-6.805-.555.382-.686.779-1.386 1.184-2.061zm11.595 10.616h-11.948c-1.671 0-3.026-1.354-3.026-3.026 0-1.641.506-2.421 1.184-3.678 1.041.205 3.967.704 7.816.704 3.481 0 6.561-.455 7.834-.672.664 1.231 1.166 2.01 1.166 3.646 0 1.672-1.355 3.026-3.026 3.026zm5.526-10c.276 0 .5.224.5.5v.511c0 .793-.926.989-1.616.989l-1.086-2h2.202z", "#FF0000" ,"Q"});
        datas.add(new String[]{"Moving truck", "M5 11v1h8v-7h-10v-1c0-.552.448-1 1-1h10c.552 0 1 .448 1 1v2h4.667c1.117 0 1.6.576 1.936 1.107.594.94 1.536 2.432 2.109 3.378.188.312.288.67.288 1.035v4.48c0 1.089-.743 2-2 2h-1c0 1.656-1.344 3-3 3s-3-1.344-3-3h-4c0 1.656-1.344 3-3 3s-3-1.344-3-3h-1c-.552 0-1-.448-1-1v-6h-2v-2h7v2h-3zm3 5.8c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm10 0c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm-3-2.8h-10v2h.765c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h5.53c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h1.765v-4.575l-1.711-2.929c-.179-.307-.508-.496-.863-.496h-4.426v6zm1-5v3h5l-1.427-2.496c-.178-.312-.509-.504-.868-.504h-2.705zm-16-3h8v2h-8v-2z", "#000000", "S"});
        datas.add(new String[]{"Stopped truck", "M3 18h-2c-.552 0-1-.448-1-1v-13c0-.552.448-1 1-1h13c.552 0 1 .448 1 1v2h4.667c1.117 0 1.6.576 1.936 1.107.594.94 1.536 2.432 2.109 3.378.188.312.288.67.288 1.035v4.48c0 1.089-.743 2-2 2h-1c0 1.656-1.344 3-3 3s-3-1.344-3-3h-6c0 1.656-1.344 3-3 3s-3-1.344-3-3zm3-1.2c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm12 0c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm-3-2.8h-13v2h1.765c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h7.53c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h1.765v-4.575l-1.711-2.929c-.179-.307-.508-.496-.863-.496h-4.426v6zm-2-9h-11v7h11v-7zm3 4v3h5l-1.427-2.496c-.178-.312-.509-.504-.868-.504h-2.705z",  "#FF0000" ,"W"});
        datas.add(new String[]{"Moving bike", "M6.804 10.336l1.181-2.331-.462-1.005h-4.523v-1h5.992c.238 0 .5.19.5.5 0 .311-.26.5-.5.5h-.368l.47 1h6.483l-.841-2h3.243c.823.005 1.49.675 1.49 1.5 0 .828-.672 1.5-1.5 1.5-.711 0-.727-1 0-1 .239 0 .5-.189.5-.5 0-.239-.189-.5-.5-.5h-1.727l1.324 3.209c.454-.136.936-.209 1.434-.209 2.76 0 5 2.24 5 5s-2.24 5-5 5c-2.759 0-5-2.24-5-5 0-1.906 1.069-3.564 2.64-4.408l-.43-1.039-4.493 5.947h-1.742c-.251 2.525-2.384 4.5-4.975 4.5-2.759 0-5-2.24-5-5s2.241-5 5-5c.636 0 1.244.119 1.804.336zm-.455.897c-.421-.151-.876-.233-1.349-.233-2.207 0-4 1.792-4 4s1.793 4 4 4c2.038 0 3.723-1.528 3.97-3.5h-3.103c-.174.299-.497.5-.867.5-.551 0-1-.448-1-1 0-.533.419-.97.946-.998l1.403-2.769zm10.675.29c-1.208.688-2.024 1.988-2.024 3.477 0 2.208 1.792 4 4 4s4-1.792 4-4-1.792-4-4-4c-.363 0-.716.049-1.05.14l1.182 2.869c.49.064.868.484.868.991 0 .552-.448 1-1 1-.551 0-1-.448-1-1 0-.229.077-.44.207-.609l-1.183-2.868zm-9.783.164l-1.403 2.766.029.047h3.103c-.147-1.169-.798-2.183-1.729-2.813zm.454-.898c1.254.804 2.126 2.152 2.28 3.711h.998l-2.455-5.336-.823 1.625zm7.683-1.789h-5.839l2.211 4.797 3.628-4.797zm-14.378 0h4v-1h-4v1zm1-4h4v-1h-4v1z", "#000000", "D"});
        datas.add(new String[]{"Stopped bike", "M17.565 9.209c.454-.136.937-.209 1.435-.209 2.759 0 5 2.24 5 5s-2.241 5-5 5c-2.76 0-5-2.24-5-5 0-1.906 1.068-3.564 2.639-4.408l-.429-1.039-4.494 5.947h-1.741c-.251 2.525-2.385 4.5-4.975 4.5-2.76 0-5-2.24-5-5s2.24-5 5-5c.635 0 1.244.119 1.803.336l1.181-2.331-.462-1.005h-1.022c-.277 0-.5-.224-.5-.5 0-.239.189-.5.5-.5h2.491c.239 0 .5.189.5.5s-.26.5-.5.5h-.368l.47 1h6.484l-.421-1h-1.656c-.277 0-.5-.224-.5-.5 0-.311.259-.5.5-.5h2.33l1.735 4.209zm-11.217 1.024c-.421-.151-.875-.233-1.348-.233-2.208 0-4 1.792-4 4s1.792 4 4 4c2.038 0 3.722-1.528 3.969-3.5h-3.103c-.174.299-.497.5-.866.5-.552 0-1-.448-1-1 0-.533.419-.97.945-.998l1.403-2.769zm10.675.289c-1.208.689-2.023 1.989-2.023 3.478 0 2.208 1.792 4 4 4s4-1.792 4-4-1.792-4-4-4c-.364 0-.716.049-1.051.14l1.182 2.869c.491.064.869.484.869.991 0 .552-.449 1-1 1-.552 0-1-.448-1-1 0-.229.077-.44.207-.609l-1.184-2.869zm-9.783.165l-1.403 2.766.029.047h3.103c-.147-1.169-.798-2.183-1.729-2.813m.454-.898c1.254.804 2.126 2.152 2.281 3.711h.997l-2.454-5.336-.824 1.625zm7.683-1.789h-5.839l2.212 4.797 3.627-4.797z", "#FF0000", "E"});
        datas.add(new String[]{"Construction site", "M16.063 19.528l-6.416-5.37 1.08 1.961c.134.241.214.515.232.791l.38 5.793c.041.674-.466 1.297-1.253 1.297-.59 0-1.093-.4-1.225-.974l-1.157-4.961c-.385-.437-1.475-1.593-2.135-2.291-.719 1.711-2.536 6.043-3.114 7.459-.192.473-.645.767-1.179.767-.703 0-1.276-.569-1.276-1.27 0-.092 0-.112.761-3.863.518-2.555 1.167-5.755 1.473-7.382.084-.445.287-.86.587-1.198l.938-1.057-3.37-2.82c-.507-.425.137-1.192.642-.767l.949.795c.325-.626.855-1.651 1.165-2.278.199-.397.589-.658 1.045-.699 1.178.012 2.314.018 5.252.039.57.005 1.121.049 1.596.528.294.294 2.051 2.176 2.416 2.542.333.334.522.794.521 1.263l-.005 8.639 2.766 2.316 2.72-2.996c.235-.259.546-.381.854-.381.465 0 .919.277 1.088.779.937 2.817 2.602 7.81 2.602 7.81h-12l4.063-4.472zm-11.537-9.656l-.957 1.079c-.179.202-.302.451-.352.72-.306 1.63-.956 4.835-1.476 7.394-.371 1.829-.721 3.556-.744 3.72.01.211.431.32.533.071.726-1.782 3.414-8.175 3.414-8.175.186-.439.621-.362.823-.151 0 0 2.101 2.208 2.629 2.776.139.15.235.334.282.531l1.157 4.963c.064.287.524.261.506-.033l-.38-5.79c-.008-.131-.047-.261-.109-.375l-1.943-3.527c-.07-.125-.081-.271-.037-.402l-3.346-2.801zm18.087 13.128c-.559-1.676-1.793-5.397-2.164-6.494-.036-.106-.171-.129-.253-.042-1.031 1.082-4.259 4.691-5.936 6.536h8.353zm-9.642-7.364l.004-7.805c.001-.206-.083-.408-.229-.555-.709-.711-1.804-1.928-2.417-2.543-.177-.179-.37-.229-.895-.233-2.865-.021-4.154-.027-5.206-.041-.063.008-.143.058-.187.146-.338.684-.938 1.842-1.252 2.444l.07.049 1.694-1.887c.103-.114.259-.169.405-.165l2.617.175c.414.028.616.521.34.831l-2.725 3.072 3.308 2.768 2.434-2.826c.273-.316.796-.177.871.239l1.165 6.329.003.002zm-3.706-3.102l.94.787 1.527 1.278-.729-4.083-1.738 2.018zm-5.677-4.751l.835.699 2.068-2.331-1.356-.091-1.547 1.723zm10.845-2.096c-1.568 0-2.845-1.275-2.845-2.843s1.277-2.844 2.845-2.844c1.568 0 2.844 1.276 2.844 2.844 0 1.568-1.276 2.843-2.844 2.843zm0-4.687c-1.018 0-1.845.827-1.845 1.844 0 1.016.827 1.843 1.845 1.843 1.017 0 1.844-.827 1.844-1.843 0-1.017-.827-1.844-1.844-1.844z",  "#000000" ,"F"});

        List<Category> result = new ArrayList<>();

        datas.forEach(d -> {
            Category category = new Category(d);
            result.add(category);
        });

        return result;
    }
}