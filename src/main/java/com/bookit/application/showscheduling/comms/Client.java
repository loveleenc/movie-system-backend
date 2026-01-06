package com.bookit.application.showscheduling.comms;

public interface Client {
  void sendRequest(Request request);
  Object processResponse(Response response);
}
