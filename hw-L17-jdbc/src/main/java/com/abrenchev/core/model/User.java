package com.abrenchev.core.model;

import com.abrenchev.jdbc.mapper.annotations.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {
    @Id
    private long id;

    private String name;

    private int age;

    public User() {

    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
