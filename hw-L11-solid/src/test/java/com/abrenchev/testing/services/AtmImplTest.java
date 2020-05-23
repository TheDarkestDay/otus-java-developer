package com.abrenchev.testing.services;

import com.abrenchev.AtmImpl;
import com.abrenchev.AtmState;
import com.abrenchev.Banknote;
import com.abrenchev.BanknoteValue;
import com.abrenchev.exceptions.AtmWithdrawException;
import com.abrenchev.interfaces.Atm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AtmImplTest")
public class AtmImplTest {
    @Test()
    @DisplayName("should correctly return remaining funds amount after ATM creation")
    public void testRemainingFundsAfterCreation() {
        AtmState state = new AtmState();

        state.putBanknotes(BanknoteValue.OneThousand, 1);
        state.putBanknotes(BanknoteValue.FiveHundred, 1);
        state.putBanknotes(BanknoteValue.OneHundred, 1);

        Atm atm = new AtmImpl(1, state);

        assertThat(atm.getRemainingFunds()).isEqualTo(1600);
    }

    @Test()
    @DisplayName("should decrease remaining funds amount after withdraw")
    public void testWithdrawRemainingAmountDecrease() {
        AtmState state = new AtmState();

        state.putBanknotes(BanknoteValue.OneThousand, 1);
        state.putBanknotes(BanknoteValue.FiveHundred, 1);
        state.putBanknotes(BanknoteValue.OneHundred, 1);

        Atm atm = new AtmImpl(1, state);
        atm.withdraw(600);

        assertThat(atm.getRemainingFunds()).isEqualTo(1000);
    }

    @Test()
    @DisplayName("should withdraw the given amount of funds")
    public void testWithdraw() {
        AtmState state = new AtmState();

        state.putBanknotes(BanknoteValue.OneThousand, 1);
        state.putBanknotes(BanknoteValue.FiveHundred, 1);
        state.putBanknotes(BanknoteValue.OneHundred, 1);

        Atm atm = new AtmImpl(1, state);

        List<Banknote> result = atm.withdraw(600);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getValue().getNumericValue()).isEqualTo(500);
        assertThat(result.get(1).getValue().getNumericValue()).isEqualTo(100);
    }

    @Test()
    @DisplayName("should throw exception if it's not possible to withdraw the given amount")
    public void testWithdrawException() {
        AtmState state = new AtmState();

        state.putBanknotes(BanknoteValue.OneThousand, 1);
        state.putBanknotes(BanknoteValue.FiveHundred, 1);
        state.putBanknotes(BanknoteValue.OneHundred, 1);

        Atm atm = new AtmImpl(1, state);

        assertThatThrownBy(() -> atm.withdraw(700)).isInstanceOf(AtmWithdrawException.class);
    }

    @Test()
    @DisplayName("should add new funds")
    public void testAddFunds() {
        AtmState state = new AtmState();

        state.putBanknotes(BanknoteValue.OneThousand, 1);
        state.putBanknotes(BanknoteValue.FiveHundred, 1);
        state.putBanknotes(BanknoteValue.OneHundred, 1);

        Atm atm = new AtmImpl(1, state);

        List<Banknote> newBanknotes = new ArrayList<>();
        newBanknotes.add(new Banknote(BanknoteValue.TwoHundred));
        newBanknotes.add(new Banknote(BanknoteValue.FiveThousand));
        atm.addFunds(newBanknotes);

        assertThat(atm.getRemainingFunds()).isEqualTo(6800);
    }
}
