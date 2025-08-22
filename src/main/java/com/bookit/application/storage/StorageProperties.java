package com.bookit.application.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.util.Objects;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String url;
    private String containerName;
    private String connectionString;

    public String getUrl() {
        return url;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public StorageProperties(Environment env){
        this.url = Objects.requireNonNull(env.getProperty("spring.azureblobsource.url"));
        this.containerName = Objects.requireNonNull(env.getProperty("spring.azureblobsource.containerName"));
        this.connectionString = Objects.requireNonNull(env.getProperty("spring.azureblobsource.connectionString"));
    }


}
