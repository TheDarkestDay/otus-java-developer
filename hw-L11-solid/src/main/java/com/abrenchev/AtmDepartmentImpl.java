package com.abrenchev;

import com.abrenchev.interfaces.Atm;
import com.abrenchev.interfaces.AtmDepartment;
import java.util.ArrayList;
import java.util.List;

public class AtmDepartmentImpl implements AtmDepartment {
    List<Atm> atms = new ArrayList<>();

    public int getRemainingFunds() {
        return atms.stream()
                .mapToInt(Atm::getRemainingFunds)
                .reduce(0, Integer::sum);
    }

    public Atm getAtm(int index) {
        return this.atms.get(index);
    }

    public void addAtm(Atm newAtm) {
        this.atms.add(newAtm);
    }

    public void resetAtms() {
        this.atms.forEach(Atm::reset);
    }

    public int getAtmsCount() {
        return atms.size();
    }
}
