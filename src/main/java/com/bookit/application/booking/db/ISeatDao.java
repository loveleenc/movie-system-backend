package com.bookit.application.booking.db;

import com.bookit.application.booking.entity.Seat;
import com.bookit.application.booking.entity.types.SeatCategory;

import java.util.List;
import java.util.Map;

public interface ISeatDao {
    List<Seat> getSeatPricesByTheatre(Integer theatreId);
    void create(List<Seat> seats, Integer theatreId);
    void addPrices(Map<SeatCategory, Long> seatCategoryAndPrices, Integer theatreId);

}
