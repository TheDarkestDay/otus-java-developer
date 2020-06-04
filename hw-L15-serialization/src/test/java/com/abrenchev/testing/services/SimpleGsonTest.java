package com.abrenchev.testing.services;

import com.abrenchev.gson.SimpleGson;
import com.abrenchev.testing.dummy_classes.Animal;
import com.abrenchev.testing.dummy_classes.Pizza;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.*;

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

    @ParameterizedTest
    @MethodSource("generateDataForToJsonValuesTest")
    void testToJsonValues(Object o){
        Gson gson = new GsonBuilder().serializeNulls().create();
        SimpleGson simpleGson = new SimpleGson();
        assertThat(simpleGson.toJson(o)).isEqualTo(gson.toJson(o));
    }

    private static Stream<Arguments> generateDataForToJsonValuesTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte)1), Arguments.of((short)2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new boolean[] {false, true}),
                Arguments.of(new char[] {'a', 'b', 'c'}),
                Arguments.of(new byte[] {1, 2, 3}),
                Arguments.of(new short[] {4, 5, 6}),
                Arguments.of(new int[] {7, 8, 9}),
                Arguments.of(new long[] {9, 10, 11}),
                Arguments.of(new float[] {12f, 13f, 14f}),
                Arguments.of(new double[] {15d, 16d, 17d}),
                Arguments.of(List.of(18, 19, 20)),
                Arguments.of(Collections.singletonList(21))
        );
    }
}