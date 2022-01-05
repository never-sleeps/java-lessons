package ru.app.front;

import ru.app.dto.UserData;
import ru.app.messagesystem.client.MessageCallback;
import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.message.MessageType;

import java.util.List;

public record FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) implements FrontendService {

    @Override
    public void getUserData(long userId, MessageCallback<List<UserData>> dataConsumer) {
        var outMsg = msClient.produceMessage(
                databaseServiceClientName,
                new UserData(userId),
                MessageType.USER_DATA,
                dataConsumer
        );
        msClient.sendMessage(outMsg);
    }
}
