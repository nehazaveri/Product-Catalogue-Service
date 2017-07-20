package main;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.mongodb.morphia.Datastore;
import resources.ProductResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class Main extends Application<AppConfiguration>{


    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new Java8Bundle());

        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor()));

        bootstrap.addBundle(new SwaggerBundle<AppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(AppConfiguration appConfig, Environment environment) throws Exception {
        if (appConfig.getDbConnectionUri() == null || appConfig.getDbName() == null) {
            throw new RuntimeException("Database config not defined.");
        }

        MongoDBClient mongoDBClient = new MongoDBClient(appConfig.getDbConnectionUri(), appConfig.getDbName());
        Datastore datastore = mongoDBClient.getDatastore();

        ProductResource resource = new ProductResource(datastore);

        environment.jersey().register(resource);

        addCrossOriginFilter(environment);
    }

    private void addCrossOriginFilter(Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "*");

        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, "Authorization, x-cci-user-metadata");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET, POST, HEAD, PUT, DELETE");
    }
}
