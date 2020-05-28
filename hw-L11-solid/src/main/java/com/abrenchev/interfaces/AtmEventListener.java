package com.abrenchev.interfaces;

@FunctionalInterface
public interface AtmEventListener {
    void handle(AtmCommand command);
}
