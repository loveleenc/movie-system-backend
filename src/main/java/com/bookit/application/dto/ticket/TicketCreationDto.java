package com.bookit.application.dto.ticket;

public class TicketCreationDto {
    private String showId;
    private Long moviePrice;
    private String status;

    public TicketCreationDto(String showId, Long moviePrice, String status) {
        this.showId = showId;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    public String getShowId() {
        return showId;
    }

    public Long getMoviePrice() {
        return moviePrice;
    }

    public String getStatus() {
        return status;
    }
}
