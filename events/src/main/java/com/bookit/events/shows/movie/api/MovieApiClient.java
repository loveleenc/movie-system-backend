package com.bookit.events.shows.movie.api;

import com.bookit.events.shows.ResourceNotFoundException;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.movie.MovieClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component("showsMovieApiClient")
@ConditionalOnProperty(value = "catalog.source", havingValue = "external", matchIfMissing = false)
public class MovieApiClient implements MovieClient {
    private String catalogUrl;
    private RestClient restClient;

    public MovieApiClient(ClientConfigUrl clientConfigUrl) {
        System.out.println(clientConfigUrl.getCatalog());
        this.restClient = RestClient.builder().baseUrl(clientConfigUrl.getCatalog()).build();
    }

    @Override
    public Movie getMovieById(Long movieId) throws ResourceNotFoundException {
        System.out.println(this.catalogUrl);
        ParameterizedTypeReference<Movie> typeReference = new ParameterizedTypeReference<Movie>() {};
        return this.restClient.get().uri("/api/internal/movie/{movieId}", movieId).retrieve().body(typeReference);
    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public Object processResponse(Response response) {
        return null;
    }
}
