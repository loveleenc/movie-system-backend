package com.bookit.application.services;

import com.bookit.application.entity.ShowTimeSlot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class PricingTest {
    private PricingService pricingService;

    public PricingTest(){
        this.pricingService = new PricingService();
    }

    @Test
    public void test_verifyTicketPrice_whenSaturday_AndEveningShow(){
        Long seatPrice = 500L;
        Long moviePrice = 100L;
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 20, 17, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 20, 19, 0, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(662L, ticketPrice);
    }

    @Test
    public void test_verifyTicketPrice_whenSunday_AndAfternoonShow(){
        Long seatPrice = 250L;
        Long moviePrice = 400L;
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 21, 15, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 21, 18, 0, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(683L, ticketPrice);
    }

    @Test
    public void test_verifyTicketPrice_whenSaturday_AndMorningShow(){
        Long seatPrice = 100L;
        Long moviePrice = 1000L;
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 21, 9, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 21, 10, 0, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(1097L, ticketPrice);
    }


    @Test
    public void test_verifyTicketPrice_whenMonday_And4PmShow(){
        Long seatPrice = 650L;
        Long moviePrice = 590L;
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 29, 16, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 29, 17, 0, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(1302L, ticketPrice);
    }

    @Test
    public void test_verifyTicketPrice_whenWednesday_AndAfternoonShow(){
        Long seatPrice = 300L;
        Long moviePrice = 350L;
        LocalDateTime startTime = LocalDateTime.of(2025, 10, 1, 15, 30, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 10, 1, 18, 0, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(650L, ticketPrice);
    }

    @Test
    public void test_verifyTicketPrice_whenFriday_AndMorningShow(){
        Long seatPrice = 300L;
        Long moviePrice = 350L;
        LocalDateTime startTime = LocalDateTime.of(2025, 9, 5, 9, 30, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 9, 5, 12, 30, 0);
        ShowTimeSlot timeSlot = new ShowTimeSlot(startTime, endTime);
        Long ticketPrice = this.pricingService.calculateTicketPrice(seatPrice, moviePrice, timeSlot);
        Assertions.assertEquals(618L, ticketPrice);
    }
}
