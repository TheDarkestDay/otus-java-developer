package com.abrenchev.core.model;

import com.abrenchev.jdbc.mapper.annotations.Id;

public class Account {
    @Id
    private long no;

    private String type;

    private int rest;

    public Account() {

    }

    public Account(long no, String type, int rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public int getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest='" + rest + '\'' +
                '}';
    }
}
