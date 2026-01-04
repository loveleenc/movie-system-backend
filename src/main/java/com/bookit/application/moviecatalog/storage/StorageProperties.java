package com.bookit.application.moviecatalog.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public StorageProperties(){}


}
