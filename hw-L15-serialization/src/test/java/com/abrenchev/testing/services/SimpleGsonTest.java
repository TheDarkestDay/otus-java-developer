package com.abrenchev.testing.services;

import com.abrenchev.gson.SimpleGson;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class Animal {
    public String name;
    public int age;
    public boolean isAdopted;

    public Animal() {

    }
}

class Pizza {
    public String name;
    public String[] ingredients;

    public Pizza() {

    }
}

@DisplayName("SimpleGson")
public class SimpleGsonTest {
    @Test
    @DisplayName("should correctly transform object to JSON string with primitive values")
    public void testToJson() {
        Animal pet = new Animal();
        pet.name = "Tom";
        pet.age = 7;
        pet.isAdopted = true;

        Gson gson = new Gson();
        SimpleGson simpleGson = new SimpleGson();

        assertThat(simpleGson.toJson(pet)).isEqualTo(gson.toJson(pet));
    }

    @Test
    @DisplayName("should correctly build a new object from JSON string")
    public void testFromJson() {
        String petJson = "{\"name\": \"Kate\", \"age\": 15, \"isAdopted\": false}";
        SimpleGson simpleGson = new SimpleGson();
        Animal parsedAnimal =  (Animal) simpleGson.fromJson(petJson, Animal.class);

        assertThat(parsedAnimal.name).isEqualTo("Kate");
        assertThat(parsedAnimal.age).isEqualTo(15);
        assertThat(parsedAnimal.isAdopted).isEqualTo(false);
    }

    @Test
    @DisplayName("should correctly transform arrays of primitive values")
    public void testToJsonPrimitiveArrays() {
        Pizza pepperoni = new Pizza();
        pepperoni.name = "Pepperoni";
        pepperoni.ingredients = new String[]{"salami", "cheese", "tomato"};

        Gson gson = new Gson();
        SimpleGson simpleGson = new SimpleGson();

        assertThat(simpleGson.toJson(pepperoni)).isEqualTo(gson.toJson(pepperoni));
    }

    @Test
    @DisplayName("should correctly reads arrays of primitive values")
    public void testFromJsonPrimitiveValues() {
        Pizza pepperoni = new Pizza();
        pepperoni.name = "Pepperoni";
        pepperoni.ingredients = new String[]{"salami", "cheese", "tomato"};

        Gson gson = new Gson();

        String pizzaJson = gson.toJson(pepperoni);
        SimpleGson simpleGson = new SimpleGson();

        Pizza parsedPizza = (Pizza) simpleGson.fromJson(pizzaJson, Pizza.class);

        assertThat(parsedPizza.name).isEqualTo(pepperoni.name);
        assertThat(parsedPizza.ingredients.length).isEqualTo(pepperoni.ingredients.length);

        for (int i = 0; i < parsedPizza.ingredients.length; i++) {
            assertThat(parsedPizza.ingredients[i]).isEqualTo(pepperoni.ingredients[i]);
        }
    }
}