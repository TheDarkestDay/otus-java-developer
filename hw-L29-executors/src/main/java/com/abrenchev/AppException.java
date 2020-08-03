package com.abrenchev;

public class AppException extends RuntimeException {
    public AppException(String message, Exception cause) {
        super(message, cause);
    }
}
