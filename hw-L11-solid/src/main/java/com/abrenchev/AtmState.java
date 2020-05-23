package com.abrenchev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmState {
    Map<BanknoteValue, Integer> banknoteStorage = new HashMap<>();

    public void putBanknotes(BanknoteValue banknoteValue, int count) {
        if (!banknoteStorage.containsKey(banknoteValue)) {
            banknoteStorage.put(banknoteValue, count);
        } else {
            banknoteStorage.put(banknoteValue, banknoteStorage.get(banknoteValue) + count);
        }
    }

    public int getTotalFundsAmount() {
        return banknoteStorage.entrySet().stream()
                .map(entry -> {
                    int numericValue = entry.getKey().getNumericValue();
                    int banknotesCount = entry.getValue();

                    return numericValue * banknotesCount;
                })
                .reduce(0, Integer::sum);
    }

    public Banknote takeBanknote(BanknoteValue banknoteValue) {
        int previousCount = banknoteStorage.get(banknoteValue);
        banknoteStorage.put(banknoteValue, previousCount - 1);

        return new Banknote(banknoteValue);
    }

    public List<BanknoteValue> getDescendingBanknoteValues() {
        List<Map.Entry<BanknoteValue, Integer>> entries = new ArrayList<>(banknoteStorage.entrySet());
        entries.sort((entryA, entryB) -> entryB.getKey().getNumericValue() - entryA.getKey().getNumericValue());

        return entries.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public boolean hasBanknote(BanknoteValue banknoteValue) {
        return banknoteStorage.containsKey(banknoteValue) && banknoteStorage.get(banknoteValue) != 0;
    }

    public AtmState createCopy() {
        AtmState clonedState = new AtmState();

        for (Map.Entry<BanknoteValue, Integer> entry : banknoteStorage.entrySet()) {
            clonedState.putBanknotes(entry.getKey(), entry.getValue());
        }

        return clonedState;
    }
}
