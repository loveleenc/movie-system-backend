package com.bookit.application.booking.dto.inbound.api.cart;

import com.bookit.application.booking.dto.inbound.api.ticket.TicketDto;
import com.bookit.application.booking.dto.inbound.api.ticket.TicketDtoMapper;
import com.bookit.application.booking.entity.Item;
import com.bookit.application.booking.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDtoMapper {
    private TicketDtoMapper ticketDtoMapper;

    public ItemDtoMapper(TicketDtoMapper ticketDtoMapper) {
        this.ticketDtoMapper = ticketDtoMapper;
    }

    private TicketDto toTicketDto(Ticket ticket){
        return this.ticketDtoMapper.toTicketDto(ticket, true, ticket.getShow());
    }

    public ItemDto toItemDto(Item item){
        return new ItemDto(item.getId(), this.toTicketDto(item.getTicket()));
    }

    public List<ItemDto> toCartItemsDTO(List<Item> items){
        return items.stream().map(this::toItemDto).toList();
    }

}
