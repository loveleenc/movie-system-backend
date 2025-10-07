package com.bookit.application.services;

import com.bookit.application.entity.*;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITicketDao;
import com.bookit.application.types.TicketStatus;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class TicketTest {
    private List<Ticket> tickets;
    private List<Seat> seats;
    private static Show show;
    private ISeatDao seatDao;
    private ITicketDao ticketDao;
    private TicketService ticketService;
    private UserService userService;
    private static PricingService pricingService = new PricingService();


    @BeforeAll
    public static void beforeAll() {
        UUID showId = UUID.randomUUID();
        Theatre theatre = new Theatre("ABC Inox Theatre", "Antarctica", 1);
        List<String> genre = Arrays.asList("Action", "Adventure");
        List<String> languages = Arrays.asList("English", "Tamil", "Hindi");
        Movie movie = new MovieBuilder()
                .setName("Inception")
                .setPoster("inception.png")
                .setDuration(148)
                .setGenreList(genre)
                .setLanguages(languages)
                .setReleaseDate(LocalDate.of(2010, 7, 16))
                .build();
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 20, 19, 30, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        show = new Show(timeSlot, theatre, movie, languages.get(0), showId);


    }

    @BeforeEach
    public void before() {
        this.seatDao = mock(ISeatDao.class);
        this.ticketDao = mock(ITicketDao.class);
        this.userService = mock(UserService.class);
        this.ticketService = new TicketService(seatDao, pricingService, ticketDao, userService);
        Seat seat1 = new Seat("A1", "Bronze", 100L, 1L);
        Seat seat2 = new Seat("B1", "Silver", 250L, 11L);
        Seat seat3 = new Seat("C2", "Gold", 300L, 25L);
        Seat seat4 = new Seat("C9", "Gold", 300L, 28L);
        Ticket ticket1 = new Ticket(show, seat1, TicketStatus.AVAILABLE, 243L);
        Ticket ticket2 = new Ticket(show, seat2, TicketStatus.BOOKED, 424L);
        Ticket ticket3 = new Ticket(show, seat3, TicketStatus.BLOCKED, 485L);
        Ticket ticket4 = new Ticket(show, seat4, TicketStatus.AVAILABLE, 485L);
        this.tickets = Arrays.asList(ticket1, ticket2, ticket3, ticket4);
        this.seats = Arrays.asList(seat1, seat2, seat3, seat4);
    }

    @Test
    public void test_whenUpdatingTickets_andNoTicketsExistForShow_thenResourceNotFoundExceptionThrown() {
        String showId = UUID.randomUUID().toString();
        String status = TicketStatus.AVAILABLE.code();
        when(ticketService.getTicketsByShow(showId)).thenReturn(new ArrayList<>());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> ticketService.updateTicketStatusForShow(showId, status, false));
    }

    @Test
    public void test_whenUpdatingAllTicketsForShowWithInvalidTicketStatusConditions_andStatusChecksAreEnabled() {
        when(ticketService.getTicketsByShow(show.getId().toString())).thenReturn(tickets);
        Boolean overrideStatusChangeValidation = false;
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), "RANDOMSTATUS11", overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.USED.code());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BLOCKED.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.CANCELLED.code());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BLOCKED.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.BOOKED.code());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BLOCKED.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.AVAILABLE.code());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.CANCELLED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.USED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BOOKED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.BLOCKED.code());
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.CANCELLED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.USED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BOOKED.code(), overrideStatusChangeValidation));
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.BLOCKED.code(), overrideStatusChangeValidation));
        tickets.forEach(ticket -> ticket.setStatus(TicketStatus.BLOCKED.code()));
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
    }

    @Test
    public void test_whenUpdatingAllTicketsForShowWithInvalidTicketStatusConditions_andStatusChecksAreDisabled() {
        when(ticketService.getTicketsByShow(show.getId().toString())).thenReturn(tickets);
        Boolean overrideStatusChangeValidation = true;
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> ticketService.updateTicketStatusForShow(show.getId().toString(), "RANDOMSTATUS11", overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.USED.code());
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.CANCELLED.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.BOOKED.code());
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.USED.code(), overrideStatusChangeValidation));

        tickets.get(0).setStatus(TicketStatus.BLOCKED.code());
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.CANCELLED.code(), overrideStatusChangeValidation));

        tickets.forEach(ticket -> ticket.setStatus(TicketStatus.BLOCKED.code()));
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.AVAILABLE.code(), overrideStatusChangeValidation));
        Assertions.assertDoesNotThrow(() -> ticketService.updateTicketStatusForShow(show.getId().toString(), TicketStatus.CANCELLED.code(), overrideStatusChangeValidation));
    }

    @Test
    public void test_ticketCreationForShowFailsWhenMoviePriceIsZeroOrLess() {
        Long moviePriceZero = 0L;
        Long moviePriceNegative = -200L;
        String status = TicketStatus.AVAILABLE.code();
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePriceZero, show, status));
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePriceNegative, show, status));
    }

    @Test
    public void test_ticketCreationForShowFailsWithTicketStatuses_used_booked_cancelled() {
        Long moviePrice = 100L;
        String usedStatus = TicketStatus.USED.code();
        String bookedStatus = TicketStatus.BOOKED.code();
        String cancelledStatus = TicketStatus.CANCELLED.code();
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePrice, show, usedStatus));
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePrice, show, bookedStatus));
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePrice, show, cancelledStatus));
    }

    @Test
    public void test_ticketCreationForShowFailsWhenNoSeatsAreFoundForTheatre() {
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(seatDao.getSeatPricesByTheatre(show.getTheatreId())).thenReturn(new ArrayList<>());
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePrice, show, status));
    }

    @Test

    public void test_ticketCreationForShowFailsWhenTicketsAlreadyExist() {
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(seatDao.getSeatPricesByTheatre(show.getTheatreId())).thenReturn(seats);
        when(ticketDao.findTicketsByShow(show.getId().toString())).thenReturn(tickets);
        Assertions.assertThrows(ResourceCreationException.class, () -> ticketService.createTickets(moviePrice, show, status));
    }

    @Test
    public void test_createAvailableTicketsForAShowSuccessfully() {
        Long moviePrice = 100L;
        String availableStatus = TicketStatus.AVAILABLE.code();
        when(seatDao.getSeatPricesByTheatre(show.getTheatreId())).thenReturn(seats);
        when(ticketDao.findTicketsByShow(show.getId().toString())).thenReturn(new ArrayList<>());
        ticketService.createTickets(moviePrice, show, availableStatus);
        ArgumentCaptor<List<Ticket>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(ticketDao).createTickets(argumentCaptor.capture());
        List<Ticket> createdTickets = argumentCaptor.getValue();

        tickets.forEach(ticket -> ticket.setStatus(availableStatus));
        for (int i = 0; i < createdTickets.size(); i++) {
            Ticket actualTicket = createdTickets.get(i);
            Ticket expectedTicket = tickets.get(i);
            Assertions.assertEquals(expectedTicket.getPrice(), actualTicket.getPrice());
            Assertions.assertEquals(expectedTicket.getStatus(), actualTicket.getStatus());
            Assertions.assertEquals(expectedTicket.getSeat().getSeatNumber(), actualTicket.getSeat().getSeatNumber());
            Assertions.assertEquals(expectedTicket.getShow().getStartTime().toString(), actualTicket.getShow().getStartTime().toString());
            Assertions.assertEquals(expectedTicket.getShow().getEndTime().toString(), actualTicket.getShow().getEndTime().toString());
            Assertions.assertEquals(expectedTicket.getShow().getTheatre().getName(), actualTicket.getShow().getTheatre().getName());
            Assertions.assertEquals(expectedTicket.getSeat().getSeatType(), actualTicket.getSeat().getSeatType());
        }
    }

    @Test
    public void test_createBlockedTicketsForAShowSuccessfully() {
        Long moviePrice = 100L;
        String statusBlocked = TicketStatus.BLOCKED.code();
        when(seatDao.getSeatPricesByTheatre(show.getTheatreId())).thenReturn(seats);
        when(ticketDao.findTicketsByShow(show.getId().toString())).thenReturn(new ArrayList<>());
        ticketService.createTickets(moviePrice, show, statusBlocked);
        ArgumentCaptor<List<Ticket>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(ticketDao).createTickets(argumentCaptor.capture());
        List<Ticket> createdTickets = argumentCaptor.getValue();

        tickets.forEach(ticket -> ticket.setStatus(statusBlocked));
        for (int i = 0; i < createdTickets.size(); i++) {
            Ticket actualTicket = createdTickets.get(i);
            Ticket expectedTicket = tickets.get(i);
            Assertions.assertEquals(expectedTicket.getPrice(), actualTicket.getPrice());
            Assertions.assertEquals(expectedTicket.getStatus(), actualTicket.getStatus());
            Assertions.assertEquals(expectedTicket.getSeat().getSeatNumber(), actualTicket.getSeat().getSeatNumber());
            Assertions.assertEquals(expectedTicket.getShow().getStartTime().toString(), actualTicket.getShow().getStartTime().toString());
            Assertions.assertEquals(expectedTicket.getShow().getEndTime().toString(), actualTicket.getShow().getEndTime().toString());
            Assertions.assertEquals(expectedTicket.getShow().getTheatre().getName(), actualTicket.getShow().getTheatre().getName());
            Assertions.assertEquals(expectedTicket.getSeat().getSeatType(), actualTicket.getSeat().getSeatType());
        }
    }


    //CREATE TICKETS-
    //create tickets with price 0 or -400 - DONE
    //create tickets with invalid status -- DONE
    //create tickets with null show - null id or null theatre id
    //create tickets with status used, booked, cancelled -- DONE
    //if no seats are present in theatre -- DONE
    //if tickets already exist for the show -- DONE
    //https://stackoverflow.com/questions/32011881/how-do-i-test-the-values-passed-as-a-parameter-to-a-method-inside-another-method
    //https://stackoverflow.com/questions/3555472/mockito-verify-method-arguments

    //UPDATE TICKET STATUSES FOR SHOW
    //when no tickets exist for a show - done

    //1.override = false --- DONE
    //provide invalid status string
    //current status is used
    //current status is booked and new status is available/used
    //current status is blocked and new status is used


    //override = true --- DONE
    //provide invalid status string
    //current status is used
    //current status is booked and new status is available/used
    //current status is blocked and new status is used


}
