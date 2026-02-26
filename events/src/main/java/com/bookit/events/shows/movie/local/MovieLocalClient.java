package com.bookit.events.shows.movie.local;

import com.bookit.events.shows.ResourceNotFoundException;
import com.bookit.catalog.movie.MovieService;
import com.bookit.catalog.movie.inbound.service.MovieServiceDto;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.movie.MovieClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component("showsMovieLocalClient")
@ComponentScan(basePackages = {"com.bookit.catalog.movie"})
public class MovieLocalClient implements MovieClient {
  private MovieService movieService;

  public MovieLocalClient(MovieService movieService) {
    this.movieService = movieService;
  }

  @Override
  public Movie getMovieById(Long movieId) throws ResourceNotFoundException {
    MovieServiceDto movieServiceDto = this.movieService.findMovie(movieId);
    return new Movie(movieServiceDto.getName(),
      movieServiceDto.getDuration(),
      movieServiceDto.getPoster(),
      movieServiceDto.getGenreList(),
      movieServiceDto.getReleaseDate(),
      movieServiceDto.getLanguages(),
      movieServiceDto.getId());
  }

  @Override
  public void sendRequest(Request request) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Object processResponse(Response response) {
    throw new RuntimeException("Not implemented yet");
  }
}
