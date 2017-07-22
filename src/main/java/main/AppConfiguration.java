package main;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.ws.rs.DefaultValue;


public class AppConfiguration extends Configuration {

    private String appName;
    @DefaultValue("mongodb://localhost:27017")
    private String dbConnectionUri;
    private String dbName;

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public String getAppName() {
        return appName;
    }

    public String getDbConnectionUri() {
        return dbConnectionUri;
    }

    public String getDbName() {
        return dbName;
    }
}
