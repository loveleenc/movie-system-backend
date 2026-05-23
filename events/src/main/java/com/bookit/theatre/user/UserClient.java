package com.bookit.theatre.user;

import com.bookit.theatre.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
