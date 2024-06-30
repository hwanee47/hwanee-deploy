package com.deploy.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class AppBizException extends AppException {

    public AppBizException(String message) {
        super(message);
    }

    public AppBizException(String message, String fieldName, Object data) {
        super(message);
        addValidation(fieldName, data);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}
