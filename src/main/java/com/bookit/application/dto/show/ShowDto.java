package com.bookit.application.dto.show;

import com.bookit.application.dto.movie.MovieDto;
import com.bookit.application.dto.theatre.TheatreDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDto {
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String id;
    @JsonProperty
    private TheatreDto theatre;
    @JsonProperty
    private MovieDto movie;

    public ShowDto(TheatreDto theatre, MovieDto movie, LocalDateTime startTime, LocalDateTime endTime, String language, String id){
        this.theatre = theatre;
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = endTime;
        this.language = language;
        this.id = id;
    }

    public ShowDto() {
    }

    public String getId() {
        return id;
    }

    public TheatreDto getTheatre(){
        return this.theatre;
    }

    public MovieDto getMovie() {
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
