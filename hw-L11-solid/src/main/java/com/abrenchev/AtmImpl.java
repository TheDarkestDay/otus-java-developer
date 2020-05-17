package com.abrenchev;

import com.abrenchev.exceptions.AtmWithdrawException;
import com.abrenchev.interfaces.Atm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AtmImpl implements Atm {
    private TreeMap<Integer, List<Banknote>> banknoteStorage = new TreeMap<>();

    private void addBanknote(Banknote banknote) {
        int value = banknote.getValue();

        if (!banknoteStorage.containsKey(value)) {
            banknoteStorage.put(value, new ArrayList<>());
        }

        banknoteStorage.get(value).add(banknote);
    }

    public AtmImpl(List<Banknote> initialFunds) {
        initialFunds.forEach(this::addBanknote);
    }

    @Override
    public int getRemainingFunds() {
        int total = 0;

        for (Map.Entry<Integer, List<Banknote>> entry : banknoteStorage.entrySet()) {
            total += entry.getKey() * entry.getValue().size();
        }

        return total;
    }

    @Override
    public List<Banknote> withdraw(int amount) {
        List<Banknote> result = new ArrayList<>();
        int amountToProvide = amount;

        for (Map.Entry<Integer, List<Banknote>> entry : banknoteStorage.descendingMap().entrySet()) {
            List<Banknote> currentBanknotes = entry.getValue();
            int currentBanknoteValue = entry.getKey();
            int givenBanknotesCount = amountToProvide / currentBanknoteValue;

            int i = 0;
            while (i < givenBanknotesCount && !currentBanknotes.isEmpty()) {
                result.add(currentBanknotes.remove(0));
                amountToProvide -= currentBanknoteValue;
                i++;
            }
        }

        if (amountToProvide != 0) {
            throw new AtmWithdrawException();
        }

        return result;
    }

    @Override
    public void addFunds(List<Banknote> funds) {
        funds.forEach(this::addBanknote);
    }
}
