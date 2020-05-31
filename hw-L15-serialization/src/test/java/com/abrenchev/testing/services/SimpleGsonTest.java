package com.abrenchev.testing.services;

import com.abrenchev.gson.SimpleGson;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

@DisplayName("SimpleGson")
public class SimpleGsonTest {
    class Animal {
        public String name;
        public int age;
        public boolean isAdopted;

        public Animal(String name, int age, boolean isAdopted) {
            this.name = name;
            this.age = age;
            this.isAdopted = isAdopted;
        }
    }

    @Test
    @DisplayName("should correctly transform object to JSON string with primitive values")
    public void testToJson() {
        Animal pet = new Animal("Tom", 7, true);

        Gson gson = new Gson();
        SimpleGson simpleGson = new SimpleGson();

        assertThat(simpleGson.toJson(pet)).isEqualTo(gson.toJson(pet));
    }

    @Test
    @DisplayName("should correctly build a new object from JSON string")
    public void testFromJson() {
        String petJson = "{name: \"Kate\", age: 15, isAdopted: false}";

        Gson gson = new Gson();
        SimpleGson simpleGson = new SimpleGson();

        assertThat(simpleGson.fromJson(petJson, Animal.class)).isEqualTo(gson.fromJson(petJson, Animal.class));
    }
}