package com.abrenchev.jdbc.mapper;

import com.abrenchev.jdbc.mapper.annotations.Id;
import com.abrenchev.jdbc.mapper.exceptions.EntityClassMetadataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetadataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> targetClass;

    public EntityClassMetadataImpl(Class<T> clazz) {
        targetClass = clazz;
    }

    @Override
    public String getName() {
        return targetClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return targetClass.getDeclaredConstructor();
        } catch (NoSuchMethodException exception) {
            throw new EntityClassMetadataException("Could not find a constructor of class", exception);
        }
    }

    @Override
    public Field getIdField() {
        var idField = getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();

        return idField.orElse(getAllFields().get(0));
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(targetClass.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.equals(getIdField()))
                .collect(Collectors.toList());
    }
}
