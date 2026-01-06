package com.bookit.application.showscheduling.user;

import com.bookit.application.showscheduling.comms.Client;

public interface UserClient extends Client {
  Long getCurrentUserId();
}
