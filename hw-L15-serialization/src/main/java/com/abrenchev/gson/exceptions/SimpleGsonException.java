package com.abrenchev.gson.exceptions;

public class SimpleGsonException extends RuntimeException {
    public SimpleGsonException(String message, Exception cause) {
        super(message, cause);
    }
}
