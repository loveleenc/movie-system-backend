package com.bookit.application.DTO.show;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowAndTicketCreationDTO {
    @JsonProperty
    private ShowDTO show;
    private Long moviePrice;
    private String status;

    public ShowAndTicketCreationDTO(ShowDTO show) {
        this.show = show;
    }

    public ShowAndTicketCreationDTO(ShowDTO show, Long moviePrice, String status) {
        this.show = show;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    public ShowAndTicketCreationDTO(){}

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
