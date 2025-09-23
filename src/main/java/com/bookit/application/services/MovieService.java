package com.bookit.application.services;


import com.bookit.application.services.storage.StorageService;
import com.bookit.application.services.storage.UploadException;
import com.bookit.application.entity.Movie;
import com.bookit.application.persistence.IMovieDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Component
public class MovieService {

    private StorageService storageService;
    private IMovieDao movieDao;

    public MovieService(IMovieDao movieDao, StorageService storageService){
        this.movieDao = movieDao;
        this.storageService = storageService;
    }

    public Movie findMovie(Long id){
        return this.movieDao.findById(id);
    }

    public List<Movie> getMovies(){
        return this.movieDao.findAll();
    }

    public List<Movie> getOngoingMovies(){
        return this.movieDao.findOngoingMovies();
    }

    public List<Movie> getUpcomingMovies(){
        return this.movieDao.findUpcomingMovies();
    }


    public List<Movie> filterMovies(List<String> genre, List<String> languages, String releasedOnOrAfter){
        LocalDate date = LocalDate.parse(releasedOnOrAfter);
        return this.movieDao.filterMovies(genre, languages, date);
    }

    public Movie addMovie(Movie movie, MultipartFile file) throws MovieException {
        movie.setPoster(file.getOriginalFilename());
        try{
            Long movieId = this.movieDao.create(movie);
            Movie createdMovie = this.movieDao.findById(movieId);
            this.storageService.store(file, false);
            return createdMovie;
        }
        catch(DataAccessException | NullPointerException e){
            throw new MovieException("Unable to create the movie", e);
        }
        catch(UploadException e){
            System.out.println(e.getMessage());
            this.movieDao.deleteMovie(movie);
            throw new MovieException("Unable to create the movie due to ", e);
        }
    }
}
