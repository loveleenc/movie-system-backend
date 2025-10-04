package com.bookit.application.controller.result;

public enum ResultStatus {
    SUCCESS("success"),
    FAILURE("failure"),
    PENDING("pending");

    private final String code;

    ResultStatus(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return this.code;
    }
}
