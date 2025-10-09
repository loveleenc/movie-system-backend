package com.bookit.application.dto.cart;

import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDtoMapper {
    private TicketDtoMapper ticketDtoMapper;

    public ItemDtoMapper(TicketDtoMapper ticketDtoMapper) {
        this.ticketDtoMapper = ticketDtoMapper;
    }

    private TicketDto toTicketDto(Ticket ticket){
        return this.ticketDtoMapper.toTicketDto(ticket, true);
    }

    public ItemDto toItemDto(Item item){
        return new ItemDto(item.getId(), this.toTicketDto(item.getTicket()));
    }

    public List<ItemDto> toCartItemsDTO(List<Item> items){
        return items.stream().map(this::toItemDto).toList();
    }

}
