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
            this.cartDao.extendCartExpiry(userId);
            this.ticketService.releaseTicket(item.getTicket());
        }
    }

    public Item addItem(String ticketId){
        Long userId = this.userService.getCurrentUserId();
        Ticket ticket = this.ticketService.reserveTicket(ticketId, userId);
        this.cartDao.extendCartExpiry(userId);
        Long itemId = this.cartDao.add(ticket, userId);
        return this.cartDao.findById(itemId);
    }

    public List<Item> checkout(){
        Long userId = this.userService.getCurrentUserId();
        this.cartDao.extendCartExpiry(userId);
        return this.cartDao.get(userId);
        //TODO: might handle coupons later? maybe idk
//        Double cartTotal = this.pricingService.getCartTotal(items.stream().map(Item::getTicket).toList());
    }

    public List<Ticket> confirmBooking(){
        Long userId = this.userService.getCurrentUserId();
        this.cartDao.extendCartExpiry(userId);
        List<Item> items = this.cartDao.get(userId);
        List<Ticket> bookedTickets = this.ticketService.bookTickets(items.stream().map(Item::getTicket).toList());
        items.forEach(item -> {
            this.cartDao.remove(item.getId());
        });
        return bookedTickets;
    }

    public void createCartForNewUser(Long userId){
        this.cartDao.createCart(userId);
    }




}
