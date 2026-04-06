package com.bookit.catalog.movie.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
class MovieExternalInformationService implements MovieAdditionalInformationService {
    private static final String DEFAULT_PLOT = "";
    private final RestClient restClient;
    private CircuitBreaker circuitBreaker;


    private final Logger logger = LoggerFactory.getLogger(MovieExternalInformationService.class);
    public MovieExternalInformationService(@Value("${omdb.apikey}") String apiKey,
                                           CircuitBreaker circuitBreaker) {
        this.restClient = RestClient.builder()
                .baseUrl(String.format("https://www.omdbapi.com/?apikey=%s", apiKey))
                .build();
        this.circuitBreaker = circuitBreaker;
    }

    private ReturnedMovie getDefaultMovie(){
        return new ReturnedMovie("", "", "");
    }

    @Override
    public String getMoviePlot(String movieName){
        try {
            ReturnedMovie movie = this.circuitBreaker.run(() -> this.restClient.get().uri(uriBuilder ->
                    uriBuilder.queryParam("t", movieName).build()).retrieve().body(ReturnedMovie.class),
                    throwable -> {
                        logger.info("external API call resulted in an exception: " + throwable.getMessage());
                        return getDefaultMovie();
                    });
            if (movie != null){
                return movie.getPlot();
            }
        }
        catch(RestClientException e){
            return DEFAULT_PLOT;
        }
        return DEFAULT_PLOT;
    }
}
