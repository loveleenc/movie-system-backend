package com.bookit.application.theatremanagement.user;

import com.bookit.application.theatremanagement.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
