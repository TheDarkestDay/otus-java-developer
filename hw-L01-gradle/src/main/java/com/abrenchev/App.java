package com.abrenchev;

import com.google.common.base.Optional;

public class App {
    public Optional<String> getGreeting() {
        return Optional.of("Hello, world!");
    }

    public static void main(String... args) {
        System.out.println(new App().getGreeting());
    }
}