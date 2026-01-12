package com.bookit.events.shows.user;

import com.bookit.security.user.UserService;
import com.bookit.events.shows.comms.Request;
import com.bookit.events.shows.comms.Response;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.ComponentScan;

@Component("showsUserLocalClient")
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
