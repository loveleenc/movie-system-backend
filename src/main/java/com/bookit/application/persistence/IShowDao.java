package com.bookit.application.persistence;

import com.bookit.application.entity.Show;
import com.bookit.application.entity.ShowTimeSlot;

import java.util.List;

public interface IShowDao extends Crud<Show, String>{
    List<ShowTimeSlot> getBookedSlotsByTheatreId(Long theatreId);
    List<Show> findShowsByMovie(Long movieId);
    List<Show> findShowsByTheatre(Integer theatreId);
}
