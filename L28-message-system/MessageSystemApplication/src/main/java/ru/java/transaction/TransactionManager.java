package ru.java.transaction;

/**
 * С помощью применения TransactionManager уходим от использования SELF_INJECT для сохранения транзакционности
 */
public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);

    <T> T doInReadOnlyTransaction(TransactionAction<T> action);
}
