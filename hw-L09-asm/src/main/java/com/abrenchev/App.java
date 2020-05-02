package com.abrenchev;

public class App {
    public static void main(String... args) {
        SampleClass instance = new SampleClass();

        instance.doSomething("Jack", 1, 2.5);
        instance.noLog("Bob");
    }
}