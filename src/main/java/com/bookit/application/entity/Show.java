package com.bookit.application.entity;
import java.time.LocalDateTime;

public class Show {
    private String movieExternalId;
    private String theatreExternalId;
    private Long movieId;
    private Long theatreId;
    private String language;
    private TheatreTimeSlots timeSlot;
    private Theatre theatre;

    public Show(String movieExternalId, String theatreExternalId, String language, TheatreTimeSlots timeSlot) {
        this.movieExternalId = movieExternalId;
        this.theatreExternalId = theatreExternalId;
        this.language = language;
        this.timeSlot = timeSlot;
    }

    public Show(Long movieId, Long theatreId, String language, TheatreTimeSlots timeSlot) {
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.language = language;
        this.timeSlot = timeSlot;
    }

    public Show(Theatre theatre, TheatreTimeSlots timeSlot, String language){
        this.theatre = theatre;
        this.timeSlot = timeSlot;
        this.language = language;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public String getMovieExternalId() {
        return movieExternalId;
    }

    public String getTheatreExternalId() {
        return theatreExternalId;
    }

    public String getLanguage() {
        return language;
    }

    public LocalDateTime getStartTime() {
        return this.timeSlot.startTime();
    }

    public LocalDateTime getEndTime() {
        return this.timeSlot.endTime();
    }

    public Long getDuration(){
        return this.timeSlot.getSlotDuration();
    }

    public TheatreTimeSlots getTimeSlot(){
        return this.timeSlot;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public void setMovieExternalId(String movieExternalId) {
        this.movieExternalId = movieExternalId;
    }

    public void setTheatreExternalId(String theatreExternalId) {
        this.theatreExternalId = theatreExternalId;
    }
}
