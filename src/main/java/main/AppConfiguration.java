package main;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;


public class AppConfiguration extends Configuration {

    private String appName;
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
