package com.abrenchev.testing.services;

import com.abrenchev.*;
import com.abrenchev.interfaces.AtmDepartment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AtmDepartmentImplTest")
public class AtmDepartmentImplTest {
    @Test()
    @DisplayName("should calculate the correct amount of remaining funds")
    public void testGetRemainingFunds() {
        AtmDepartment department = new AtmDepartmentImpl();

        int atmsCount = 3;
        AtmState state = new AtmState();
        state.putBanknotes(BanknoteValue.FiveHundred, 1);

        for (int i = 0; i < atmsCount; i++) {
            department.addAtm(new AtmImpl(i, state));
        }

        assertThat(department.getRemainingFunds()).isEqualTo(1500);
    }

    @Test()
    @DisplayName("should allow to get one particular ATM")
    public void testGetAtm() {
        AtmDepartment department = new AtmDepartmentImpl();

        int atmsCount = 3;
        AtmState state = new AtmState();
        state.putBanknotes(BanknoteValue.FiveHundred, 1);

        for (int i = 0; i < atmsCount; i++) {
            department.addAtm(new AtmImpl(i, state));
        }

        assertThat(department.getAtm(1).getId()).isEqualTo(1);
    }

    @Test()
    @DisplayName("should allow to add a new ATM")
    public void testAddAtm() {
        AtmDepartment department = new AtmDepartmentImpl();

        int atmsCount = 3;
        AtmState state = new AtmState();
        state.putBanknotes(BanknoteValue.FiveHundred, 1);

        for (int i = 0; i < atmsCount; i++) {
            department.addAtm(new AtmImpl(i, state));
        }

        assertThat(department.getAtmsCount()).isEqualTo(atmsCount);
    }

    @Test()
    @DisplayName("should reset the whole department to the initial state")
    public void testResetAtms() {
        AtmDepartment department = new AtmDepartmentImpl();

        int atmsCount = 3;
        AtmState state = new AtmState();
        state.putBanknotes(BanknoteValue.FiveHundred, 1);

        for (int i = 0; i < atmsCount; i++) {
            department.addAtm(new AtmImpl(i, state));
        }

        List<Banknote> fundsToAdd = new ArrayList<>();
        fundsToAdd.add(new Banknote(BanknoteValue.TwoHundred));
        fundsToAdd.add(new Banknote(BanknoteValue.FiveThousand));

        department.getAtm(0).addFunds(fundsToAdd);

        department.resetAtms();

        assertThat(department.getRemainingFunds()).isEqualTo(1500);
    }
}
