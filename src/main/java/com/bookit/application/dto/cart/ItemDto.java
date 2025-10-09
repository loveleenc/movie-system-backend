package com.bookit.application.dto.cart;

import com.bookit.application.dto.ticket.TicketDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    @JsonProperty("ticket")
    private TicketDto ticketDto;

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
