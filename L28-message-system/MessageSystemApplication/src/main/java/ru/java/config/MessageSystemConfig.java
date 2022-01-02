package ru.java.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import ru.java.front.handlers.GetClientsRequestHandler;
import ru.java.front.handlers.ResponseHandler;
import ru.java.front.handlers.SaveClientRequestHandler;
import ru.java.service.DBServiceClient;
import ru.app.messagesystem.HandlersStoreImpl;
import ru.app.messagesystem.MessageSystem;
import ru.app.messagesystem.MessageSystemImpl;
import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.client.MsClientImpl;
import ru.app.messagesystem.message.MessageType;

@Configuration
public class MessageSystemConfig {

    private final DBServiceClient dbServiceClient;
    public static final String DATABASE_CLIENT_NAME = "databaseClient";
    public static final String RESPONSE_CLIENT_NAME = "responseClient";

    public MessageSystemConfig(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Bean("messageSystem")
    @ConditionalOnMissingBean(MessageSystem.class)
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    @DependsOn("messageSystem")
    @Qualifier(DATABASE_CLIENT_NAME)
    public MsClient databaseClient(final MessageSystem messageSystem) {
        var requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(
                MessageType.SAVE_CLIENT,
                new SaveClientRequestHandler(dbServiceClient)
        );
        requestHandlerDatabaseStore.addHandler(
                MessageType.GET_CLIENTS,
                new GetClientsRequestHandler(dbServiceClient)
        );
        var databaseMsClient = new MsClientImpl(
                DATABASE_CLIENT_NAME,
                messageSystem,
                requestHandlerDatabaseStore);
        messageSystem.addClient(databaseMsClient
        );
        return databaseMsClient;
    }

    @Bean
    @DependsOn("messageSystem")
    @Qualifier(RESPONSE_CLIENT_NAME)
    public MsClient responseClient(final MessageSystem messageSystem) {
        var responseHandlerDatabaseStore = new HandlersStoreImpl();
        responseHandlerDatabaseStore.addHandler(
                MessageType.SAVE_CLIENT,
                new ResponseHandler()
        );
        responseHandlerDatabaseStore.addHandler(
                MessageType.GET_CLIENTS,
                new ResponseHandler()
        );
        var databaseMsClient = new MsClientImpl(
                RESPONSE_CLIENT_NAME,
                messageSystem,
                responseHandlerDatabaseStore
        );
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

}
