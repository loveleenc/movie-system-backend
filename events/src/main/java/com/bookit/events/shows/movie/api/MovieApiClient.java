package com.bookit.events.shows.movie.api;

import com.bookit.events.shows.ResourceNotFoundException;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.movie.MovieClient;
import org.apache.commons.lang.CharSet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component("showsMovieApiClient")
@ConditionalOnProperty(value = "catalog.source", havingValue = "external", matchIfMissing = false)
public class MovieApiClient implements MovieClient {
    private RestClient restClient;
    private final DiscoveryClient discoveryClient;

    public MovieApiClient(DiscoveryClient discoveryClient) {
        this.restClient = RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, (req, res) -> {
                    System.out.println(res.getStatusCode());
                    System.out.println(res.getStatusText());
                    System.out.println(res.getBody());
                })
                .build();
        this.discoveryClient = discoveryClient;
    }
    
    private ServiceInstance getCatalogInstance(){
        return discoveryClient.getInstances("catalog").get(0);
    }
    
    @Override
    public Movie getMovieById(Long movieId) throws ResourceNotFoundException {
        ParameterizedTypeReference<Movie> typeReference = new ParameterizedTypeReference<Movie>() {};

//        URI uri = getCatalogInstance().getUri();

        String decodedUrl = URLDecoder.decode(getCatalogInstance().getUri().toString(), StandardCharsets.UTF_8);
        return this.restClient.get()
                .uri(
                    UriComponentsBuilder.fromUri(getCatalogInstance().getUri())
                            .pathSegment("api", "internal", "movie", movieId.toString())
                            .build().toUri()
                )
//                .uri("{decodedUrl}/api/internal/movie/{movieId}",
//                        decodedUrl,
//                        movieId)
//                .uri("{scheme}://{host}:{port}/api/internal/movie/{movieId}",
//                        uri.getScheme(),
//                        uri.getHost(),
//                        uri.getPort(),
//                        movieId)
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
