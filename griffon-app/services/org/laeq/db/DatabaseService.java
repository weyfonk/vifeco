package org.laeq.db;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DatabaseService extends AbstractGriffonService {
    private final DatabaseManager manager;
    private UserDAO userDAO;
    private CategoryCollectionDAO categoryCollectionDAO;
    private PointDAO pointDAO;

    public DatabaseService() {
        DatabaseConfigBean configBean = new DatabaseConfigBean("jdbc:hsqldb:hsql://localhost/vifecodb", "SA", "");

        manager = new DatabaseManager(configBean);
    }

    public DAOInterface factory(String entity) throws Exception {
        if (entity.equals("user")) {
            return new UserDAO(manager, UserDAO.sequence_name);
        } else if (entity.equals("category")) {
            return new CategoryDAO(manager, CategoryDAO.sequence_name);
        } else if (entity.equals("video")) {
            return new VideoDAO(manager, VideoDAO.sequence_name);
        } else if (entity.equals("point")) {
            return new PointDAO(manager, PointDAO.sequence_name);
        } else if (entity.equals("category_collection")) {
            return new CategoryCollectionDAO(manager, CategoryCollection.sequence_name);
        }

        throw new Exception("Database service: cannot instantiate a DAO");
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public void init() {
        try{
            URL tableQuery = getClass().getClassLoader().getResource("sql/create_tables.sql");
            manager.loadFixtures(tableQuery);

            URL sequenceQuery = getClass().getClassLoader().getResource("sql/create_sequences.sql");
            manager.loadFixtures(sequenceQuery);

            getLog().info("DatabaseService: tables and sequences created");

        } catch (Exception e) {
            getLog().error("DatabaseService init: cannot load create_table/create_sequence.");
        }

        try{
            userDAO = new UserDAO(manager, UserDAO.sequence_name);
            userDAO.init();
            getLog().info("DatabaseService: default user created");
        } catch (Exception e){
            getLog().error("DatabaseService: cannot create default user");
        }

        try{
            categoryCollectionDAO = new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
            categoryCollectionDAO.init();
            getLog().info("DatabaseService: default category collection created");
        } catch (Exception e){
            getLog().error("DatabaseService: cannot create default category collection");
        }


        pointDAO = new PointDAO(manager, "point_id");

    }

    public void create(Video video) throws SQLException, DAOException {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        dao.insert(video);
    }

    public List<VideoUser> getVideoUserList(){
        return new VideoUserDAO(manager, "not_applicable").findAll();
    }

    public Set<Video> findAll(VideoUser videoUser) {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        return dao.findAll();
    }


    public SortedSet<Point> getVideoPoint(VideoUser videoUser){
        PointDAO pointDAO = new PointDAO(manager, PointDAO.sequence_name);

        return pointDAO.findByVideoAndUser(videoUser.getVideo(), videoUser.getUser());
    }

    public CategoryCollection getCategoryCollection(int id) throws SQLException {
        return categoryCollectionDAO.findByID(id);
    }

    public void set(VideoUser videoUser) throws SQLException {
        videoUser.setPoints(pointDAO.findByVideoAndUser(videoUser.getVideo(), videoUser.getUser()));

        int categoryCollectionId = videoUser.getVideo().getCategoryCollection().getId();

        videoUser.getVideo().setCategoryCollection(categoryCollectionDAO.findByID(categoryCollectionId));
    }
}