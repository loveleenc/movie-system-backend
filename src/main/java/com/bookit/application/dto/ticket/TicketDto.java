package com.bookit.application.dto.ticket;

import com.bookit.application.dto.seat.SeatDto;
import com.bookit.application.dto.show.ShowDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {
    @JsonProperty
    private ShowDto show;
    @JsonProperty
    private SeatDto seat;
    private String status;
    private Long price;

    public TicketDto(ShowDto show, SeatDto seat, String status, Long price) {
        this.show = show;
        this.seat = seat;
        this.status = status;
        this.price = price;
    }

    public ShowDto getShow() {
        return show;
    }

    public SeatDto getSeat() {
        return seat;
    }

    public String getStatus() {
        return status;
    }

    public Long getPrice() {
        return price;
    }
}
