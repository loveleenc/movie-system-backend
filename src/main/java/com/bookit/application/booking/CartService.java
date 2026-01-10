package com.bookit.application.booking;

import com.bookit.application.booking.user.UserClient;
import com.bookit.application.booking.entity.Item;
import com.bookit.application.booking.entity.Ticket;
import com.bookit.application.booking.db.ICartDao;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private static final Integer MAX_CART_CAPACITY = 10;
    private ICartDao cartDao;
    private TicketService ticketService;
    private UserClient userClient;

    public CartService(ICartDao cartDao, TicketService ticketService, UserClient userClient) {
        this.cartDao = cartDao;
        this.ticketService = ticketService;
        this.userClient = userClient;
    }

    public List<Item> getCart(){
        Long userId = this.userClient.getCurrentUserId();
        return this.cartDao.get(userId);
    }

    public void removeItem(Long itemId) throws ResourceNotFoundException {
        try{
            Item item = this.cartDao.findById(itemId);
            Long userId = this.userClient.getCurrentUserId();
            if(!item.getTicket().getOwnerId().equals(userId)){
                throw new ResourceNotFoundException("The requested item was not found");
            }
            this.cartDao.remove(itemId);
            this.cartDao.extendCartExpiry(userId);
            this.ticketService.releaseTicket(item.getTicket());
        }
        catch(IncorrectResultSizeDataAccessException e){
            throw new ResourceNotFoundException("The requested item was not found");
        }

    }

    public Item addItem(String ticketId){
        Long userId = this.userClient.getCurrentUserId();
        if(this.cartDao.getItemCount(userId).equals(MAX_CART_CAPACITY)){
            throw new TicketBookingException("Cannot add more than 10 items to the cart");
        }
        Ticket ticket = this.ticketService.reserveTicket(ticketId, userId);
        this.cartDao.extendCartExpiry(userId);
        Long itemId = this.cartDao.add(ticket, userId);
        return this.cartDao.findById(itemId);
    }

    public List<Item> checkout(){
        Long userId = this.userClient.getCurrentUserId();
        this.cartDao.extendCartExpiry(userId);
        return this.cartDao.get(userId);
        //TODO: might handle coupons later? maybe idk
//        Double cartTotal = this.pricingService.getCartTotal(items.stream().map(Item::getTicket).toList());
    }

    public List<Ticket> confirmBooking(){
        Long userId = this.userClient.getCurrentUserId();
        this.cartDao.extendCartExpiry(userId);
        List<Item> items = this.cartDao.get(userId);
        List<Ticket> bookedTickets = this.ticketService.bookTickets(items.stream().map(Item::getTicket).toList());
        items.forEach(item -> {
            this.cartDao.remove(item.getId());
        });
        return bookedTickets;
    }

    public void createCart(Long userId){
        this.cartDao.createCart(userId);
    }
}
