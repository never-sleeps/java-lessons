package ru.java.core.sessionmanager;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
