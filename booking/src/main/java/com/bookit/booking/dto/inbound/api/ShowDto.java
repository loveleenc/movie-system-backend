package com.bookit.booking.dto.inbound.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDto {
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String id;
    @JsonProperty("theatre")
    private TheatreDto theatreDto;
    @JsonProperty("movie")
    private MovieDto movieDto;

    public ShowDto(TheatreDto theatreDto, MovieDto movieDto, LocalDateTime startTime, LocalDateTime endTime, String language, String id){
        this.theatreDto = theatreDto;
        this.movieDto = movieDto;
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

    public TheatreDto getTheatreDto(){
        return this.theatreDto;
    }

    public MovieDto getMovieDto() {
        return movieDto;
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
