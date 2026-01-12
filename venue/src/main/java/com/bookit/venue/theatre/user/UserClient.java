package com.bookit.venue.theatre.user;

import com.bookit.venue.theatre.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
