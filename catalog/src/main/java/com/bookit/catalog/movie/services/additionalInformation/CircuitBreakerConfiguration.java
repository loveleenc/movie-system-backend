package com.bookit.catalog.movie.services.additionalInformation;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker createMoviePlotCircuitBreaker(CircuitBreakerFactory<?, ?> circuitBreakerFactory){
        return circuitBreakerFactory.create("moviePlotService");
    }

}
