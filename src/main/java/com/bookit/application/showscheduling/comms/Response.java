package com.bookit.application.showscheduling.comms;

public abstract class Response {
  private final Object ResponseData;

  public Response(Object responseData) {
    ResponseData = responseData;
  }

  public Object getResponseData() {
    return ResponseData;
  }
}
