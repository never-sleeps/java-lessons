package ru.otus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MyIntArrayTest {

    @Test
    @DisplayName("value from array")
    void setGetValue() throws Exception {
        MyIntArray myArr = new MyIntArray(1);
        int val = 45;
        int idx = 0;
        myArr.setValue(idx, val);
        assertEquals(val, myArr.getValue(idx));
    }

    @Test
    @DisplayName("value from array for index")
    void setGetValueSeq() throws Exception {
        int size = 100;
        MyIntArray myArr = new MyIntArray(size);
        for (int idx = 0; idx < size; idx++) {
            myArr.setValue(idx, idx);
        }

        for (int idx = 0; idx < size; idx++) {
            assertEquals(idx, myArr.getValue(idx));
        }
    }

    @Test
    @DisplayName("value from array for index with initial size")
    void incSize() throws Exception {
        int sizeInit = 1;
        int sizeMax = 100;
        MyIntArray myArr = new MyIntArray(sizeInit);
        for (int idx = 0; idx < sizeMax; idx++) {
            myArr.setValue(idx, idx);
        }

        for (int idx = 0; idx < sizeMax; idx++) {
            assertEquals(idx, myArr.getValue(idx));
        }
    }
}
