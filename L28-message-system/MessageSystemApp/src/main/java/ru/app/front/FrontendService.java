package ru.app.front;

import ru.app.dto.UserData;
import ru.app.messagesystem.client.MessageCallback;

import java.util.List;

/**
 * Бизнес-прослойка между клиентом системы сообщений и фронтендом
 */
public interface FrontendService {
    void getUserData(long userId, MessageCallback<List<UserData>> dataConsumer);
}

