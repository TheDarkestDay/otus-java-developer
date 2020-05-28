package com.abrenchev.interfaces;

public interface AtmDepartment {
    Atm getAtm(int index);
    void addAtm(Atm newAtm);
    int getRemainingFunds();
    void resetAtms();
    int getAtmsCount();
}
