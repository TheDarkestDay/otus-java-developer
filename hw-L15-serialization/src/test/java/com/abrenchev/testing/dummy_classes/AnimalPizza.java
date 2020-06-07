package com.abrenchev.testing.dummy_classes;

import java.util.List;

public class AnimalPizza {
    private int id;
    private Animal animal;
    private List<Pizza> pizzas;

    public AnimalPizza() {
        id = 11;

        animal = new Animal();
        animal.setAge(13);
        animal.setAdopted(true);
        animal.setName("Vasya");

        Pizza pizza = new Pizza();
        pizza.setName("Mozzarella");
        pizza.setIngredients(new String[]{"cheese", "tomato"});
        pizzas = List.of(pizza);
    }
}