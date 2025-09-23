package com.bookit.application.services;

import com.bookit.application.entity.Movie;
import com.bookit.application.entity.MovieBuilder;
import com.bookit.application.persistence.IMovieDao;
import com.bookit.application.services.storage.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieTest {
    MovieService movieService;
    private IMovieDao movieDao;
    private StorageService storageService;
    private List<Movie> movies;

    @BeforeEach
    public void before() {
        this.movieDao = mock(IMovieDao.class);
        this.storageService = mock(StorageService.class);
        this.movieService = new MovieService(this.movieDao, this.storageService);
        this.movies = new ArrayList<>();
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        movies.add(
                new MovieBuilder()
                        .setName("Inception")
                        .setPoster("inception.png")
                        .setDuration(148)
                        .setGenreList(genre)
                        .setLanguages(languages)
                        .setReleaseDate(LocalDate.of(2010, 7, 16))
                        .build()
        );

        movies.add(
                new MovieBuilder()
                        .setName("The Dark Knight")
                        .setPoster("dark_knight.png")
                        .setDuration(152)
                        .setGenreList(genre)
                        .setLanguages(languages)
                        .setReleaseDate(LocalDate.of(2008, 7, 18))
                        .build()
        );

        movies.add(
                new MovieBuilder()
                        .setName("Interstellar")
                        .setPoster("interstellar.png")
                        .setDuration(169)
                        .setGenreList(genre)
                        .setLanguages(languages)
                        .setReleaseDate(LocalDate.of(2014, 11, 7))
                        .build()
        );

        movies.add(
                new MovieBuilder()
                        .setName("Avatar")
                        .setPoster("avatar.png")
                        .setDuration(162)
                        .setGenreList(genre)
                        .setLanguages(languages)
                        .setReleaseDate(LocalDate.of(2009, 12, 18))
                        .build()
        );

        movies.add(
                new MovieBuilder()
                        .setName("The Lion King")
                        .setPoster("lion_king.png")
                        .setDuration(88)
                        .setGenreList(genre)
                        .setLanguages(languages)
                        .setReleaseDate(LocalDate.of(1994, 6, 24))
                        .build()
        );
    }

    @Test()
    public void testGetOngoingMoviesReturnsOnlyOngoingMovies() {
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        Movie movie = new MovieBuilder()
                .setName("Batman")
                .setPoster("batman.png")
                .setDuration(81)
                .setGenreList(genre)
                .setLanguages(languages)
                .setReleaseDate(LocalDate.of(2027, 9, 19))
                .build();

        Assertions.assertThrows(MovieException.class, () -> this.movieService.addMovie(movie, null));
    }

    @Test
    public void doSomething(){
        when(this.movieDao.findAll()).thenReturn(this.movies);
        Assertions.assertEquals(movies.size(), this.movieService.getMovies().size());
    }

    @Test
    public void filterMovies(){
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        Assertions.assertThrows(DateTimeParseException.class, () -> this.movieService.filterMovies(genre, languages, "2020-1-2"));
    }
}
