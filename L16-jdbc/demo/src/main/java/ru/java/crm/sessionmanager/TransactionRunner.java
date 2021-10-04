package ru.java.crm.sessionmanager;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
