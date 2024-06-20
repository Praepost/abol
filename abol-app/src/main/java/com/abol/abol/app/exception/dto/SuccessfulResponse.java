package com.abol.abol.app.exception.dto;

public class SuccessfulResponse implements Response {
    public ResponseStatus getStatus() {
        return ResponseStatus.SUCCESS;
    }
}
