package com.bookit.booking.dto.inbound.api.ticket;

import com.bookit.booking.dto.inbound.api.SeatDto;
import com.bookit.booking.dto.inbound.api.ShowDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {
    @JsonProperty("show")
    private ShowDto showDto;
    @JsonProperty("seat")
    private SeatDto seatDto;
    private String status;
    private Long price;
    @JsonProperty("id")
    private String ticketId;

    public TicketDto(ShowDto showDto, SeatDto seatDto, String status, Long price, String ticketId) {
        this.showDto = showDto;
        this.seatDto = seatDto;
        this.status = status;
        this.price = price;
        this.ticketId = ticketId;
    }

    public ShowDto getShowDto() {
        return showDto;
    }

    public SeatDto getSeatDto() {
        return seatDto;
    }

    public String getStatus() {
        return status;
    }

    public Long getPrice() {
        return price;
    }

    public String getTicketId() {
        return ticketId;
    }
}
