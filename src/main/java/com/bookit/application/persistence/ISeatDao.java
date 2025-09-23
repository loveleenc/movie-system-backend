package com.bookit.application.persistence;

import com.bookit.application.entity.Seat;

import java.util.List;

public interface ISeatDao {
    List<Seat> getSeatPricesByTheatre(Long theatreId);
}
