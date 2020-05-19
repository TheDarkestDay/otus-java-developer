package com.abrenchev;

public class Banknote {
    private BanknoteValue value;

    public Banknote(BanknoteValue value) {
        this.value = value;
    }

    public BanknoteValue getValue() {
        return value;
    }
}
