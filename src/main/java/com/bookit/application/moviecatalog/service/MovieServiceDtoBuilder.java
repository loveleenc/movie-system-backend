package com.bookit.application.moviecatalog.service;


import java.time.LocalDate;
import java.util.List;

public class MovieServiceDtoBuilder {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private List<String> languages;
    private LocalDate releaseDate;
    private Long id;


    public MovieServiceDtoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MovieServiceDtoBuilder setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public MovieServiceDtoBuilder setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public MovieServiceDtoBuilder setGenreList(List<String> genreList) {
        this.genreList = genreList;
        return this;
    }

    public MovieServiceDtoBuilder setLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    public MovieServiceDtoBuilder setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieServiceDtoBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieServiceDto build() {
        return new MovieServiceDto(
                name, duration, poster, genreList, languages, releaseDate, id
        );
    }
}
