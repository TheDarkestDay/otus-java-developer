package com.abrenchev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        new App().logRange(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
    }

    public void logRange(List<Integer> range) {
        CompletableFuture<Integer> resultFuture = CompletableFuture.completedFuture(0);
        List<CompletableFuture<Integer>> numberFutures = range.stream()
                .map((num) -> CompletableFuture.supplyAsync(() -> num)).collect(Collectors.toList());

        for (CompletableFuture<Integer> numFuture : numberFutures) {
            resultFuture = resultFuture
                    .thenCombineAsync(numFuture, (empty, num) -> num)
                    .thenApplyAsync(this::logNumber)
                    .thenApplyAsync(this::logNumber);
        }

        resultFuture.join();
    }

    private int logNumber(int value) {
        logger.info("Logged value: " + value);
        return value;
    }
}
