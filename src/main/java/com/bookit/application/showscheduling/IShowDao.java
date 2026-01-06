package com.bookit.application.showscheduling;

import com.bookit.application.common.persistence.Crud;
import com.bookit.application.showscheduling.entity.Show;
import com.bookit.application.showscheduling.entity.ShowTimeSlot;

import java.util.List;

public interface IShowDao extends Crud<Show, String> {
    List<ShowTimeSlot> getBookedSlotsByTheatreId(Integer theatreId);
    List<Show> findShowsByMovie(Long movieId);
    List<Show> findShowsByTheatre(Integer theatreId, Long userId);
}
