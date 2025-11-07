package com.bookit.application.dto.ticket;


import org.springframework.lang.NonNull;

public class TicketCreationDto {
    @NonNull
    private Long moviePrice;
    @NonNull
    private String status;
    @NonNull
    private String showId;

    public TicketCreationDto(String showId, Long moviePrice, String status) {
        this.showId = showId;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    public Long getMoviePrice() {
        return moviePrice;
    }

    public String getStatus() {
        return status;
    }

    public String getShowId() {
        return showId;
    }
}
