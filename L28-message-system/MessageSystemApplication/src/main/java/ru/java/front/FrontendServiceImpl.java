package ru.java.front;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.java.dto.ClientDto;
import ru.java.service.CallbackService;
import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.client.ResultDataType;
import ru.app.messagesystem.message.MessageType;

import java.util.Collections;
import java.util.List;

import static ru.java.config.MessageSystemConfig.DATABASE_CLIENT_NAME;
import static ru.java.config.MessageSystemConfig.RESPONSE_CLIENT_NAME;

@Service
public class FrontendServiceImpl<T extends ResultDataType> implements FrontendService {

    @Qualifier(RESPONSE_CLIENT_NAME)
    private final MsClient responseClient;
    private final CallbackService<T> callbackService;

    public FrontendServiceImpl(MsClient responseClient, CallbackService<T> callbackService) {
        this.responseClient = responseClient;
        this.callbackService = callbackService;
    }

    @Override
    public void getAll() {
        var responseMessage = responseClient.produceMessage(
                DATABASE_CLIENT_NAME,
                Collections.singletonList(null),
                MessageType.GET_CLIENTS,
                (data) -> callbackService.callbackForBatch((List<T>) data)
        );
        responseClient.sendMessage(responseMessage);
    }

    @Override
    public void createClient(ClientDto clientDto) {
        var responseMessage = responseClient.produceMessage(
                DATABASE_CLIENT_NAME,
                Collections.singletonList(clientDto),
                MessageType.SAVE_CLIENT,
                (data) -> callbackService.callbackForBatch((List<T>) data)
        );
        responseClient.sendMessage(responseMessage);
    }

    @Override
    public void deleteClient(ClientDto clientDto) {
        var responseMessage = responseClient.produceMessage(
                DATABASE_CLIENT_NAME,
                Collections.singletonList(clientDto),
                MessageType.DELETE_CLIENT,
                (data) -> callbackService.callbackForBatch((List<T>) data)
        );
        responseClient.sendMessage(responseMessage);
    }
}
