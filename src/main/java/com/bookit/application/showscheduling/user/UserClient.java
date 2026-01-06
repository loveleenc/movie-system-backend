package com.bookit.application.showscheduling.user;

import com.bookit.application.services.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
