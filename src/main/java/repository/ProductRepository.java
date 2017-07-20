package repository;

import model.Product;
import org.mongodb.morphia.Datastore;

import java.util.List;

/**
 * Created by neha on 19/07/17.
 */
public class ProductRepository {

    private Datastore datastore;

    public ProductRepository(Datastore datastore) {
        this.datastore = datastore;
    }

    public List<Product> get() {
        return null;
    }
}
