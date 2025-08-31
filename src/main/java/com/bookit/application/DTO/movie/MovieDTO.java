package com.bookit.application.DTO.movie;

import com.bookit.application.DTO.InvalidDataException;
import com.bookit.application.entity.Movie;
import com.bookit.application.services.MovieException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public MovieDTO() {}

    public MovieDTO(String name, Integer duration, String poster, List<String> genreList, List<String> languages, String releaseDate) {
        this.name = name;
        this.duration = duration;
        this.poster = poster;
        this.genreList = genreList;
        this.languages = languages;
        try{
            this.releaseDate = LocalDate.parse(releaseDate);
        }
        catch(DateTimeParseException e){
            throw new MovieException("Unable to parse provided date", e);
        }
    }
}
