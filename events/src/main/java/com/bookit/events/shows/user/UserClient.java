package com.bookit.events.shows.user;

import com.bookit.events.shows.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
