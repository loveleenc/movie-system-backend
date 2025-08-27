package com.bookit.application.DTO.show;

import com.bookit.application.entity.Show;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShowDTO {
    private String theatreName;
    private String language;
    private LocalDateTime starttime;
    private LocalDateTime endTime;
    private String showId;

    public ShowDTO(String theatreName, String language, Time starttime, Time endTime) {
        this.theatreName = theatreName;
        this.language = language;
        this.setStarttime(starttime);
        this.setEndTime(endTime);
    }

    public ShowDTO(Show show){
        this.theatreName = show.getTheatreName();
        this.language = show.getLanguage();
        this.starttime = show.getStarttime();
        this.endTime = show.getEndTime();
        this.showId = show.getShowId();
    }

    public String getShowId() {
        return showId;
    }

    public String getTheatreName() {
        return theatreName;
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

    public void setStarttime(Time starttime) {
        String date = starttime.toString().split(" ")[0];
        this.starttime = starttime.toLocalTime().atDate(LocalDate.parse(date));
    }

    public void setEndTime(Time endTime) {
        String date = endTime.toString().split(" ")[0];
        this.endTime = endTime.toLocalTime().atDate(LocalDate.parse(date));
    }
}
