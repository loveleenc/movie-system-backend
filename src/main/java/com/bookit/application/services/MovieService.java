package com.bookit.application.services;


import com.bookit.application.DTO.movie.MovieDTO;
import com.bookit.application.services.storage.StorageService;
import com.bookit.application.services.storage.UploadException;
import com.bookit.application.DTO.movie.MovieDTOMapper;
import com.bookit.application.entity.Movie;
import com.bookit.application.repository.MovieDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Component
public class MovieService {

    private StorageService storageService;
    private MovieDAO movieDao;
    private MovieDTOMapper movieDTOMapper;

    public MovieService(MovieDAO movieDao, MovieDTOMapper movieDTOMapper, StorageService storageService){
        this.movieDao = movieDao;
        this.movieDTOMapper = movieDTOMapper;
        this.storageService = storageService;
    }

    public MovieDTO findMovie(Long id){
        Movie movie = this.movieDao.findById(id);
        return this.movieDTOMapper.toDTO(movie);
    }

    public List<MovieDTO> getMovies(){
        return this.movieDTOMapper.toDTO(this.movieDao.findAll());
    }

    public List<MovieDTO> getOngoingMovies(){
        return this.movieDTOMapper.toDTO(this.movieDao.findMoviesWithActiveTickets());
    }

    public List<MovieDTO> getUpcomingMovies(){
        return this.movieDTOMapper.toDTO(this.movieDao.findUpcomingMovies());
    }

    public List<MovieDTO> filterMovies(List<String> genre, List<String> languages, String releasedOnOrAfter){
        LocalDate date = LocalDate.parse(releasedOnOrAfter);
        return this.movieDTOMapper.toDTO(this.movieDao.filterMovies(genre, languages, date));
    }

    public MovieDTO addMovie(Movie movie, MultipartFile file) throws MovieException {
        movie.setPoster(file.getOriginalFilename());
        try{
            Long movieId = this.movieDao.create(movie);
            Movie createdMovie = this.movieDao.findById(movieId);
            this.storageService.store(file, false);
            return this.movieDTOMapper.toDTO(createdMovie);
        }
        catch(DataAccessException | NullPointerException e){
            throw new MovieException("Unable to create the movie", e);
        }
        catch(UploadException e){
            System.out.println(e.getMessage());
            this.movieDao.deleteMovie(movie);
            throw new MovieException("Unable to create the movie", e);
        }
    }
}
