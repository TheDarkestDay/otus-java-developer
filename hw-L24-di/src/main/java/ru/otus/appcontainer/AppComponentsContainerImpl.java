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
    private final Object configInstance;

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        configInstance = createInstance(initialConfigClass);
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> factories = Arrays
                .stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted((factoryA, factoryB) -> {
                    int orderA = factoryA.getAnnotation(AppComponent.class).order();
                    int orderB = factoryB.getAnnotation(AppComponent.class).order();

                    return orderA - orderB;
                })
                .collect(Collectors.toList());

        factories.forEach(factory -> {
            List<Class<?>> factoryArgTypes = Arrays.asList(factory.getParameterTypes());
            List<Object> factoryArgs = factoryArgTypes.stream()
                    .map(factoryArgType -> {
                        Object factoryArg = appComponents.get(0);
                        for (Object resolvedDep : appComponents) {
                            if (factoryArgType.isInstance(resolvedDep)) {
                                factoryArg = resolvedDep;
                                break;
                            }
                        }

                        return factoryArg;
                    })
                    .collect(Collectors.toList());

            Object newDep = invokeFactory(factory, factoryArgs);
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

    private Object invokeFactory(Method factory, List<Object> deps) {
        try {
            return factory.invoke(configInstance, deps.toArray());
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new AppContainerException("An exception occurred during factory execution", exception);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object result = appComponents.get(0);
        for (Object resolvedDep : appComponents) {
            if (componentClass.isInstance(resolvedDep)) {
                result = resolvedDep;
            }
        }

        return (C) result;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
