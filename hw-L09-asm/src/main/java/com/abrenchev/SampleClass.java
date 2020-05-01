package com.abrenchev;

import com.abrenchev.annotations.Log;

public class SampleClass {

    @Log
    public void doSomething(String arg, String arg1) {
        System.out.println("I've done something");
    }

    public void dontDoSomething() {
        System.out.println("Something went wrong if you see logs prior me");
    }
}
