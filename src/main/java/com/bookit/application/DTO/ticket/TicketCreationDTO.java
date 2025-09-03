package com.bookit.application.DTO.ticket;

public class TicketCreationDTO {
    private String showId;
    private Long moviePrice;
    private String status;

    public TicketCreationDTO(String showId, Long moviePrice, String status) {
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
