package com.abrenchev.gson;

import com.abrenchev.gson.exceptions.SimpleGsonException;

import javax.json.*;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class SimpleGson {
    private final Class<?>[] primitiveTypes = new Class<?>[]{
            Character.class,
            Integer.class,
            Boolean.class,
            Double.class,
            Byte.class,
            Short.class,
            Float.class,
            Long.class,
            String.class,
    };

    public String toJson(Object obj) {
        if (obj == null) {
            return JsonValue.NULL.toString();
        }

        for (Class<?> clazz : primitiveTypes) {
            if (clazz.isInstance(obj)) {
                return convertPrimitiveValueToJson(obj, clazz).toString();
            }
        }

        if (obj.getClass().isArray()) {
            return convertArrayToJson(obj).toString();
        }

        if (Collection.class.isAssignableFrom(obj.getClass())) {
            var collection = (Collection) obj;
            var arrayFromCollection = collection.toArray(new Object[0]);
            return convertArrayToJson(arrayFromCollection).toString();
        }

        var jsonBuilder = Json.createObjectBuilder();

        for (Field objField : obj.getClass().getDeclaredFields()) {
            if (!objField.getName().equals("this$0")) {
                var fieldValue = getFieldValue(obj, objField);
                jsonBuilder.add(
                        objField.getName(),
                        objField.getType().isArray()
                                ? convertArrayToJson(fieldValue)
                                : convertPrimitiveValueToJson(fieldValue, fieldValue.getClass())
                );
            }
        }

        return jsonBuilder.build().toString();
    }

    public Object fromJson(String json, Class<?> clazz) {
        var jsonReader = Json.createReader(new StringReader(json));
        var jsonObject = jsonReader.readObject();

        Object result = createInstance(clazz);

        for (Field clazzField : clazz.getDeclaredFields()) {
            setObjectField(result, clazzField, getJsonFieldValue(jsonObject, clazzField));
        }

        return result;
    }

    private JsonArray convertArrayToJson(Object obj) {
        var arrayBuilder = Json.createArrayBuilder();

        for (int i = 0; i < Array.getLength(obj); i++) {
            var arrayItem = Array.get(obj, i);
            arrayBuilder.add(convertPrimitiveValueToJson(arrayItem, arrayItem.getClass()));
        }

        return arrayBuilder.build();
    }

    private JsonValue convertPrimitiveValueToJson(Object obj, Class<?> clazz) {
        if (clazz.equals(Integer.class)) {
            return Json.createValue((int) obj);
        }

        if (clazz.equals(Short.class)) {
            return Json.createValue((short) obj);
        }

        if (clazz.equals(Float.class)) {
            return Json.createValue((float) obj);
        }

        if (clazz.equals(Byte.class)) {
            return Json.createValue((byte) obj);
        }

        if (clazz.equals(Long.class)) {
            return Json.createValue((long) obj);
        }

        if (clazz.equals(Double.class)) {
            return Json.createValue((double) obj);
        }

        if (clazz.equals(String.class)) {
            return Json.createValue((String) obj);
        }

        if (clazz.equals(Character.class)) {
            return Json.createValue(Character.toString((char) obj));
        }

        if (clazz.equals(Boolean.class)) {
            var boolValue = (boolean) obj;
            return boolValue ? JsonValue.TRUE : JsonValue.FALSE;
        }

        return JsonValue.NULL;
    }

    private void setObjectField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not set field value of " + field.getName(), exception);
        }
    }

    private Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could't get a field content", exception);
        }
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new SimpleGsonException("Could't construct the new instance", exception);
        }
    }

    private Object getJsonFieldValue(JsonObject obj, Field field) {
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();

        if (fieldType.equals(Integer.TYPE)) {
            return obj.getInt(fieldName);
        }

        if (fieldType.equals(Boolean.TYPE)) {
            return obj.getBoolean(fieldName);
        }

        if (fieldType.equals(String.class)) {
            return obj.getString(fieldName);
        }

        if (fieldType.isArray()) {
            var jsonArray = obj.getJsonArray(fieldName);
            var arrayType = fieldType.getComponentType();
            var objArray = Array.newInstance(arrayType, jsonArray.size());

            for (int i = 0; i < jsonArray.size(); i++) {
                Array.set(objArray, i, getValueFromJsonArray(jsonArray, arrayType, i));
            }

            return objArray;
        }

        return obj.get(field.getName());
    }

    private Object getValueFromJsonArray(JsonArray jsonArray, Class<?> componentType, int index) {
        if (componentType.equals(Integer.TYPE)) {
            return jsonArray.getInt(index);
        }

        if (componentType.equals(Boolean.TYPE)) {
            return jsonArray.getBoolean(index);
        }

        if (componentType.equals(String.class)) {
            return jsonArray.getString(index);
        }

        return null;
    }
}