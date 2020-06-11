package com.abrenchev.jdbc.mapper.exceptions;

public class JdbcMapperException extends RuntimeException {
    public JdbcMapperException(String message, Exception cause) {
        super(message, cause);
    }
}
