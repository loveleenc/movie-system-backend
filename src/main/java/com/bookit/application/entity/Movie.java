package com.bookit.application.entity;


import com.bookit.application.types.MovieGenre;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class Movie {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private LocalDate releaseDate;

    public Movie(String name, Integer duration, String poster, List<String> genre, String releaseDate) {
        this.poster = poster;
        this.duration = duration;
        this.name = name;
        this.genreList = genre;
        this.releaseDate = LocalDate.parse(releaseDate);
    }

    public String getName() {
        return name;
    }

    public Integer getDuration() {
        return duration;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getPoster() {
        return poster;
    }


}
