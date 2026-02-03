package com.bookit.events.shows.dto.inbound.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowDto {
    private String language;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
