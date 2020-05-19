package com.abrenchev;

import com.abrenchev.exceptions.AtmWithdrawException;
import com.abrenchev.interfaces.Atm;

import java.util.*;

public class AtmImpl implements Atm {
    private Map<BanknoteValue, AtmCell> banknoteStorage = new HashMap<>();

    private void addBanknote(Banknote banknote) {
        BanknoteValue value = banknote.getValue();

        if (!banknoteStorage.containsKey(value)) {
            banknoteStorage.put(value, new AtmCell());
        }

        banknoteStorage.get(value).putBanknote(banknote);
    }

    public AtmImpl(List<Banknote> initialFunds) {
        initialFunds.forEach(this::addBanknote);
    }

    @Override
    public int getRemainingFunds() {
        int total = 0;

        for (Map.Entry<BanknoteValue, AtmCell> entry : banknoteStorage.entrySet()) {
            total += entry.getValue().getRemainingFunds();
        }

        return total;
    }

    @Override
    public List<Banknote> withdraw(int amount) {
        List<Banknote> result = new ArrayList<>();
        List<Map.Entry<BanknoteValue, AtmCell>> entries = new ArrayList<>(banknoteStorage.entrySet());
        entries.sort((entryA, entryB) -> entryB.getKey().getNumericValue() - entryA.getKey().getNumericValue());

        int amountToProvide = amount;

        for (Map.Entry<BanknoteValue, AtmCell> entry : entries) {
            AtmCell currentCell = entry.getValue();
            int currentBanknoteValue = entry.getKey().getNumericValue();
            int givenBanknotesCount = amountToProvide / currentBanknoteValue;

            int i = 0;
            while (i < givenBanknotesCount && !currentCell.isEmpty()) {
                result.add(currentCell.takeBanknote());
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
