package com.bookit.application.security.user.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
