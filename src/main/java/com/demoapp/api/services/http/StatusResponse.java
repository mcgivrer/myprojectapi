package com.demoapp.api.services.http;

public enum StatusResponse {
    SUCCESS(200, "Object created"),
    UNKNOWN(404, "Object not found"),
    CREATED(401, "Object created"),
    ERROR(500, "error during processing");

    private final int errCode;
    private final String message;

    StatusResponse(int code, String message) {
        this.errCode = code;
        this.message = message;
    }
}
