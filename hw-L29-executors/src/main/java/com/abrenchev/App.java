package com.abrenchev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    private long lastLoggedThreadId;

    public static void main(String... args) {
        var range = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        var app = new App();

        new Thread(() -> app.logRange(range)).start();
        new Thread(() -> app.logRange(range)).start();
    }

    public synchronized void logRange(List<Integer> range) {
        for (Integer num : range) {
            try {
                long threadId = Thread.currentThread().getId();
                while (lastLoggedThreadId == threadId) {
                    wait();
                }

                lastLoggedThreadId = threadId;
                logger.info("Logging value: " + num);
                Thread.sleep(1500);
                notifyAll();
            } catch (InterruptedException exception) {
                throw new AppException("Something went wrong", exception);
            }
        }
    }
}
