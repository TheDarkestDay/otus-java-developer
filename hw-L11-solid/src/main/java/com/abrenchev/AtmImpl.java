package com.abrenchev;

import com.abrenchev.exceptions.AtmWithdrawException;
import com.abrenchev.interfaces.Atm;
import com.abrenchev.interfaces.AtmCommand;

import java.util.*;

public class AtmImpl implements Atm {
    private AtmState atmState;

    private final AtmState initialState;

    private final int id;

    private void addBanknote(Banknote banknote) {
        atmState.putBanknotes(banknote.getValue(), 1);
    }

    public AtmImpl(int id, AtmState initialState) {
        this.id = id;
        atmState = initialState;
        this.initialState = initialState.createCopy();
    }

    @Override
    public int getRemainingFunds() {
        return atmState.getTotalFundsAmount();
    }

    @Override
    public List<Banknote> withdraw(int amount) {
        List<Banknote> result = new ArrayList<>();
        List<BanknoteValue> banknoteValues = atmState.getDescendingBanknoteValues();

        int amountToProvide = amount;

        for (BanknoteValue banknoteValue : banknoteValues) {
            int currentBanknoteValue = banknoteValue.getNumericValue();
            int givenBanknotesCount = amountToProvide / currentBanknoteValue;

            int i = 0;
            while (i < givenBanknotesCount && atmState.hasBanknote(banknoteValue)) {
                result.add(atmState.takeBanknote(banknoteValue));
                amountToProvide -= currentBanknoteValue;
                i++;
            }
        }

        if (amountToProvide != 0) {
            throw new AtmWithdrawException(amount);
        }

        return result;
    }

    @Override
    public void addFunds(List<Banknote> funds) {
        funds.forEach(this::addBanknote);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void reset() {
        atmState = initialState;
    }

    @Override
    public void processCommand(AtmCommand command) {
        command.execute(this);
    }
}
