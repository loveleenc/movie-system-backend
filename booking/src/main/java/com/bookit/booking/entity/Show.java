package com.bookit.booking.entity;


import java.time.LocalDateTime;
import java.util.UUID;

public class Show {
    private ShowTimeSlot timeSlot;
    private Theatre theatre;
    private Movie movie;
    private String language;
    private UUID id;


    public Show(ShowTimeSlot timeSlot, Theatre theatre, Movie movie, String language, UUID id) {
        this.timeSlot = timeSlot;
        this.theatre = theatre;
        this.movie = movie;
        this.language = language;
        this.id = id;
    }

    public Show(){}

    public Movie getMovie() {
        return movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public LocalDateTime getStartTime() {
        return this.timeSlot.startTime();
    }

    public LocalDateTime getEndTime() {
        return this.timeSlot.endTime();
    }

    public ShowTimeSlot getTimeSlot() {
        return this.timeSlot;
    }

    public UUID getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }
}
