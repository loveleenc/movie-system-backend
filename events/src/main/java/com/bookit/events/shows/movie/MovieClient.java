package com.bookit.events.shows.movie;

import com.bookit.events.shows.ResourceNotFoundException;
import com.bookit.events.shows.comms.Client;
import com.bookit.events.shows.entity.Movie;

public interface MovieClient extends Client {
  Movie getMovieById(Long movieId) throws ResourceNotFoundException;
}
