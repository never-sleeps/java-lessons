package ru.demo;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.numbers.NumbersServiceGrpc;

public class ClientGRPC {
    private static final Logger log = LoggerFactory.getLogger(ClientGRPC.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        log.info("Client is starting ...");

        var managedChannel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var numbersServiceStub = NumbersServiceGrpc.newStub(managedChannel);

        new CalculateExecutor(numbersServiceStub).execute();

        log.info("Client is shutting down...");
        managedChannel.shutdown();
    }
}
