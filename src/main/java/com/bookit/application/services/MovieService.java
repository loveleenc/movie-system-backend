package com.bookit.application.services;


import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.types.MovieGenre;
import com.bookit.application.types.MovieLanguage;
import com.bookit.application.wrappers.MovieMapper;
import com.bookit.application.entity.Movie;
import com.bookit.application.repository.database.MovieDAO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MovieService {

    private MovieDAO movieDao;
    private MovieMapper movieMapper;

    public MovieService(MovieDAO movieDao, MovieMapper movieMapper){
        this.movieDao = movieDao;
        this.movieMapper = movieMapper;
    }

    public MovieDTO findMovie(Long id){
        Movie movie = this.movieDao.findById(id);
        return this.movieMapper.toDTO(movie);
    }

    public List<MovieDTO> getMovies(){
        return this.movieMapper.transformAllMovies(this.movieDao.findAll());
    }

    public List<MovieDTO> getOngoingMovies(){
        return this.movieMapper.transformAllMovies(this.movieDao.findMoviesWithActiveTickets());
    }

    public List<MovieDTO> getUpcomingMovies(){
        return this.movieMapper.transformAllMovies(this.movieDao.findUpcomingMovies());
    }

    public List<MovieDTO> filterMovies(List<String> genre, List<String> languages, String releasedOnOrAfter){
        LocalDate date = LocalDate.parse(releasedOnOrAfter);
        return this.movieMapper.transformAllMovies(this.movieDao.filterMovies(genre, languages, date));
    }
}
