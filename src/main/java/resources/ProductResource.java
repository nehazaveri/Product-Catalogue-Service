package resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import model.Product;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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

    @ApiOperation(value = "Get Products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> get(@QueryParam("type") Optional<String> productType) {
        logger.debug("In get products by type " + productType);
        Query<Product> productQuery = datastore.find(Product.class);
        productType.ifPresent(type -> {
            productQuery.field("type").equalIgnoreCase(type);
        });
        return productQuery.asList();
    }

    @ApiOperation(value = "Create Product")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addProduct(Product product) {
        logger.debug("In addProduct product");
        datastore.save(product);
    }

    @ApiOperation(value = "Delete Product")
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String productId) {
        logger.debug("In delete product , Product Id :: " + productId);
        Query<Product> query = datastore.find(Product.class, "id", productId);
        datastore.delete(query);
        return Response.ok().build();
    }


}
