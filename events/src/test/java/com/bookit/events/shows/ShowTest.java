package com.bookit.events.shows;

import com.bookit.events.shows.entity.Movie;
import com.bookit.events.shows.booking.BookingClient;
import com.bookit.events.shows.db.IShowDao;
import com.bookit.events.shows.entity.Show;
import com.bookit.events.shows.entity.ShowTimeSlot;
import com.bookit.events.shows.movie.MovieClient;
import com.bookit.events.shows.user.UserClient;
import com.bookit.events.shows.entity.Theatre;
import com.bookit.events.shows.entity.types.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ShowTest {
    static Movie movie;
    static Theatre theatre;
    static List<String> genre;
    static List<String> languages;
    static ShowService showService;
    static IShowDao showDao;
    static MovieClient movieClient;
    static BookingClient bookingClient;
    static UserClient userClient;
    private Show show;

    @BeforeAll
    public static void beforeAll(){
        theatre = new Theatre("ABC Inox Theatre", "Antarctica", 1);
        genre = Arrays.asList("Action", "Adventure");
        languages = Arrays.asList("English", "Tamil", "Hindi");
         movie = new Movie("Inception",
                148,
                "inception.png",
                genre,
                LocalDate.of(2010, 7, 16),
                languages,
                2L);
        showDao = mock(IShowDao.class);
        userClient = mock(UserClient.class);
        movieClient = mock(MovieClient.class);
        bookingClient = mock(BookingClient.class);
        showService = new ShowService(showDao, userClient, movieClient, bookingClient);
    }

    @BeforeEach
    public void before() {
        Theatre theatre = new Theatre("ABC Inox Theatre", "Antarctica", 1);
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 20, 19, 30, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        this.show = new Show(timeSlot, theatre, movie, languages.get(0), null);
    }
    //requirements-
    //movie
    //show timeslots (start, ends)
    //theatre

    @Test
    public void test_showCreationFailsWhenShowLanguageIsDifferentFromAvailableMovieLanguages(){
        this.show.setLanguage("Bengali");
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);
        Assertions.assertThrows(ResourceCreationException.class, () -> showService.createShowAndTickets(this.show, moviePrice, status));
    }

    @Test
    public void test_showCreationFailsWhenShowDateIsBeforeMovieReleaseDate(){
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        LocalDate dateBeforeMovieRelease = movie.getReleaseDate().minusDays(1);
        LocalDateTime startTime = dateBeforeMovieRelease.atTime(17, 0, 0);
        LocalDateTime endTime = dateBeforeMovieRelease.atTime(17, 0, 0).plusMinutes(movie.getDuration());
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        this.show.setTimeSlot(timeSlot);
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);
        Assertions.assertThrows(ResourceCreationException.class, () ->showService.createShowAndTickets(this.show, moviePrice, status));
    }

    @Test
    public void test_showCreationFailsWhenShowDurationIsLessThanLengthOfTheMovie(){
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        LocalDateTime endTime = show.getStartTime().plusMinutes(movie.getDuration()).minusMinutes(1);
        ShowTimeSlot timeSlot = new ShowTimeSlot(show.getStartTime(), endTime);
        this.show.setTimeSlot(timeSlot);
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);
        Assertions.assertThrows(ResourceCreationException.class, () ->showService.createShowAndTickets(this.show, moviePrice, status));
    }

    @Test
    public void test_showCreationFailsWhenShowTimeslotOverlapsWithExistingShowTimeslot(){
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);

        ShowTimeSlot timeSlotOverlapWithStartTime = new ShowTimeSlot(show.getStartTime().minusMinutes(30), show.getStartTime().plusMinutes(40));
        when(showDao.getBookedSlotsByTheatreId(theatre.getId())).thenReturn(List.of(timeSlotOverlapWithStartTime));
        Assertions.assertThrows(ResourceCreationException.class, () ->showService.createShowAndTickets(this.show, moviePrice, status));

        ShowTimeSlot timeSlotOverlapWithEndTime = new ShowTimeSlot(show.getEndTime().minusMinutes(10), show.getEndTime().plusMinutes(60));
        when(showDao.getBookedSlotsByTheatreId(theatre.getId())).thenReturn(List.of(timeSlotOverlapWithEndTime));
        Assertions.assertThrows(ResourceCreationException.class, () ->showService.createShowAndTickets(this.show, moviePrice, status));

        when(showDao.getBookedSlotsByTheatreId(theatre.getId())).thenReturn(List.of(show.getTimeSlot()));
        Assertions.assertThrows(ResourceCreationException.class, () ->showService.createShowAndTickets(this.show, moviePrice, status));
    }

    @Test
    public void test_createShowSuccessfully(){
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);

        LocalDateTime startTime = show.getEndTime().plusMinutes(0);
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());
        ShowTimeSlot timeSlotAfterShowTimeSlot = new ShowTimeSlot(startTime, endTime);
        when(showDao.getBookedSlotsByTheatreId(theatre.getId())).thenReturn(List.of(timeSlotAfterShowTimeSlot));
        UUID showId = UUID.randomUUID();

        Show showReturnedByDao = new Show(this.show.getTimeSlot(),
                this.show.getTheatre(),
                this.show.getMovie(),
                this.show.getLanguage(),
                showId);

        when(showDao.create(this.show)).thenReturn(showId.toString());
        when(showDao.findById(showId.toString())).thenReturn(showReturnedByDao);
        Show returnedShow = showService.createShowAndTickets(this.show, moviePrice, status);

        Assertions.assertEquals(this.show.getTheatre().getName(), returnedShow.getTheatre().getName());
        Assertions.assertEquals(this.show.getLanguage(), returnedShow.getLanguage());
        Assertions.assertEquals(this.show.getMovie().getId(), returnedShow.getMovie().getId());
        Assertions.assertEquals(this.show.getStartTime(), returnedShow.getStartTime());
        Assertions.assertEquals(this.show.getEndTime(), returnedShow.getEndTime());
        Assertions.assertEquals(this.show.getTheatreId(), returnedShow.getTheatreId());
    }

    @Test
    public void test_creatingShowFailsWhenMoviePriceOrTicketStatusIsNull(){
        Long moviePrice = 100L;
        String status = TicketStatus.AVAILABLE.code();
        when(movieClient.getMovieById(movie.getId())).thenReturn(movie);
        when(showDao.getBookedSlotsByTheatreId(theatre.getId())).thenReturn(List.of());
        UUID showId = UUID.randomUUID();

        Show showReturnedByDao = new Show(this.show.getTimeSlot(),
                this.show.getTheatre(),
                this.show.getMovie(),
                this.show.getLanguage(),
                showId);

        when(showDao.create(this.show)).thenReturn(showId.toString());
        when(showDao.findById(showId.toString())).thenReturn(showReturnedByDao);
        Assertions.assertThrows(NullPointerException.class, () -> showService.createShowAndTickets(this.show, null, status));
        Assertions.assertThrows(NullPointerException.class, () -> showService.createShowAndTickets(this.show, moviePrice, null));
    }
}
