package com.bookit.application.services;

import com.bookit.application.entity.ShowTimeSlot;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class PricingService {

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
        return totalPrice + (0.1 * totalPrice);
    }

    public Long calculateTicketPrice(Long seatPrice, Long moviePrice, ShowTimeSlot timeSlot) {
        Long currentPrice = seatPrice + moviePrice;
        Double priceAfterTax = this.applyTax(currentPrice);
        DayOfWeek day = timeSlot.startTime().getDayOfWeek();
        if (this.showStartsBeforeNoon(timeSlot.startTime())) {
            priceAfterTax = priceAfterTax - (priceAfterTax * 0.05);
        }
        else if(this.showStartsAfterFourPm(timeSlot.startTime())){
            priceAfterTax = priceAfterTax + (priceAfterTax * 0.05);
        }
        if(isWeekend(day)){
            priceAfterTax = priceAfterTax + (priceAfterTax * 0.05);
        }
        return Math.round(priceAfterTax);
    }
}
