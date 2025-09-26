package com.bookit.application.persistence;

import com.bookit.application.entity.Movie;

import java.time.LocalDate;
import java.util.List;

public interface IMovieDao extends Crud<Movie, Long> {

    List<Movie> findOngoingMovies();

    List<Movie> findUpcomingMovies();

    List<Movie> filterMovies(List<String> genre, List<String> languages, LocalDate releasedOnOrAfter);

    void deleteMovie(Movie movie);

//    @Override
//    List<Movie> findAll();
}
