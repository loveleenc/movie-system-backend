package com.bookit.application.services;


import com.bookit.application.DTO.MovieDTO;
import com.bookit.application.storage.BlobCrud;
import com.bookit.application.wrappers.MovieMapper;
import com.bookit.application.entity.Movie;
import com.bookit.application.repository.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class MovieService {

    @Autowired
    private BlobCrud blobCrud;
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

    public MovieDTO addMovie(Movie movie, MultipartFile file){
        Integer rows = this.movieDao.createNewMovie(movie);
        if(rows == 1){
            try{
                this.blobCrud.createBlob(movie.getPoster(), file.getInputStream());
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        return this.movieMapper.toDTO(movie);
    }

    public void uploadFile(MultipartFile file){
        try{
            this.blobCrud.createBlob(file.getOriginalFilename(), file.getInputStream());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
