package model;

import org.mongodb.morphia.annotations.Indexed;

/**
 * Created by neha on 19/07/17.
 */
public class Product {

    private String id;
    private String name;

    @Indexed
    private String type;

    public Product() {
    }

    public Product(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
