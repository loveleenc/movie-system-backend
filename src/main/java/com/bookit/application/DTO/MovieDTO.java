package com.bookit.application.DTO;

import com.bookit.application.entity.Movie;

import java.time.LocalDate;
import java.util.List;


public class MovieDTO {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private List<String> languages;
    private LocalDate releaseDate;

    public String getName() {
        return name;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getPoster() {
        return poster;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public List<String> getLanguages(){
        return this.languages;
    }

    public MovieDTO(Movie movie){
        this.name = movie.getName();
        this.duration = movie.getDuration();
        this.poster = movie.getPoster();
        this.genreList = movie.getGenreList();
        this.releaseDate = movie.getReleaseDate();
        this.languages = movie.getLanguages();
    }

    public void setPoster(String updatedPosterLink){
        this.poster = updatedPosterLink;
    }

}
