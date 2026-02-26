package com.bookit.catalog.movie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class MovieExternalInformationService {
    private final RestClient restClient;

    public MovieExternalInformationService(@Value("${omdb.apikey}") String apiKey) {
        this.restClient = RestClient.builder()
                .baseUrl(String.format("https://www.omdbapi.com/?apikey=%s", apiKey))
                .build();
    }

    public String getMoviePlot(String movieName){
        try {
            ReturnedMovie movie = this.restClient.get().uri(uriBuilder ->
                    uriBuilder.queryParam("t", movieName).build()).retrieve().body(ReturnedMovie.class);
            if (movie != null){
                return movie.getPlot();
            }
        }
        catch(RestClientException e){
            return "";
        }
        return "";
    }
}
