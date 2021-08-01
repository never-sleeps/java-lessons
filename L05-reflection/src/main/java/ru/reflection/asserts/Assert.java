package ru.reflection.asserts;

import ru.reflection.exceptions.TestExecutionException;

public class Assert {
    public static void assertTrue(boolean statement) {
        if (!statement) {
            throw new TestExecutionException("AssertionFailedError: expected <true> but was <false>");
        }
    }

    public static void assertFalse(boolean statement) {
        if (statement) {
            throw new TestExecutionException("AssertionFailedError: expected <false> but was <true>");
        }
    }

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new TestExecutionException(String.format("AssertionFailedError: expected <%s> but was <%s>", expected, actual));
        }
    }
}
