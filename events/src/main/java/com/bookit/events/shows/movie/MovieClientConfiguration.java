package com.bookit.events.shows.movie;


import com.bookit.events.shows.movie.api.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class MovieClientConfiguration {
    private final Logger logger = LoggerFactory.getLogger(MovieClientConfiguration.class);

    @Bean
    @ConditionalOnProperty(value = "catalog.source", havingValue = "external", matchIfMissing = false)
    @ConditionalOnExpression("'${catalog.url}'.matches('^http?:.*')")
    public CatalogSystemUrlResolver getClientConfigUrlResolver(ClientConfig clientConfig){
        return new CatalogSystemUrlResolver() {
            @Override
            public URI getUri() {
                try {
                    return new URI(clientConfig.getCatalog());
                } catch (URISyntaxException e) {
                    logger.error("URL for movie client provided by client config appears to be invalid: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Bean
    @ConditionalOnProperty(value = "catalog.source", havingValue = "external", matchIfMissing = false)
    @ConditionalOnExpression("!'${catalog.url}'.matches('^https?:.*')")
    public CatalogSystemUrlResolver getDiscoveryClient(DiscoveryClient discoveryClient){
        return new CatalogSystemUrlResolver() {
            @Override
            public URI getUri() {
                try{
                    ServiceInstance serviceInstance =  discoveryClient.getInstances("catalog").get(0);
                    return serviceInstance.getUri();
                }
                catch (IndexOutOfBoundsException e){
                    logger.error("Unable to find movie service instance using service discovery: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
