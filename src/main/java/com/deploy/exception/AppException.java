package com.deploy.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AppException extends RuntimeException {

    public final Map<String, Object> validation = new HashMap<>();

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getStatusCode();

    public void addValidation(String fieldName, Object message) {
        validation.put(fieldName, message);
    }
}
