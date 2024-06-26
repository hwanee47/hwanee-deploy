package com.deploy.exception;

import java.util.HashMap;
import java.util.Map;

public class RemoteException extends AppException {

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getStatusCode() {
        return "500";
    }
}
