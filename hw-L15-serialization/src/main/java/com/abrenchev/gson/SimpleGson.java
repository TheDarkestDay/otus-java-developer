package com.abrenchev.gson;

import javax.json.Json;
import javax.json.Json.*;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.lang.reflect.Field;

public class SimpleGson {
    private int getIntegerValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (int) field.get(obj);
        } catch (IllegalAccessException exception) {
            return 0;
        }
    }

    private boolean getBooleanValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (boolean) field.get(obj);
        } catch (IllegalAccessException exception) {
            return false;
        }
    }

    private String getStringValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return (String) field.get(obj);
        } catch (IllegalAccessException exception) {
            return "";
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

        return JsonValue.NULL;
    }


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
        return null;
    }
}