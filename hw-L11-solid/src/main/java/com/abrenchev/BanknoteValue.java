package com.abrenchev;

public enum BanknoteValue {
    OneHundred (100),
    TwoHundred (200),
    FiveHundred (500),
    OneThousand (1000),
    FiveThousand (5000);

    private int value;

    BanknoteValue(int value) {
        this.value = value;
    }

    public int getNumericValue() {
        return value;
    }
}
