package com.bookit.application.DTO.ticket;

import com.bookit.application.DTO.seat.SeatDTO;
import com.bookit.application.DTO.show.ShowDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDTO {
    @JsonProperty
    private ShowDTO show;
    @JsonProperty
    private SeatDTO seat;
    private String status;
    private Long price;

    public TicketDTO(ShowDTO show, SeatDTO seat, String status, Long price) {
        this.show = show;
        this.seat = seat;
        this.status = status;
        this.price = price;
    }

    public ShowDTO getShow() {
        return show;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public String getStatus() {
        return status;
    }

    public Long getPrice() {
        return price;
    }
}
