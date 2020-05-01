package com.abrenchev;

import com.abrenchev.annotations.Log;

public class SampleClass {

    @Log
    public void doSomething(String arg, String arg1) {
        int test = 0;
        System.out.println("I've done something" + arg + " and even better " + arg1);
        System.out.println(test);
    }

    public void dontDoSomething() {
        System.out.println("Something went wrong if you see logs prior me");
    }
}
