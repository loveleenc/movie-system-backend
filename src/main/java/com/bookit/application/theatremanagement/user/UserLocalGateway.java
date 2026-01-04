package com.bookit.application.theatremanagement.user;

import com.bookit.application.security.user.UserService;
import com.bookit.application.services.comms.Request;
import com.bookit.application.services.comms.Response;
import org.springframework.stereotype.Component;

@Component
public class UserLocalGateway implements UserGateway {
  private UserService userService;

  public UserLocalGateway(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Long getCurrentUserId() {
    return this.userService.getCurrentUserId();
  }

  @Override
  public void sendRequest(Request request) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Object processResponse(Response response) {
    throw new RuntimeException("Not implemented yet");
  }
}
