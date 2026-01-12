package com.bookit.booking.dto.inbound.api.cart;

import com.bookit.booking.dto.inbound.api.ticket.TicketDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    @JsonProperty("ticket")
    private TicketDto ticketDto;
    private String movie;


    public ItemDto(Long id, TicketDto ticketDto) {
        this.id = id;
        this.ticketDto = ticketDto;
    }

    public Long getId() {
        return id;
    }

    public TicketDto getTicketDto() {
        return ticketDto;
    }
}
