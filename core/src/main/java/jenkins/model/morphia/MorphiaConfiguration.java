package jenkins.model.morphia;

import com.mongodb.Mongo;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;

/**
 * Created by gblike on 3/21/14.
 */
public class MorphiaConfiguration {
    public String dbHost = "localhost";
    public int dbPort = 27017;
    public String dbName = "test";
    public Datastore makeDatastore() {
        Morphia morphia = new Morphia();
        Mapper mapper = morphia.getMapper();

        mapper.getOptions().actLikeSerializer = true;
        mapper.getOptions().objectFactory = new CustomMorphiaObjectFactory();

        try{
            Mongo mongo = new Mongo(dbHost, dbPort);
            return morphia.createDatastore(mongo, dbName);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
