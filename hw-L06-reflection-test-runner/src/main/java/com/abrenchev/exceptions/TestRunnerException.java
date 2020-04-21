package com.abrenchev.exceptions;

public class TestRunnerException extends RuntimeException {
    public TestRunnerException(String message) {
        super(message);
    }

    public TestRunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
