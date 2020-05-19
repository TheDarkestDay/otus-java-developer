package com.abrenchev;

import java.util.ArrayList;
import java.util.List;

public class AtmCell {
    private List<Banknote> banknotes = new ArrayList<>();

    public void putBanknote(Banknote banknote) {
        banknotes.add(banknote);
    }

    public int getRemainingFunds() {
        int result = 0;

        for (Banknote banknote : banknotes) {
            result += banknote.getValue().getNumericValue();
        }

        return result;
    }

    public Banknote takeBanknote() {
        return banknotes.remove(0);
    }

    public boolean isEmpty() {
        return banknotes.isEmpty();
    }
}
