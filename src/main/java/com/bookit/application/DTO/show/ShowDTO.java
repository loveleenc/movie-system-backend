package com.bookit.application.DTO.show;

import com.bookit.application.DTO.theatre.TheatreDTO;
import com.bookit.application.services.MovieException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDTO {
    private String theatreId;
    private String movieId;
    private String language;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @JsonProperty
    private TheatreDTO theatre;

    public ShowDTO(String theatreId, String movieId, String language, String startTime, String endTime) {
        this.theatreId = theatreId;
        this.movieId = movieId;
        this.language = language;
        try{
            this.startTime = LocalDateTime.parse(startTime);
            this.endTime = LocalDateTime.parse(endTime);
        }
        catch(DateTimeParseException e){
            //TODO: handle an exception specifically for shows
            throw new MovieException("cant parse show timings");
        }
    }

    public ShowDTO(String theatreId, String movieId, String language, LocalDateTime startTime, LocalDateTime endTime) {
        this.theatreId = theatreId;
        this.movieId = movieId;
        this.language = language;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ShowDTO(String language, LocalDateTime startTime, LocalDateTime endTime, TheatreDTO theatre) {
        this.language = language;
        this.startTime = startTime;
        this.endTime = endTime;
        this.theatre = theatre;
    }

    public String getTheatreId() {
        return theatreId;
    }

    public String getMovieId() {
        return movieId;
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
