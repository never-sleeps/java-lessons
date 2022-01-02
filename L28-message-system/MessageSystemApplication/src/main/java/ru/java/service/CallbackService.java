package ru.java.service;

import ru.app.messagesystem.client.ResultDataType;

import java.util.List;

public interface CallbackService<T extends ResultDataType> {
    void callback(T data);

    void callbackForBatch(List<T> data);
}
