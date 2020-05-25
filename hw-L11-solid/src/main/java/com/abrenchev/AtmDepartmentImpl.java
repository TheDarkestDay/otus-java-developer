package com.abrenchev;

import com.abrenchev.interfaces.Atm;
import com.abrenchev.interfaces.AtmDepartment;
import com.abrenchev.interfaces.AtmEventListener;
import com.abrenchev.interfaces.AtmEventSource;

import java.util.ArrayList;
import java.util.List;

public class AtmDepartmentImpl implements AtmDepartment, AtmEventSource {
    List<Atm> atms = new ArrayList<>();

    List<AtmEventListener> listeners = new ArrayList<>();

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

        this.subscribe(() -> newAtm.processCommand(new AtmResetCommand()));
    }

    public void resetAtms() {
        this.listeners.forEach(AtmEventListener::handle);
    }

    public int getAtmsCount() {
        return atms.size();
    }

    @Override
    public void subscribe(AtmEventListener listener) {
        listeners.add(listener);
    }
}
