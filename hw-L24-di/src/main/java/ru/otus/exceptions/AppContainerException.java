package ru.otus.exceptions;

public class AppContainerException extends RuntimeException {
    public AppContainerException(String message) {
        super(message);
    }

    public AppContainerException(String message, Exception cause) {
        super(message, cause);
    }
}
