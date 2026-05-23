package com.bookit.booking.user;

import com.bookit.security.user.UserService;
import com.bookit.booking.comms.Request;
import com.bookit.booking.comms.Response;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component("bookingUserLocalClient")
@ComponentScan(basePackages = {"com.bookit.security"})
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
