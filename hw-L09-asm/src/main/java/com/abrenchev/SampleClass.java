package com.abrenchev;

import com.abrenchev.annotations.Log;

public class SampleClass {
    @Log
    public void doSomething(String arg, int test, double dbl) {
        System.out.println(arg + test + dbl);
    }

    public void noLog(String arg) {
        System.out.println("IT SHOULD NOT BE LOGGED");
    }
}
