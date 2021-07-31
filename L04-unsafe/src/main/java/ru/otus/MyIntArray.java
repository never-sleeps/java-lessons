package ru.otus;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class MyIntArray implements AutoCloseable {
    private final Unsafe unsafe;
    private final long elementSizeBytes;
    private int size;
    private long arrayBeginIdx;

    public MyIntArray(int size) throws InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException
    {
        this.size = size;

        Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        unsafe = unsafeConstructor.newInstance();
        elementSizeBytes = Integer.SIZE / 8;
        arrayBeginIdx = unsafe.allocateMemory(this.size * elementSizeBytes);
    }

    public void setValue(long index, int value) {
        if (index == size) {
            this.size *= 2;
            arrayBeginIdx = unsafe.reallocateMemory(arrayBeginIdx, this.size * elementSizeBytes);
        }
        unsafe.putInt(calcIndex(index), value);
    }

    public int getValue(long index) {
        return unsafe.getInt(calcIndex(index));
    }

    @Override
    public void close() {
        unsafe.freeMemory(arrayBeginIdx);
    }

    private long calcIndex(long offset) {
        return arrayBeginIdx + offset * this.elementSizeBytes;
    }
}
