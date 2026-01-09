package com.bookit.application.booking.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
