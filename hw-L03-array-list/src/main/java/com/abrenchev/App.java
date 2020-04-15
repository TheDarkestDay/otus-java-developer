package com.abrenchev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static void main(String... args) {
        List<Integer> list = new DIYList<Integer>(20);

        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

        if (list.size() != 20) {
            throw new RuntimeException("List size is wrong");
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != i + 1) {
                throw new RuntimeException("addAll messed with the list content");
            }
        }

        int testArrSize = 20;
        List<Integer> testArr = new ArrayList<>();
        for (int i = 0; i < testArrSize; i++) {
            testArr.add(ThreadLocalRandom.current().nextInt());
        }

        Collections.copy(list, testArr);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(testArr.get(i))) {
                throw new RuntimeException("Collections.copy messed with the content");
            }
        }

        Collections.sort(testArr);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(testArr.get(i))) {
                throw new RuntimeException("Collections.sort works incorrectly");
            }
        }
    }
}
