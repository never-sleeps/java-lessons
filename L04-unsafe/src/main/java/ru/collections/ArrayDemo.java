package ru.collections;

import java.util.ArrayList;

public class ArrayDemo {

    public static void main(String[] args) throws Exception {
        var arraySizeMax = 2_000_000;
        var arraySizeInit = 10;

        // MyArrayInt test
        long sum1 = 0;
        try (var myArr = new MyIntArray(arraySizeInit)) {
            var startTime = System.currentTimeMillis();

            for (var idx = 0; idx < arraySizeMax; idx++) {
                myArr.setValue(idx, idx);
            }

            for (var idx = 0; idx < arraySizeMax; idx++) {
                sum1 += myArr.getValue(idx);
            }
            System.out.println("MyArrayInt time:" + (System.nanoTime() - startTime));
        }

        // ArrayList test
        long sum2 = 0;
        var arrayList = new ArrayList<Integer>(arraySizeInit);
        var startTime = System.nanoTime();

        for (var idx = 0; idx < arraySizeMax; idx++) {
            arrayList.add(idx, idx);
        }

        for (var idx = 0; idx < arraySizeMax; idx++) {
            sum2 += arrayList.get(idx);
        }
        System.out.println("ArrayList time:" + (System.nanoTime() - startTime));

        System.out.println();
        System.out.println("MyArrayInt sum = " + sum1 + ", ArrayList sum = " + sum2);
        System.out.println("results are equal = " + (sum1 == sum2));
    }
}
