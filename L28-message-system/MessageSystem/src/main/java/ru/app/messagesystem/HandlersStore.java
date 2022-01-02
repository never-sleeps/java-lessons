package ru.app.messagesystem;

import ru.app.messagesystem.message.MessageType;

public interface HandlersStore {
    RequestHandler getHandlerByType(MessageType messageType);

    void addHandler(MessageType messageType, RequestHandler handler);
}
