package com.bookit.application.theatremanagement.user;

import com.bookit.application.services.comms.Client;

public interface UserGateway extends Client {
  Long getCurrentUserId();
}
