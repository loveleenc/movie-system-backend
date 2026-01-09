package com.bookit.application.booking.user;

import com.bookit.application.booking.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
