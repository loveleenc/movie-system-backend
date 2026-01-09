package com.bookit.application.booking.comms;

public abstract class Request {
  private final Object payload;

  public Request(Object payload) {
    this.payload = payload;
  }

  public Object getPayload() {
    return payload;
  }
}
