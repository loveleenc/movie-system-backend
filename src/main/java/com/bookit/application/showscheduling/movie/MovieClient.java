package com.bookit.application.showscheduling.movie;

import com.bookit.application.common.ResourceNotFoundException;
import com.bookit.application.showscheduling.comms.Client;
import com.bookit.application.showscheduling.entity.Movie;

public interface MovieClient extends Client {
  Movie getMovieById(Long movieId) throws ResourceNotFoundException;
}
