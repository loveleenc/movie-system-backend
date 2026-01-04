package com.bookit.application.theatremanagement.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
