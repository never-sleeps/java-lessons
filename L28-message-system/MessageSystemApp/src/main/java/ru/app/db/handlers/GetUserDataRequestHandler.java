package ru.app.db.handlers;

import ru.app.dto.UserData;
import ru.app.db.DBService;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.Message;
import ru.app.messagesystem.message.MessageBuilder;
import ru.app.messagesystem.RequestHandler;

import java.util.Optional;


public class GetUserDataRequestHandler implements RequestHandler {
    private final DBService dbService;

    public GetUserDataRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        var userData = (UserData) msg.getData().get(0);
        var data = new UserData(userData.getUserId(), dbService.getUserData(userData.getUserId()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, (T) data));
    }
}
