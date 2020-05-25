package com.abrenchev;

import com.abrenchev.interfaces.Atm;
import com.abrenchev.interfaces.AtmCommand;

public class AtmResetCommand implements AtmCommand {
    public void execute(Atm atm) {
        atm.reset();
    }
}
