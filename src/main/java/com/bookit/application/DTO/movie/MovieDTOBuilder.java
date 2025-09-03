package com.bookit.application.DTO.movie;

import java.time.LocalDate;
import java.util.List;

public class MovieDTOBuilder {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private List<String> languages;
    private LocalDate releaseDate;
    private Long id;


    public MovieDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MovieDTOBuilder setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public MovieDTOBuilder setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public MovieDTOBuilder setGenreList(List<String> genreList) {
        this.genreList = genreList;
        return this;
    }

    public MovieDTOBuilder setLanguages(List<String> languages) {
        this.languages = languages;
        return this;
    }

    public MovieDTOBuilder setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieDTOBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieDTO build() {
        return new MovieDTO(
                name, duration, poster, genreList, languages, releaseDate, id
        );
    }
}
