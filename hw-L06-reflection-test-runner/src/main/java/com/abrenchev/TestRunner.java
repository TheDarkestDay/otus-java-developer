package com.abrenchev;

import com.abrenchev.annotations.After;
import com.abrenchev.annotations.Before;
import com.abrenchev.annotations.Test;
import com.abrenchev.exceptions.TestRunnerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestRunner {
    private Class<?> getClassByName(String fullClassName) {
        try {
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException exception) {
            throw new TestRunnerException("Could not find class: " + fullClassName, exception);
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            throw new TestRunnerException("Provided class has no callable constructor", exception);
        } catch (InstantiationException exception) {
            throw new TestRunnerException("Abstract class or interface was provided instead of regular class", exception);
        } catch (InvocationTargetException exception) {
            throw new TestRunnerException("An exception occurred inside class constructor", exception);
        }
    }

    private void invokeMethod(Method method, Object instance) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException exception) {
            throw new TestRunnerException("Annotated method is not public", exception);
        } catch (InvocationTargetException exception) {
            throw new TestRunnerException("An exception thrown by annotated method", exception);
        }
    }

    public TestResults run(String fullClassName) {
        Class<?> testClass = getClassByName(fullClassName);

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

        Map<String, String> testErrors = new HashMap<>();

        for (Method testCase : testCases) {
            Object testInstance = createInstance(testClass);
            String testDescription  = testCase.getAnnotation(Test.class).description();

            beforeMethods.forEach(method -> invokeMethod(method, testInstance));

            try {
                testCase.invoke(testInstance);
                System.out.println(testDescription + " ...OK");
            } catch (Exception exception) {
                failedTestsCount++;
                System.err.println(testDescription + " ...FAILED");
                testErrors.put(testDescription, exception.getCause().getMessage());
            }

            afterMethods.forEach(method -> invokeMethod(method, testInstance));
        }

        return new TestResults(totalTestsCount, failedTestsCount, testErrors);
    }
}