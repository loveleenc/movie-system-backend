package com.bookit.application.moviecatalog;


import com.bookit.application.common.ResourceCreationException;
import com.bookit.application.moviecatalog.db.IMovieDao;
import com.bookit.application.moviecatalog.dto.inbound.service.MovieServiceDto;
import com.bookit.application.moviecatalog.dto.inbound.service.MovieServiceDtoMapper;
import com.bookit.application.moviecatalog.entity.Movie;
import com.bookit.application.moviecatalog.storage.StorageService;
import com.bookit.application.moviecatalog.storage.UploadException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class MovieService {
  private MovieServiceDtoMapper movieDtoMapper;
  private StorageService storageService;
  private IMovieDao movieDao;

  public MovieService(IMovieDao movieDao, StorageService storageService, MovieServiceDtoMapper movieDtoMapper) {
    this.movieDao = movieDao;
    this.storageService = storageService;
    this.movieDtoMapper = movieDtoMapper;
  }

  public MovieServiceDto findMovie(Long id) throws DataAccessException {
    Movie movie = this.movieDao.findById(id);
    return this.movieDtoMapper.toDTO(movie);
  }

  public List<MovieServiceDto> getMovies() throws DataAccessException {
    List<Movie> movies = this.movieDao.findAll();
    return this.movieDtoMapper.toDTO(movies);
  }

  public List<MovieServiceDto> getOngoingMovies() throws DataAccessException {
    List<Movie> movies = this.movieDao.findOngoingMovies();
    return this.movieDtoMapper.toDTO(movies);
  }

  public List<MovieServiceDto> getUpcomingMovies() throws DataAccessException {
    List<Movie> movies = this.movieDao.findUpcomingMovies();
    return this.movieDtoMapper.toDTO(movies);

  }


  public List<MovieServiceDto> filterMovies(List<String> genre, List<String> languages, String releasedOnOrAfter) throws DateTimeParseException {
    LocalDate date = LocalDate.parse(releasedOnOrAfter);
    List<Movie> movies = this.movieDao.filterMovies(genre, languages, date);
    return this.movieDtoMapper.toDTO(movies);
  }

  public MovieServiceDto addMovie(MovieServiceDto movieDto, MultipartFile file) throws ResourceCreationException {
    Movie movie = this.movieDtoMapper.toMovie(movieDto);
    try {
      movie.setPoster(file.getOriginalFilename());
      Long movieId = this.movieDao.create(movie);
      Movie createdMovie = this.movieDao.findById(movieId);
      this.storageService.store(file, false);
      return this.movieDtoMapper.toDTO(createdMovie);
    } catch (DataAccessException e) {
      throw new ResourceCreationException("Unable to create the movie", e);
    } catch (UploadException e) {
      this.movieDao.deleteMovie(movie);
      throw new ResourceCreationException("Unable to create the movie: poster upload has failed", e);
    }
  }
}
