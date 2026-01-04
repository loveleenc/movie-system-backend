package com.bookit.application.services;

import com.bookit.application.moviecatalog.entity.Movie;
import com.bookit.application.moviecatalog.entity.MovieBuilder;
import com.bookit.application.moviecatalog.service.MovieService;
import com.bookit.application.moviecatalog.db.IMovieDao;
import com.bookit.application.moviecatalog.service.MovieServiceDto;
import com.bookit.application.moviecatalog.service.MovieServiceDtoBuilder;
import com.bookit.application.moviecatalog.service.MovieServiceDtoMapper;
import com.bookit.application.moviecatalog.storage.StorageService;
import com.bookit.application.moviecatalog.storage.resource.PosterResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieTest {
    MovieService movieService;
    private IMovieDao movieDao;
    private StorageService storageService;
    private MovieServiceDtoMapper movieServiceDtoMapper;
    private List<Movie> movies;
    private PosterResource posterResource;

    @BeforeEach
    public void before() {
        this.posterResource = mock(PosterResource.class);
        this.movieDao = mock(IMovieDao.class);
        this.storageService = mock(StorageService.class);
        this.movieServiceDtoMapper = new MovieServiceDtoMapper(this.storageService);
        this.movieService = new MovieService(this.movieDao, this.storageService, this.movieServiceDtoMapper);
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
    public void test_whenNoPosterFileProvidedDuringMovieCreation_thenNullPointerExceptionIsThrown() {
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        MovieServiceDto movie = new MovieServiceDtoBuilder()
                .setName("Batman")
                .setPoster("batman.png")
                .setDuration(81)
                .setGenreList(genre)
                .setLanguages(languages)
                .setReleaseDate(LocalDate.of(2027, 9, 19))
                .build();

        Assertions.assertThrows(NullPointerException.class, () -> this.movieService.addMovie(movie, null));
    }

    @Test
    public void doSomething(){
        String posterResourcePath = "path-aaa.png";
        when(this.storageService.getResource(any(String.class))).thenReturn(this.posterResource);
        when(this.posterResource.getContentOrUrlAsString()).thenReturn(posterResourcePath);
        when(this.movieDao.findAll()).thenReturn(this.movies);
        Assertions.assertEquals(movies.size(), this.movieService.getMovies().size());
    }

    @Test
    public void test_whenInvalidDateIsGivenToFilterMovies_thenDateTimeParseExceptionIsThrown(){
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        Assertions.assertThrows(DateTimeParseException.class, () -> this.movieService.filterMovies(genre, languages, "2020-1-2"));
    }

    @Test
    public void test_creatingMovie(){
        String posterName = "Maximum_Overdrive.png";
        MockMultipartFile poster = new MockMultipartFile(posterName, "some something something".getBytes());
        Movie movie = new MovieBuilder()
                .setName("Maximum Overdrive")
                .setPoster("")
                .setDuration(98)
                .setGenreList(List.of("Action", "Horror", "Sci-Fi"))
                .setLanguages(List.of("English"))
                .setReleaseDate(LocalDate.of(1986, 7, 25))
                .build();
        MovieServiceDto movieServiceDto = new MovieServiceDtoBuilder()
          .setName("Maximum Overdrive")
          .setPoster("")
          .setDuration(98)
          .setGenreList(List.of("Action", "Horror", "Sci-Fi"))
          .setLanguages(List.of("English"))
          .setReleaseDate(LocalDate.of(1986, 7, 25))
          .build();
        Movie returnedMovie = new MovieBuilder()
                .setName("Maximum Overdrive")
                .setPoster(posterName)
                .setDuration(98)
                .setGenreList(List.of("Action", "Horror", "Sci-Fi"))
                .setLanguages(List.of("English"))
                .setReleaseDate(LocalDate.of(1986, 7, 25))
                .setId(2L)
                .build();

        String posterResourcePath = "path-" + posterName;
        when(this.movieDao.create(any(Movie.class))).thenReturn(2L);
        when(this.movieDao.findById(2L)).thenReturn(returnedMovie);
        when(this.storageService.getResource(any(String.class))).thenReturn(this.posterResource);
        when(this.posterResource.getContentOrUrlAsString()).thenReturn(posterResourcePath);
        MovieServiceDto createdMovie = this.movieService.addMovie(movieServiceDto, poster);
        Assertions.assertEquals(movie.getName(), createdMovie.getName());
        Assertions.assertEquals(posterResourcePath, createdMovie.getPoster());
        Assertions.assertEquals(2L, createdMovie.getId());
        Assertions.assertEquals(movie.getDuration(), createdMovie.getDuration());
        //Etc etc for the remaining props
    }
}
