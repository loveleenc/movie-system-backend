package com.bookit.booking;

import com.bookit.booking.entity.ShowTimeSlot;
import com.bookit.booking.entity.Ticket;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PricingService {
    private static final double TAX = 0.1;
    private Boolean isWeekend(DayOfWeek day) {
        return day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY);
    }

    private Boolean showStartsBeforeNoon(LocalDateTime showStartTime) {
        return showStartTime.isBefore(LocalDateTime.of(showStartTime.toLocalDate(), LocalTime.of(12, 0, 0)));
    }

    private Boolean showStartsAfterFourPm(LocalDateTime showStartTime) {
        return showStartTime.isAfter(LocalDateTime.of(showStartTime.toLocalDate(), LocalTime.of(16, 0, 0))) ||
                showStartTime.isEqual(LocalDateTime.of(showStartTime.toLocalDate(), LocalTime.of(16, 0, 0)));
    }

    private Double applyTax(Long totalPrice) {
        return totalPrice + (PricingService.TAX * totalPrice);
    }

    public Long calculateTicketPrice(Long seatPrice, Long moviePrice, ShowTimeSlot timeSlot) {
        Double currentPrice = (double)seatPrice + moviePrice;
        DayOfWeek day = timeSlot.startTime().getDayOfWeek();
        if (this.showStartsBeforeNoon(timeSlot.startTime())) {
            currentPrice = currentPrice - (currentPrice * 0.05);
        }
        else if(this.showStartsAfterFourPm(timeSlot.startTime())){
            currentPrice = currentPrice + (currentPrice * 0.05);
        }
        if(isWeekend(day)){
            currentPrice = currentPrice + (currentPrice * 0.05);
        }
        return Math.round(currentPrice);
    }

    public Double getCartTotal(List<Ticket> tickets) throws NoSuchElementException {
        Optional<Long> totalBeforeTax = tickets.stream().map(Ticket::getPrice).reduce(Long::sum);
        totalBeforeTax.orElseThrow();
        return this.applyTax(totalBeforeTax.get());
    }


}
