package com.abrenchev.core.dao;

public class AccountDaoException extends RuntimeException {
    public AccountDaoException(String message, Exception cause) {
        super(message, cause);
    }
}
