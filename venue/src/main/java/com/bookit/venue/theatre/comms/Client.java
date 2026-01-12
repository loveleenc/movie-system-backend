package com.bookit.venue.theatre.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
