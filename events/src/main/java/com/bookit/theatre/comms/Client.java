package com.bookit.theatre.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
