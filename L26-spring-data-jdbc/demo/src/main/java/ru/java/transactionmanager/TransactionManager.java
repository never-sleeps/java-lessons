package ru.java.transactionmanager;

/**
 * С помощью применения TransactionManager уходим от использования SELF_INJECT для сохранения транзакционности
 */
public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
