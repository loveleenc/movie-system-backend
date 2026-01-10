package com.bookit.application.services;

import com.bookit.application.booking.CartService;
import com.bookit.application.booking.TicketBookingException;
import com.bookit.application.booking.TicketService;
import com.bookit.application.booking.entity.Item;
import com.bookit.application.booking.entity.Seat;
import com.bookit.application.booking.entity.Ticket;
import com.bookit.application.booking.user.UserClient;
import com.bookit.application.booking.ResourceNotFoundException;
import com.bookit.application.booking.entity.Movie;
import com.bookit.application.booking.db.ICartDao;
import com.bookit.application.booking.entity.Show;
import com.bookit.application.booking.entity.ShowTimeSlot;
import com.bookit.application.booking.entity.Theatre;
import com.bookit.application.booking.entity.types.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class CartTest {
    private static Item item;
    private CartService cartService;
    private UserClient userClient;
    private TicketService ticketService;
    private ICartDao cartDao;

    @BeforeAll
    public static void beforeAll() {
        UUID showId = UUID.randomUUID();
        Theatre theatre = new Theatre("ABC Inox Theatre", "Antarctica", 1);
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        Movie movie = new
                Movie("Inception", 
                148, 
                "inception.png", 
                genre,
                LocalDate.of(2010, 7, 16),
                languages, 
                null);
        
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 20, 19, 30, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Show show = new Show(timeSlot, theatre, movie, languages.get(0), showId);
        Seat seat1 = new Seat("A1", "Bronze", 100L, 1L);
        Ticket ticket = new Ticket(show, seat1, TicketStatus.RESERVED, 200L);
        ticket.setOwnerId(45L);
        item = new Item(ticket, 2L);
    }

    @BeforeEach
    public void before() {
        this.cartDao = mock(ICartDao.class);
        this.userClient = mock(UserClient.class);
        this.ticketService = mock(TicketService.class);
        this.cartService = new CartService(cartDao, ticketService, userClient);
    }

    @Test
    public void test_whenUserTriesToRemoveATicketNotInTheirCart_thenResourceNotFoundExceptionThrown() {
        when(cartDao.findById(item.getId())).thenReturn(item);
        when(userClient.getCurrentUserId()).thenReturn(item.getTicket().getOwnerId() + 1);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.removeItem(item.getId()));
    }

    @Test
    public void test_whenRemoveItemFromCart_andItemNotInCart_thenThrowResourceNotFoundException() {
        when(cartDao.findById(item.getId())).thenThrow(IncorrectResultSizeDataAccessException.class);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> cartService.removeItem(item.getId()));
    }

    @Test
    public void test_whenAddItemToCart_andCartSizeIs10_thenThrowTicketBookingException() {
        when(userClient.getCurrentUserId()).thenReturn(item.getTicket().getOwnerId());
        when(cartDao.getItemCount(userClient.getCurrentUserId())).thenReturn(10);
        Assertions.assertThrows(TicketBookingException.class, () -> cartService.addItem(item.getTicket().getId()));
    }

    @Test
    public void test_whenAddItemToCart_thenTicketIsReserved_andItemAddedToCart() {
        when(userClient.getCurrentUserId()).thenReturn(item.getTicket().getOwnerId());
        when(cartDao.getItemCount(userClient.getCurrentUserId())).thenReturn(4);
        when(ticketService.reserveTicket(item.getTicket().getId(), userClient.getCurrentUserId())).thenReturn(item.getTicket());
        this.cartService.addItem(item.getTicket().getId());
        verify(ticketService, times(1)).reserveTicket(item.getTicket().getId(), userClient.getCurrentUserId());
        verify(cartDao, times(1)).extendCartExpiry(userClient.getCurrentUserId());
        verify(cartDao, times(1)).add(item.getTicket(), userClient.getCurrentUserId());
    }

    @Test
    public void test_duringCheckout_thenCartExpiryExtended() {
        when(userClient.getCurrentUserId()).thenReturn(item.getTicket().getOwnerId());
        this.cartService.checkout();
        verify(cartDao, times(1)).extendCartExpiry(userClient.getCurrentUserId());
    }

    @Test
    public void test_whenBookingTickets_thenCartExpiryExtended_andTicketsBooked_cartIsEmptied() {
        when(userClient.getCurrentUserId()).thenReturn(item.getTicket().getOwnerId());
        when(cartDao.get(userClient.getCurrentUserId())).thenReturn(List.of(item));
        when(ticketService.bookTickets(List.of(item.getTicket()))).thenReturn(List.of(item.getTicket()));
        List<Ticket> tickets = this.cartService.confirmBooking();
        verify(cartDao, times(1)).extendCartExpiry(userClient.getCurrentUserId());
        verify(ticketService, times(1)).bookTickets(List.of(item.getTicket()));
        verify(cartDao, times(1)).remove(item.getId());
        Assertions.assertEquals(1, tickets.size());
        Assertions.assertEquals(tickets.get(0).getId(), item.getTicket().getId());
    }


}
