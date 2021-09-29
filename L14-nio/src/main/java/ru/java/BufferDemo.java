package ru.java;

import java.nio.CharBuffer;

public class BufferDemo {
    public static void main(String[] args) {
        new BufferDemo().go();
    }

    /**
     * Демонстрация сначала записи в буфер, затем чтения из него.
     */
    private void go() {
        var buffer = CharBuffer.allocate(10);
        System.out.printf("capacity: %d limit: %d position: %d%n", buffer.capacity(), buffer.limit(), buffer.position());

        var text = "testText".toCharArray();
        for (var idx = 0; idx < text.length; idx += 2) {
            buffer.put(text, idx, 2);
            System.out.printf("idx: %d capacity: %d limit: %d position: %d %n", idx, buffer.capacity(), buffer.limit(), buffer.position());
        }

        buffer.flip();

        System.out.println("-----");
        for (var idx = 0; idx < buffer.limit(); idx++) {
            System.out.printf("idx: %d char: %s capacity: %d limit:%d position: %d %n", idx, buffer.get(), buffer.capacity(), buffer.limit(), buffer.position());
        }
    }
    /*
        capacity: 10 limit: 10 position: 0
        idx: 0 capacity: 10 limit: 10 position: 2
        idx: 2 capacity: 10 limit: 10 position: 4
        idx: 4 capacity: 10 limit: 10 position: 6
        idx: 6 capacity: 10 limit: 10 position: 8
        -----
        idx: 0 char: t capacity: 10 limit:8 position: 1
        idx: 1 char: e capacity: 10 limit:8 position: 2
        idx: 2 char: s capacity: 10 limit:8 position: 3
        idx: 3 char: t capacity: 10 limit:8 position: 4
        idx: 4 char: T capacity: 10 limit:8 position: 5
        idx: 5 char: e capacity: 10 limit:8 position: 6
        idx: 6 char: x capacity: 10 limit:8 position: 7
        idx: 7 char: t capacity: 10 limit:8 position: 8
     */
}
