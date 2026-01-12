package com.bookit.booking.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
