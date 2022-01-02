package ru.app.messagesystem;

import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.Message;

/**
 * Распределяет сообщения между адресатами. Соответственно содержит внутри себя клиентов и очередь сообщений.
 */
public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(String clientId);

    <T extends ResultDataType> boolean newMessage(Message<T> msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}

