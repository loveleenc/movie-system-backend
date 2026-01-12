package com.bookit.venue.theatre.db;

import com.bookit.venue.theatre.entity.Seat;
import com.bookit.venue.theatre.entity.SeatCategory;

import java.util.List;
import java.util.Map;

public interface ISeatDao {
    List<Seat> getSeatPricesByTheatre(Integer theatreId);
    void create(List<Seat> seats, Integer theatreId);
    void addPrices(Map<SeatCategory, Long> seatCategoryAndPrices, Integer theatreId);

}
