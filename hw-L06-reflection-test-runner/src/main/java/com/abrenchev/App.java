package com.abrenchev;

import java.lang.reflect.InvocationTargetException;

public class App {
    public static void main(String... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TestRunner testRunner = new TestRunner();

        TestResults results = testRunner.run("com.abrenchev.SampleTest");

        System.out.println("DONE");
        System.out.println("Failed tests: " + results.getFailedCount());
        System.out.println("Total: " + results.getTotalCount());
    }
}
