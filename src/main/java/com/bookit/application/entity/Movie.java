package com.bookit.application.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {
    @NonNull
    private String name;
    @NonNull
    private Integer duration;
    @NonNull
    private String poster;
    @NonNull
    private List<String> genreList;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private List<String> languages;
    private Long id;

    public Movie(@NonNull String name,
                 @NonNull Integer duration,
                 @NonNull String poster,
                 @NonNull List<String> genre,
                 @NonNull LocalDate releaseDate,
                 @NonNull List<String> languages,
                 Long id) {
        this.poster = poster;
        this.duration = duration;
        this.name = name;
        this.genreList = genre;
        this.releaseDate = releaseDate;
        this.languages = languages;
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setPoster(String filename) {
        this.poster = filename;
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

    public List<String> getLanguages() {
        return this.languages;
    }
}
