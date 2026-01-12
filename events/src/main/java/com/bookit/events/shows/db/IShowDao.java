package com.bookit.events.shows.db;

import com.bookit.events.shows.entity.Show;
import com.bookit.events.shows.entity.ShowTimeSlot;

import java.util.List;

public interface IShowDao extends Crud<Show, String> {
    List<ShowTimeSlot> getBookedSlotsByTheatreId(Integer theatreId);
    List<Show> findShowsByMovie(Long movieId);
    List<Show> findShowsByTheatre(Integer theatreId, Long userId);
}
