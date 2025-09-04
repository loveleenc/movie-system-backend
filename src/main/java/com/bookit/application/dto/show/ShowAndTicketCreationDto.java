package com.bookit.application.dto.show;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowAndTicketCreationDto {
    @JsonProperty
    private ShowDto show;
    private Long moviePrice;
    private String status;

    public ShowAndTicketCreationDto(ShowDto show) {
        this.show = show;
    }

    public ShowAndTicketCreationDto(ShowDto show, Long moviePrice, String status) {
        this.show = show;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    public ShowAndTicketCreationDto(){}

    public ShowDto getShow() {
        return show;
    }

    public Long getMoviePrice() {
        return moviePrice;
    }

    public String getStatus() {
        return status;
    }
}
