package com.bookit.catalog.movie.services.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieConfiguration {

    @Bean
    StorageProperties createStorageProperties(){
        return new StorageProperties();
    }
}
