package com.bookit.catalog.movie.inbound.api;

import java.time.LocalDate;
import java.util.List;

public class MovieDtoBuilder {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private List<String> languages;
    private LocalDate releaseDate;
    private Long id;


    public MovieDtoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MovieDtoBuilder setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public MovieDtoBuilder setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public MovieDtoBuilder setGenreList(List<String> genreList) {
        this.genreList = genreList;
        return this;
    }

    public MovieDtoBuilder setLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    public MovieDtoBuilder setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieDtoBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieDto build() {
        return new MovieDto(
                name, duration, poster, genreList, languages, releaseDate, id
        );
    }
}
