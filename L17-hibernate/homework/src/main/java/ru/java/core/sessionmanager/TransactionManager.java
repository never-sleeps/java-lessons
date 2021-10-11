package ru.java.core.sessionmanager;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
