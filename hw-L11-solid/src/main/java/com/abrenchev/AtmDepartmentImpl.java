package com.abrenchev;

import com.abrenchev.interfaces.Atm;
import com.abrenchev.interfaces.AtmDepartment;
import com.abrenchev.interfaces.EventListener;

import java.util.ArrayList;
import java.util.List;

public class AtmDepartmentImpl implements AtmDepartment {
    List<Atm> atms = new ArrayList<>();

    List<EventListener> resetListeners = new ArrayList<>();

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

        resetListeners.add(() -> {
            newAtm.processCommand(Atm::reset);
        });
    }

    public void resetAtms() {
        resetListeners.forEach(EventListener::handle);
    }

    public int getAtmsCount() {
        return atms.size();
    }
}
