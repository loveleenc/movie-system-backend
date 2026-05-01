package com.bookit.events.shows.movie.api;

import com.bookit.events.shows.ResourceNotFoundException;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.movie.CatalogSystemUrlResolver;
import com.bookit.events.shows.movie.MovieClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;


@Component("showsMovieApiClient")
@ConditionalOnProperty(value = "catalog.source", havingValue = "external", matchIfMissing = false)
public class MovieApiClient implements MovieClient {
    private RestClient restClient;
    private final CatalogSystemUrlResolver catalogSystemUrlResolver;

    public MovieApiClient(CatalogSystemUrlResolver catalogSystemUrlResolver) {
        this.restClient = RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, (req, res) -> {
                })
                .build();
        this.catalogSystemUrlResolver = catalogSystemUrlResolver;
    }
    
    @Override
    public Movie getMovieById(Long movieId) throws ResourceNotFoundException {
        ParameterizedTypeReference<Movie> typeReference = new ParameterizedTypeReference<Movie>() {};

        return this.restClient.get()
                .uri(
                    UriComponentsBuilder.fromUri(catalogSystemUrlResolver.getUri())
                            .pathSegment("api", "internal", "movie", movieId.toString())
                            .build().toUri()
                )
                .retrieve()
                .body(Movie.class);

    }

    @Override
    public void sendRequest(Request request) {

    }

    @Override
    public Object processResponse(Response response) {
        return null;
    }
}
