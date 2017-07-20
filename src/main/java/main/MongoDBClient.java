package main;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by neha on 19/07/17.
 */
public class MongoDBClient {

    private final Datastore datastore;

    public MongoDBClient(String dbConnectionUri, String database) {
        final Morphia morphia = new Morphia();

        morphia.mapPackage("model");

        MongoClientURI uri = new MongoClientURI(dbConnectionUri);
        this.datastore = morphia.createDatastore(new MongoClient(uri), database);
        datastore.ensureIndexes(true);
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
