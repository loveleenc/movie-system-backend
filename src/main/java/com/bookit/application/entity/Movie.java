package com.bookit.application.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;
    private LocalDate releaseDate;
    private List<String> languages;
    private Map<String, Object> extraProps;

    public Movie(String name, Integer duration, String poster, List<String> genre, String releaseDate, List<String> languages) {
        this.poster = poster;
        this.duration = duration;
        this.name = name;
        this.genreList = genre;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.languages = languages;
    }

    @JsonAnySetter
    public void addExtra(String key, Object value){
        this.extraProps.put(key, value);
    }

    @JsonGetter
    public InputStream getPosterData(){
        return (InputStream) this.extraProps.get("posterData");
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

    public List<String> getLanguages(){
        return this.languages;
    }
}
