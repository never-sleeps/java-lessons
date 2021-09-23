package ru.java.handler;

import ru.java.model.Message;
import ru.java.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
