package com.bookit.application.showscheduling.movie.local;

import com.bookit.application.showscheduling.ResourceNotFoundException;
import com.bookit.application.moviecatalog.MovieService;
import com.bookit.application.moviecatalog.dto.inbound.service.MovieServiceDto;
import com.bookit.application.showscheduling.comms.Request;
import com.bookit.application.showscheduling.comms.Response;
import com.bookit.application.showscheduling.entity.Movie;
import com.bookit.application.showscheduling.movie.MovieClient;
import org.springframework.stereotype.Component;

@Component
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
