package ru.java.front;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.java.dto.ClientDto;
import ru.java.service.CallbackService;
import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.MessageType;

import java.util.Collections;
import java.util.List;

@Service
public class FrontendServiceImpl<T extends ResultDataType> implements FrontendService {

    private final MsClient responseClient;
    private final CallbackService<T> callbackService;
    private final String dbClientName;

    public FrontendServiceImpl(
            MsClient responseClient,
            CallbackService<T> callbackService,
            @Value("${app.dbClient}") String dbClient
    ) {
        this.responseClient = responseClient;
        this.callbackService = callbackService;
        this.dbClientName = dbClient;
    }

    @Override
    public void getAll() {
        var responseMessage = responseClient.produceMessage(
                dbClientName,
                Collections.singletonList(null),
                MessageType.GET_CLIENTS,
                data -> callbackService.callbackForBatch((List<T>) data)
        );
        responseClient.sendMessage(responseMessage);
    }

    @Override
    public void createClient(ClientDto clientDto) {
        var responseMessage = responseClient.produceMessage(
                dbClientName,
                Collections.singletonList(clientDto),
                MessageType.SAVE_CLIENT,
                data -> callbackService.callbackForBatch((List<T>) data)
        );
        responseClient.sendMessage(responseMessage);
    }
}
