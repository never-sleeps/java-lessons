package ru.app.messagesystem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.app.db.DBService;
import ru.app.db.handlers.GetUserDataRequestHandler;
import ru.app.front.FrontendService;
import ru.app.front.FrontendServiceImpl;
import ru.app.front.handlers.GetUserDataResponseHandler;
import ru.app.messagesystem.client.MsClient;
import ru.app.messagesystem.client.MsClientImpl;
import ru.app.messagesystem.message.Message;
import ru.app.messagesystem.message.MessageType;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private MessageSystem messageSystem;
    private FrontendService frontendService;
    private MsClient frontendMsClient;

    @DisplayName("Базовый сценарий получения данных: проверяем, что в возвращаемом значении то, что мы ожидаем")
    @RepeatedTest(1000)
    void getDataById() throws Exception {
        createMessageSystem(true);
        int counter = 3;
        CountDownLatch waitLatch = new CountDownLatch(counter);

        LongStream.range(0, counter).forEach(id ->
                frontendService.getUserData(id, data -> {
                    assertThat(data.get(0).getUserId()).isEqualTo(id);
                    waitLatch.countDown();
                }));

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Выполнение запроса после остановки сервиса: после остановки MessageSystem он больше не должен принимать сообщения")
    @RepeatedTest(2)
    void getDataAfterShutdown() throws Exception {
        createMessageSystem(true);
        messageSystem.dispose();

        CountDownLatch waitLatchShutdown = new CountDownLatch(1);

        when(frontendMsClient.sendMessage(any(Message.class))).
                thenAnswer(invocation -> {
                            waitLatchShutdown.countDown();
                            return null;
                        }
                );

        frontendService.getUserData(5, data -> logger.info("data:{}", data));
        waitLatchShutdown.await();
        boolean result = verify(frontendMsClient).sendMessage(any(Message.class));
        assertThat(result).isFalse();

        logger.info("done");
    }

    @DisplayName("Тестируем остановку работы MessageSystem: прежде чем система остановится, все сообщения должны быть обработаны")
    @RepeatedTest(1000)
    void stopMessageSystem() throws Exception {
        createMessageSystem(false); // создаем систему сразу остановленной
        int counter = 100;
        CountDownLatch messagesSentLatch = new CountDownLatch(counter);
        CountDownLatch messageSystemDisposed = new CountDownLatch(1);

        // заполняем систему сообщениями
        IntStream.range(0, counter).forEach(id -> {
                    frontendService.getUserData(id, data -> {});
                    messagesSentLatch.countDown();
                }
        );
        messagesSentLatch.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(counter); // убеждаемся, что в очереди системы есть наши сообщения

        messageSystem.start(); // запускаем систему
        // когда завершается система обмена сообщениями, срабатывает колбек, которым мы сбрасываем заглушку
        disposeMessageSystem(messageSystemDisposed::countDown);

        messageSystemDisposed.await();
        assertThat(messageSystem.currentQueueSize()).isZero(); // убеждаемся, что в очереди системы 0 сообщения

        logger.info("done");
    }


    /**
     * Для тестирования зашиваем логику, согласно которой возвращаемое значение будет эквивалентно отправляемому
     */
    private void createMessageSystem(boolean startProcessing) {
        logger.info("setup");
        messageSystem = new MessageSystemImpl(startProcessing);

        DBService dbService = mock(DBService.class);
        when(dbService.getUserData(any(Long.class)))
                .thenAnswer(invocation -> String.valueOf((Long) invocation.getArgument(0)));

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(
                MessageType.USER_DATA,
                new GetUserDataRequestHandler(dbService)
        );
        MsClient databaseMsClient = new MsClientImpl(
                DATABASE_SERVICE_CLIENT_NAME,
                messageSystem,
                requestHandlerDatabaseStore
        );
        messageSystem.addClient(databaseMsClient);

        //////////////////////////
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(
                MessageType.USER_DATA,
                new GetUserDataResponseHandler()
        );

        frontendMsClient = spy(
                new MsClientImpl(
                        FRONTEND_SERVICE_CLIENT_NAME,
                        messageSystem,
                        requestHandlerFrontendStore
                )
        );
        frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);

        logger.info("setup done");
    }

    private void disposeMessageSystem(Runnable callback) {
        try {
            messageSystem.dispose(callback);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
