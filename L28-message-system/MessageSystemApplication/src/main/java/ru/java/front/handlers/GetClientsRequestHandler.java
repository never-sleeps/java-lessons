package ru.java.front.handlers;

import ru.java.dto.ClientDto;
import ru.java.service.DBServiceClient;
import ru.app.messagesystem.RequestHandler;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.Message;
import ru.app.messagesystem.message.MessageBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetClientsRequestHandler implements RequestHandler {

    private final DBServiceClient dbServiceClient;

    public GetClientsRequestHandler(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public <T extends ResultDataType> Optional<Message<T>> handle(Message<T> msg) {
        List<ClientDto> clients = dbServiceClient.findAll()
                .stream()
                .map(ClientDto::new)
                .collect(Collectors.toList());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, (List<T>) clients));
    }
}