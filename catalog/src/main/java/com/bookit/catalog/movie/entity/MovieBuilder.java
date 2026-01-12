package com.bookit.catalog.movie.entity;

import java.time.LocalDate;
import java.util.List;

public class MovieBuilder {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private LocalDate releaseDate;
    private List<String> languages;
    private Long id;

    public MovieBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MovieBuilder setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public MovieBuilder setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public MovieBuilder setGenreList(List<String> genreList) {
        this.genreList = genreList;
        return this;
    }

    public MovieBuilder setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieBuilder setLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    public MovieBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public Movie build() {
        return new Movie(name,
                duration,
                poster,
                genreList,
                releaseDate,
                languages,
                id
        );
    }
}
