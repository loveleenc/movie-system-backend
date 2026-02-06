package com.bookit.catalog.movie.db;

import com.bookit.catalog.movie.ResourceNotFoundException;
import com.bookit.catalog.movie.entity.Movie;
import com.bookit.catalog.movie.entity.MoviePage;

import java.time.LocalDate;
import java.util.List;

public interface IMovieDao {

    List<Movie> findOngoingMovies();

    List<Movie> findUpcomingMovies();

    List<Movie> filterMovies(List<String> genre, List<String> languages, LocalDate releasedOnOrAfter);

    void deleteMovie(Movie movie);

    Movie findById(Long id) throws ResourceNotFoundException;

    MoviePage findMovies(Integer page, Integer perPageCount);

    Long create(Movie movie);
//    @Override
////    List<Movie> findAll();
}
