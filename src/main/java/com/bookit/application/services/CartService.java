package com.bookit.application.services;

import com.bookit.application.entity.Item;
import com.bookit.application.entity.Ticket;
import com.bookit.application.persistence.ICartDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    private ICartDao cartDao;
    private UserService userService;
    private TicketService ticketService;

    public CartService(ICartDao cartDao, UserService userService, TicketService ticketService) {
        this.cartDao = cartDao;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    public List<Item> getCart(){
        Long userId = this.userService.getCurrentUserId();
        return this.cartDao.get(userId);
    }

    public void removeItem(Long itemId){
        Item item = this.cartDao.findById(itemId);
        if(!Objects.isNull(item)){
            Long userId = this.userService.getCurrentUserId();
            if(!item.getTicket().getOwnerId().equals(userId)){
                throw new TicketBookingException("Item cannot be removed from cart by another user");
            }
            this.cartDao.remove(itemId);
            //TODO: update cart expiry time
            this.ticketService.releaseTicket(item.getTicket());
        }
    }

    public Item addItem(String ticketId){
        Long userId = this.userService.getCurrentUserId();
        Ticket ticket = this.ticketService.reserveTicket(ticketId, userId);
        Long itemId = this.cartDao.add(ticket, userId);
        //TODO: update cart expiry time
        return this.cartDao.findById(itemId);
    }

    public void checkout(){
        List<Item> items = this.getCart();
        //TODO: update cart expiry time
        //TODO: get price breakdown for each of the items and add the tax
        //TODO: return the items and the price breakdown
    }

    public List<Ticket> confirmBooking(){
        List<Item> items = this.getCart();
        List<Ticket> bookedTickets = this.ticketService.bookTickets(items.stream().map(Item::getTicket).toList());
        items.forEach(item -> {
            this.cartDao.remove(item.getId());
        });
        //TODO: cancel cart expiry job
        return bookedTickets;
    }

    public void createCartForNewUser(Long userId){
        this.cartDao.createNewCart(userId);
    }

}
