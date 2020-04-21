package com.abrenchev;

import java.util.Map;

public class App {
    public static void main(String... args) {
        TestRunner testRunner = new TestRunner();

        TestResults results = testRunner.run("com.abrenchev.SampleTest");

        System.out.println("DONE");
        System.out.println("Failed tests: " + results.getFailedCount());
        System.out.println("Total: " + results.getTotalCount());

        Map<String, String> testErrors = results.getTestErrors();
        for (Map.Entry<String, String> entry : testErrors.entrySet()) {
            System.err.println(entry.getKey() + " failed because of: " + entry.getValue());
        }
    }
}
