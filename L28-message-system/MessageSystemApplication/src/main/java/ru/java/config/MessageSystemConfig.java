package ru.java.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    public MessageSystemConfig(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Bean(value = "messageSystem", destroyMethod = "dispose")
    @ConditionalOnMissingBean(MessageSystem.class)
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    @DependsOn("messageSystem")
    @Qualifier("${app.dbClient}")
    public MsClient databaseClient(final MessageSystem messageSystem, @Value("${app.dbClient}") String clientName) {
        var requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_CLIENT, new SaveClientRequestHandler(dbServiceClient));
        requestHandlerDatabaseStore.addHandler(MessageType.GET_CLIENTS, new GetClientsRequestHandler(dbServiceClient));
        var databaseMsClient = new MsClientImpl(clientName, messageSystem, requestHandlerDatabaseStore);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    @DependsOn("messageSystem")
    @Qualifier("${app.responseClient}")
    public MsClient responseClient(final MessageSystem messageSystem, @Value("${app.responseClient}") String responseClientName) {
        var responseHandlerDatabaseStore = new HandlersStoreImpl();
        responseHandlerDatabaseStore.addHandler(MessageType.SAVE_CLIENT, new ResponseHandler());
        responseHandlerDatabaseStore.addHandler(MessageType.GET_CLIENTS, new ResponseHandler());
        var databaseMsClient = new MsClientImpl(responseClientName, messageSystem, responseHandlerDatabaseStore);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }
}
