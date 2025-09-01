package com.bookit.application.entity;
import java.time.LocalDateTime;

public class Show {
    private String movieExternalId;
    private String theatreExternalId;
    private Long movieId;
    private Long theatreId;
    private String language;
    private TheatreTimeSlots timeSlots;

    public Show(String movieExternalId, String theatreExternalId, String language, LocalDateTime startTime, LocalDateTime endTime) {
        this.movieExternalId = movieExternalId;
        this.theatreExternalId = theatreExternalId;
        this.language = language;
        this.timeSlots = new TheatreTimeSlots(startTime, endTime);
    }

    public Show(Long movieId, Long theatreId, String language, LocalDateTime startTime, LocalDateTime endTime) {
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.language = language;
        this.timeSlots = new TheatreTimeSlots(startTime, endTime);
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
        return this.timeSlots.startTime();
    }

    public LocalDateTime getEndTime() {
        return this.timeSlots.endTime();
    }

    public Long getDuration(){
        return this.timeSlots.getSlotDuration();
    }

    public TheatreTimeSlots getTimeSlot(){
        return this.timeSlots;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }
}
