package com.bookit.catalog.movie.services;


import com.bookit.catalog.movie.MovieException;
import com.bookit.catalog.movie.ResourceCreationException;
import com.bookit.catalog.movie.ResourceNotFoundException;
import com.bookit.catalog.movie.db.IMovieDao;
import com.bookit.catalog.movie.entity.MoviePage;
import com.bookit.catalog.movie.inbound.service.MoviePageServiceDto;
import com.bookit.catalog.movie.inbound.service.MovieServiceDto;
import com.bookit.catalog.movie.inbound.service.MovieServiceDtoMapper;
import com.bookit.catalog.movie.entity.Movie;
import com.bookit.catalog.movie.services.storage.StorageException;
import com.bookit.catalog.movie.services.storage.StorageService;
import com.bookit.catalog.movie.services.storage.UploadException;
import com.bookit.catalog.movie.services.storage.resource.PosterResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class MovieService {
  private MovieServiceDtoMapper movieDtoMapper;
  private StorageService storageService;
  private IMovieDao movieDao;
  private final Logger logger;

  public MovieService(IMovieDao movieDao, StorageService storageService, MovieServiceDtoMapper movieDtoMapper) {
    this.movieDao = movieDao;
    this.storageService = storageService;
    this.movieDtoMapper = movieDtoMapper;
    this.logger = LoggerFactory.getLogger(MovieService.class);
  }

  public MovieServiceDto findMovie(Long id) throws ResourceNotFoundException {
    Movie movie = this.movieDao.findById(id);
    try{
      PosterResource posterResource = this.storageService.getResource(movie.getPoster());
      movie.setPoster(posterResource.getContentOrUrlAsString());
    }
    catch(StorageException e){
      throw new MovieException("Unable to fetch the movie", e);
    }
    return this.movieDtoMapper.toDTO(movie);
  }

  private void populateMoviePosterContentOrUrl(List<Movie> movies){
    List<CompletableFuture<?>> futures = new ArrayList<>();
    for(Movie movie: movies){
      futures.add(CompletableFuture.supplyAsync(() ->
              this.storageService.getResource(movie.getPoster())
                      ).thenAccept(resource -> movie.setPoster(resource.getContentOrUrlAsString()))
              .handle((result, exception) -> {
                if(exception != null){
                  logger.warn(exception.getMessage()); //TODO: handle this correctly later
                }
                return result;
              })

      );
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
  }

  public MoviePageServiceDto getMovies(Integer page, Integer perPageCount) throws DataAccessException {
    MoviePage moviePage = this.movieDao.findMovies(page, perPageCount);
    this.populateMoviePosterContentOrUrl(moviePage.movies());
    return this.movieDtoMapper.toDTO(moviePage);
  }

  public List<MovieServiceDto> getOngoingMovies() throws DataAccessException, ExecutionException, InterruptedException {
    List<Movie> movies = this.movieDao.findOngoingMovies();
    this.populateMoviePosterContentOrUrl(movies);
    return this.movieDtoMapper.toDTO(movies);
  }

  public List<MovieServiceDto> getUpcomingMovies() throws DataAccessException, ExecutionException, InterruptedException {
    List<Movie> movies = this.movieDao.findUpcomingMovies();
    this.populateMoviePosterContentOrUrl(movies);
    return this.movieDtoMapper.toDTO(movies);

  }

  public List<MovieServiceDto> filterMovies(List<String> genre, List<String> languages, String releasedOnOrAfter) throws DateTimeParseException, ExecutionException, InterruptedException {
    LocalDate date = LocalDate.parse(releasedOnOrAfter);
    List<Movie> movies = this.movieDao.filterMovies(genre, languages, date);
    this.populateMoviePosterContentOrUrl(movies);
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
