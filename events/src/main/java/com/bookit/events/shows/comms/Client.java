package com.bookit.events.shows.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
