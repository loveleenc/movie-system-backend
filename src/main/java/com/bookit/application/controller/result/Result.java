package com.bookit.application.controller.result;

import java.util.Objects;

public record Result(ResultStatus status, Object data, String reason) {
    public Result{
        Objects.requireNonNull(status);
    }
}
