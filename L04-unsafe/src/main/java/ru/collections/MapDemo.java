package ru.collections;

import java.util.HashMap;

public class MapDemo {

    public static void main(String[] args) {

        var mapSize = 400_000;
        var keyStr = "i";

        // HashMap test
        long sum1 = 0;
        var hashMap = new HashMap<String, Integer>(mapSize);
        long startTime = System.nanoTime();

        for (var idx = 0; idx < mapSize; idx++) {
            hashMap.put(keyStr + idx, idx);
        }

        for (var idx = 0; idx < mapSize; idx++) {
            sum1 += hashMap.get(keyStr + idx);
        }
        System.out.println("HashMap time:" + (System.nanoTime() - startTime));

        // MyMap test
        long sum2 = 0;
        var myMap = new MyIntMap(mapSize);
        startTime = System.nanoTime();

        for (var idx = 0; idx < mapSize; idx++) {
            myMap.put(keyStr + idx, idx);
        }

        for (var idx = 0; idx < mapSize; idx++) {
            sum2 += myMap.get(keyStr + idx);
        }
        System.out.println("MyMapInt time:" + (System.nanoTime() - startTime));

        System.out.println();
        System.out.println("HashMap sum=" + sum1 + ", MyMapInt sum=" + sum2);
        System.out.println("results are equal = " + (sum1 == sum2));
    }
}
