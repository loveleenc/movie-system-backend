package com.bookit.application.entity;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Show {
    private String theatreName;
    private String language;
    private LocalDateTime starttime;
    private LocalDateTime endTime;
    private LocalDate movieReleaseDate;
    private String movieName;
    private String showId;

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowId() {
        return showId;
    }

    public void setMovieReleaseDate(LocalDate movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Show(String theatreName, LocalDateTime starttime, LocalDateTime endTime, String movieName, LocalDate movieReleaseDate, String language) {
        this.theatreName = theatreName;
        this.language = language;
        this.starttime = starttime;
        this.endTime = endTime;
        this.movieName = movieName;
        this.movieReleaseDate = movieReleaseDate;
    }

    public Show(){}

    public String getTheatreName() {
        return theatreName;
    }

    public String getMovieName() {
        return movieName;
    }

    public LocalDate getMovieReleaseDate() {
        return movieReleaseDate;
    }
    public String getLanguage() {
        return language;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStarttime(Date startdate, Time starttime) {
        this.starttime = starttime.toLocalTime().atDate(startdate.toLocalDate());
    }

    public void setEndTime(Date endDate, Time endTime) {
        this.endTime = endTime.toLocalTime().atDate(endDate.toLocalDate());
    }
}
