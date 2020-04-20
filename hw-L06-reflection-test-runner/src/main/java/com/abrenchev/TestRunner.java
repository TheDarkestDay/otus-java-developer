package com.abrenchev;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestRunner {
    public static void invokeMethod(Method method, Object instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            System.err.println("Something went wrong with the test runner");
            exception.printStackTrace();
        }
    }

    public TestResults run(String fullClassName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> testClass = Class.forName(fullClassName);

        Method[] methods = testClass.getMethods();
        List<Method> beforeMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Before.class))
                .collect(Collectors.toList());

        List<Method> afterMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(After.class))
                .collect(Collectors.toList());

        List<Method> testCases = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());

        int totalTestsCount = testCases.size();
        int failedTestsCount = 0;

        for (Method testCase : testCases) {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            String testDescription  = testCase.getAnnotation(Test.class).description();

            beforeMethods.forEach(method -> invokeMethod(method, testInstance));

            try {
                testCase.invoke(testInstance);
                System.out.println(testDescription + " ...OK");
            } catch (Exception exception) {
                failedTestsCount++;
                System.err.println(testDescription + "...FAILED");
                exception.printStackTrace();
            }

            afterMethods.forEach(method -> invokeMethod(method, testInstance));
        }

        return new TestResults(totalTestsCount, failedTestsCount);
    }
}