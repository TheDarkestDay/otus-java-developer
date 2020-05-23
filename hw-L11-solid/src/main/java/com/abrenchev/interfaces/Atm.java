package com.abrenchev.interfaces;

import com.abrenchev.Banknote;

import java.util.List;

public interface Atm {
    int getRemainingFunds();
    List<Banknote> withdraw(int amount);
    void addFunds(List<Banknote> funds);
    int getId();
    void reset();
}
