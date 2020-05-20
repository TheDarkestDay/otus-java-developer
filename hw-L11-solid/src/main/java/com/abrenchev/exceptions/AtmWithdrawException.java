package com.abrenchev.exceptions;

public class AtmWithdrawException extends RuntimeException {
    public AtmWithdrawException(int amount) {
        super("Insufficient funds to withdraw the given amount: " + amount);
    }
}
