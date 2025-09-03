package com.bookit.application.DTO.show;

import com.bookit.application.DTO.movie.MovieDTO;
import com.bookit.application.DTO.theatre.TheatreDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDTO {
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String id;
    @JsonProperty
    private TheatreDTO theatre;
    @JsonProperty
    private MovieDTO movie;

    public ShowDTO(TheatreDTO theatre, MovieDTO movie, LocalDateTime startTime, LocalDateTime endTime, String language, String id){
        this.theatre = theatre;
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.id = id;
    }

    public ShowDTO() {
    }

    public String getId() {
        return id;
    }

    public TheatreDTO getTheatre(){
        return this.theatre;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public String getLanguage() {
        return language;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
