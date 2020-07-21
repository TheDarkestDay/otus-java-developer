package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.AppContainerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object configInstance = createInstance(configClass);

        List<Method> factories = Arrays
                .stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        factories.forEach(factory -> {
            List<Class<?>> factoryArgTypes = Arrays.asList(factory.getParameterTypes());
            List<Object> factoryArgs = factoryArgTypes.stream()
                    .map(this::getAppComponent)
                    .collect(Collectors.toList());

            Object newDep = invokeFactory(configInstance, factory, factoryArgs);
            appComponents.add(newDep);

            String depName = factory.getAnnotation(AppComponent.class).name();
            appComponentsByName.put(depName, newDep);
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new AppContainerException("An exception occurred during AppConfig instantiation", exception);
        }
    }

    private Object invokeFactory(Object object, Method factory, List<Object> deps) {
        try {
            return factory.invoke(object, deps.toArray());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new AppContainerException("An exception occurred during factory execution", exception);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var result = appComponents.stream()
                .filter(resolvedDep -> componentClass.isInstance(resolvedDep))
                .findFirst()
                .orElse(null);

        if (result == null) {
            throw new AppContainerException("Could not find dependency for class: " + componentClass.getCanonicalName());
        }

        return (C) result;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
