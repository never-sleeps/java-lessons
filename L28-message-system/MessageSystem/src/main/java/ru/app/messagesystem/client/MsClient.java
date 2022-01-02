package ru.app.messagesystem.client;

import ru.app.messagesystem.message.Message;
import ru.app.messagesystem.message.MessageType;

import java.util.List;

/**
 * Именованный клиент системы сообщений.
 * Инкапсулирует логику по созданию, отправке и обработке сообщений.
 * Внутри имеет ссылку на MessageSystem и набор обработчиков сообщений (HandlersStore)
 */
public interface MsClient {

    <T extends ResultDataType> boolean sendMessage(Message<T> msg);

    <T extends ResultDataType> void handle(Message<T> msg);

    String getName();

    <T extends ResultDataType> Message<T> produceMessage(String to, T data, MessageType msgType, MessageCallback<List<T>> callback);

    <T extends ResultDataType> Message<T> produceMessage(String to, List<T> data, MessageType msgType, MessageCallback<List<T>> callback);
}
