package com.abrenchev.gson;

import com.abrenchev.gson.exceptions.SimpleGsonException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class SimpleGson {
    public String toJson(Object obj) {
        var jsonBuilder = Json.createObjectBuilder();

        for (Field objField : obj.getClass().getDeclaredFields()) {
            if (!objField.getName().equals("this$0")) {
                jsonBuilder.add(objField.getName(), getFieldValue(obj, objField));
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

    private void setObjectField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not set field value of " + field.getName(), exception);
        }
    }

    private int getIntegerValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (int) field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not get an int field of " + field.getName(), exception);
        }
    }

    private boolean getBooleanValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (boolean) field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not get a boolean field value of " + field.getName(), exception);
        }
    }

    private String getStringValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (String) field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not get a string field value of " + field.getName(), exception);
        }
    }

    private JsonArray getJsonArray(Object obj, Field field) {
        try {
            var arrayBuilder = Json.createArrayBuilder();
            field.setAccessible(true);
            var objArray = field.get(obj);
            var objArrayType = field.getType().getComponentType();
            var arrayLength = Array.getLength(objArray);

            for (int i = 0; i < arrayLength; i++) {
                if (objArrayType.equals(Integer.TYPE)) {
                    arrayBuilder.add(Array.getInt(objArray, i));
                } else if (objArrayType.equals(Boolean.TYPE)) {
                    arrayBuilder.add(Array.getBoolean(objArray, i));
                } else if (objArrayType.equals(String.class)) {
                    arrayBuilder.add((String) Array.get(objArray, i));
                } else {
                    arrayBuilder.add(JsonValue.NULL);
                }
            }

            return arrayBuilder.build();
        } catch (IllegalAccessException exception) {
            throw new SimpleGsonException("Could not get an array field value of " + field.getName(), exception);
        }
    }

    private JsonValue getFieldValue(Object obj, Field field) {
        Class<?> fieldType = field.getType();

        if (fieldType.equals(Integer.TYPE)) {
            return Json.createValue(getIntegerValue(obj, field));
        }

        if (fieldType.equals(Boolean.TYPE)) {
            return getBooleanValue(obj, field) ? JsonValue.TRUE : JsonValue.FALSE;
        }

        if (fieldType.equals(String.class)) {
            return Json.createValue(getStringValue(obj, field));
        }

        if (fieldType.isArray()) {
            return getJsonArray(obj, field);
        }

        return JsonValue.NULL;
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
                if (arrayType.equals(Integer.TYPE)) {
                    Array.setInt(objArray, i, jsonArray.getInt(i));
                } else if (arrayType.equals(Boolean.TYPE)) {
                    Array.setBoolean(objArray, i, jsonArray.getBoolean(i));
                } else if (arrayType.equals(String.class)) {
                    Array.set(objArray, i, jsonArray.getString(i));
                } else {
                    Array.set(objArray, i, null);
                }
            }

            return objArray;
        }

        return obj.get(field.getName());
    }
}