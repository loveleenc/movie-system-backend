package com.bookit.application.DTO.showAndTickets;

import com.bookit.application.DTO.showAndTickets.show.ShowDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowAndTicketDTO {
    @JsonProperty
    private ShowDTO show;
    private Long moviePrice;
    private String status;

    public ShowAndTicketDTO(ShowDTO show) {
        this.show = show;
    }

    public ShowAndTicketDTO(ShowDTO show, Long moviePrice, String status) {
        this.show = show;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    public ShowAndTicketDTO(){}

    public ShowDTO getShow() {
        return show;
    }

    public Long getMoviePrice() {
        return moviePrice;
    }

    public String getStatus() {
        return status;
    }
}
