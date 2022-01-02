package ru.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.app.db.handlers.GetUserDataRequestHandler;
import ru.app.front.FrontendServiceImpl;
import ru.app.db.DBServiceImpl;
import ru.app.front.handlers.GetUserDataResponseHandler;
import ru.app.messagesystem.HandlersStoreImpl;
import ru.app.messagesystem.MessageSystemImpl;
import ru.app.messagesystem.message.MessageType;
import ru.app.messagesystem.client.MsClientImpl;

public class MSMain {
    private static final Logger logger = LoggerFactory.getLogger(MSMain.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static void main(String[] args) throws InterruptedException {
        var messageSystem = new MessageSystemImpl();

        var requestHandlerDatabaseStore = new HandlersStoreImpl(); // создаём хранилище обработчиков
        // добавляем в хранилище обработчик сообщения с типом USER_DATA
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(new DBServiceImpl()));
        var databaseMsClient = new MsClientImpl(
                DATABASE_SERVICE_CLIENT_NAME,
                messageSystem,
                requestHandlerDatabaseStore
        );
        messageSystem.addClient(databaseMsClient); // добавляем клиента в систему обменя сообщениями


        var requestHandlerFrontendStore = new HandlersStoreImpl();
        // добавляем в хранилище обработчик сообщения с типом USER_DATA
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler());
        var frontendMsClient = new MsClientImpl(
                FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem,
                requestHandlerFrontendStore
        );
        var frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);


        frontendService.getUserData(1, data -> logger.info("got data:{}", data));
        frontendService.getUserData(2, data -> logger.info("got data:{}", data));

        Thread.sleep(100);
        messageSystem.dispose();
        logger.info("done");
    }
}
