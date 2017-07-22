package resources;

import com.mongodb.BasicDBObject;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import main.AppConfiguration;
import main.Main;
import main.MongoDBClient;
import model.Product;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mongodb.morphia.Datastore;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ProductResourceTest {
    @ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE = new DropwizardAppRule<>(Main.class, ResourceHelpers.resourceFilePath("app-config-for-test.yml"));
    private static final Product PRODUCT = new Product("1", "NAME", "TEST");
    private static Datastore datastore;
    private JerseyClient client = new JerseyClientBuilder().build();

    @BeforeClass
    public static void setUp() throws Exception {
        AppConfiguration config = RULE.getConfiguration();
        datastore = new MongoDBClient(config.getDbConnectionUri(), config.getDbName()).getDatastore();
    }

    @Before
    public void setup() throws Exception {
        datastore.getCollection(Product.class).remove(new BasicDBObject());
    }

    @Test
    public void itShouldSaveProduct() throws Exception {
        //given
        Product product = new Product("1", "NAME", "TEST");

        //when
        Response response = getTarget(String.format("product"), RULE).request()
                .post(Entity.entity(product, MediaType.APPLICATION_JSON));

        //then
        assertEquals(204, response.getStatus());
        assertProductResponse(PRODUCT, datastore.find(Product.class).asList());

    }

    private void assertProductResponse(Product product, List<Product> products) {
        assertEquals(1, products.size());
        assertEquals(product.getId(), products.get(0).getId());
        assertEquals(product.getName(), products.get(0).getName());
        assertEquals(product.getType(), products.get(0).getType());
    }

    @Test
    public void itShould_RetrieveSavedProduct() throws Exception {
        //given
        datastore.save(PRODUCT);

        //when
        Response response = getTarget(String.format("product"), RULE).request().get();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = response.readEntity(new GenericType<List<Product>>(){});
        assertEquals(1, products.size());
        assertProductResponse(PRODUCT, products);
    }

    @Test
    public void itShould_RetrieveMultipleSavedProduct() throws Exception {
        //given
        Product product1 = new Product("2", "TEST", "TEST");
        datastore.save(PRODUCT, product1);

        //when
        Response response = getTarget(String.format("product"), RULE).request().get();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = response.readEntity(List.class);
        assertEquals(2, products.size());
    }

    @Test
    public void itShould_RetrieveSavedProduct_ByType() throws Exception {
        //given
        Product product1 = new Product("2", "TEST", "TEST1");
        datastore.save(PRODUCT, product1);

        //when
        Response response = getTarget(String.format("product?type=TEST1"), RULE).request().get();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = response.readEntity(new GenericType<List<Product>>(){});
        assertEquals(1, products.size());
        assertProductResponse(product1,products);
    }

    @Test
    public void itShould_ReturnEmptyResponse_WhenNoProductFound_ByType() throws Exception {
        //given
        Product product1 = new Product("2", "TEST", "TEST1");
        datastore.save(PRODUCT, product1);

        //when
        Response response = getTarget(String.format("product?type=TEST2"), RULE).request().get();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = response.readEntity(new GenericType<List<Product>>(){});
        assertEquals(0, products.size());
    }


    @Test
    public void itShould_Successfully_DeleteProduct() throws Exception {
        //given
        Product product1 = new Product("2", "TEST", "TEST");
        datastore.save(PRODUCT, product1);

        //when
        Response response = getTarget(String.format("product/2"), RULE).request().delete();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = datastore.find(Product.class).asList();
        assertEquals(1, products.size());
        assertProductResponse(PRODUCT,products);
    }

    @Test
    public void itShould_DoNothing_WhenProductDoesNotExist_Delete() throws Exception {
        //given
        Product product1 = new Product("2", "TEST", "TEST");
        datastore.save(PRODUCT, product1);

        //when
        Response response = getTarget(String.format("product/3"), RULE).request().delete();

        //then
        assertEquals(200, response.getStatus());
        List<Product> products = datastore.find(Product.class).asList();
        assertEquals(2, products.size());
    }


    protected JerseyWebTarget getTarget(String path, DropwizardAppRule<AppConfiguration> rule) {
        String uri = String.format("http://localhost:%d/%s", rule.getLocalPort(), path);
        return client.target(uri);
    }

}
