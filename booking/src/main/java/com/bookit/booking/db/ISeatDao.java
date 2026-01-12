package com.bookit.booking.db;

import com.bookit.booking.entity.Seat;
import com.bookit.booking.entity.types.SeatCategory;

import java.util.List;
import java.util.Map;

public interface ISeatDao {
    List<Seat> getSeatPricesByTheatre(Integer theatreId);
    void create(List<Seat> seats, Integer theatreId);
    void addPrices(Map<SeatCategory, Long> seatCategoryAndPrices, Integer theatreId);

}
