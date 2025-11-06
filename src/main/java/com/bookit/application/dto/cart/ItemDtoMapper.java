package com.bookit.application.dto.cart;

import com.bookit.application.dto.show.ShowDto;
import com.bookit.application.dto.show.ShowDtoMapper;
import com.bookit.application.dto.ticket.TicketDto;
import com.bookit.application.dto.ticket.TicketDtoMapper;
import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDtoMapper {
    private TicketDtoMapper ticketDtoMapper;
    private final ShowDtoMapper showDtoMapper;

    public ItemDtoMapper(TicketDtoMapper ticketDtoMapper, ShowDtoMapper showDtoMapper) {
        this.ticketDtoMapper = ticketDtoMapper;
        this.showDtoMapper = showDtoMapper;
    }

    private TicketDto toTicketDto(Ticket ticket){
        ShowDto showDto = this.showDtoMapper.toDTO(ticket.getShow());
        return this.ticketDtoMapper.toTicketDto(ticket, true, showDto);
    }

    public ItemDto toItemDto(Item item){
        return new ItemDto(item.getId(), this.toTicketDto(item.getTicket()));
    }

    public List<ItemDto> toCartItemsDTO(List<Item> items){
        return items.stream().map(this::toItemDto).toList();
    }

}
