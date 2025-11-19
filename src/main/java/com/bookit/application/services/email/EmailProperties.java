package com.bookit.application.services.email;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("email")
public class EmailProperties {
    private String host;
    private Integer port;
    private String emailUsername;
    private String emailPassword;

    public EmailProperties(){}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}
