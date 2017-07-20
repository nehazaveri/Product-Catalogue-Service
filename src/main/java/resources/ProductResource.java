package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.Product;
import org.mongodb.morphia.Datastore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/product", description = "Product Service API")
@Path("/product")
public class ProductResource {

    private Datastore datastore;
    private Logger logger = LoggerFactory.getLogger(ProductResource.class);

    public ProductResource() {
    }

    public ProductResource(Datastore dataStore) {
        this.datastore = dataStore;
    }

    @ApiOperation(value = "Get All Products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts(){
        logger.debug("In get all products");
        return datastore.find(Product.class).asList();
    }

    @ApiOperation(value = "Create Product")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addProduct(Product product){
        datastore.save(product);
    }


}
