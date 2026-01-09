package com.bookit.application.moviecatalog.db;

import com.bookit.application.common.ResourceNotFoundException;
import com.bookit.application.moviecatalog.entity.Movie;

import java.time.LocalDate;
import java.util.List;

public interface IMovieDao extends Crud<Movie, Long> {

    List<Movie> findOngoingMovies();

    List<Movie> findUpcomingMovies();

    List<Movie> filterMovies(List<String> genre, List<String> languages, LocalDate releasedOnOrAfter);

    void deleteMovie(Movie movie);

    @Override
    Movie findById(Long id) throws ResourceNotFoundException;
//    @Override
//    List<Movie> findAll();
}
