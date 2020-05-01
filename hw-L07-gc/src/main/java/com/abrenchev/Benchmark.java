package com.abrenchev;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class TestObject {
    private long value;

    public TestObject(long value) {
        this.value = value;
    }
}

public class Benchmark implements BenchmarkMBean {
    private volatile int size = 0;

    public void run(int itemsCount, int sleepTime, int loopCount) throws InterruptedException {
        List<TestObject> list = new ArrayList<>();

        for (int i = 0; i < loopCount; i++) {
            for (int j = 0; j < itemsCount; i++) {
                TestObject obj = new TestObject(ThreadLocalRandom.current().nextLong());
                list.add(obj);
            }

            for (int j = 0; j < itemsCount / 3; i++) {
                list.remove(0);
            }

            Thread.sleep(sleepTime);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
}
