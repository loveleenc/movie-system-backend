package com.bookit.booking.user;

import com.bookit.booking.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
