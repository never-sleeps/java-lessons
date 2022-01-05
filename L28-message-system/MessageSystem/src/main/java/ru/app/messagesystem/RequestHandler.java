package ru.app.messagesystem;

import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.Message;

import java.util.Optional;

/**
 * Обработчик сообщений
 */
public interface RequestHandler {
    <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg);
}
