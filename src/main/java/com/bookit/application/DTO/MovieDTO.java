package com.bookit.application.DTO;

import com.bookit.application.entity.Movie;
import com.bookit.application.repository.blob.MoviePosterBlob;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class MovieDTO {
    private String name;
    private Integer duration;
    private String poster;
    private List<String> genreList;

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

    private LocalDate releaseDate;


    public MovieDTO(Movie movie){
        this.name = movie.getName();
        this.duration = movie.getDuration();
        this.poster = movie.getPoster();
        this.genreList = movie.getGenreList();
        this.releaseDate = movie.getReleaseDate();
    }

    public void setPoster(String updatedPosterLink){
        this.poster = updatedPosterLink;
    }

    public static JSONObject transformm(Movie movie){
        JSONObject jo = new JSONObject();
        jo.put("name", movie.getName());
        jo.put("duration", movie.getDuration());
        jo.put("poster", movie.getPoster());
        jo.put("duration", movie.getDuration());
        jo.put("releaseDate", movie.getReleaseDate().toString());
        return jo;
    }



}
