package com.bookit.application.persistence;

import com.bookit.application.entity.Seat;

import java.util.List;

public interface ISeatDao {
    List<Seat> getSeatPricesByTheatre(Integer theatreId);
    void create(List<Seat> seats, Integer theatreId);

}
