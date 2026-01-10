package com.bookit.application.booking.user;

import com.bookit.application.security.user.UserService;
import com.bookit.application.booking.comms.Request;
import com.bookit.application.booking.comms.Response;
import org.springframework.stereotype.Component;

@Component("bookingUserLocalClient")
public class UserLocalClient implements UserClient {
  private UserService userService;

  public UserLocalClient(UserService userService) {
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
