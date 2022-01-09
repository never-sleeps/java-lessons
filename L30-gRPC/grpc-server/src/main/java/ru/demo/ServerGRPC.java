package ru.demo;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerGRPC {
    private static final Logger log = LoggerFactory.getLogger(ServerGRPC.class);

    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Server is starting ...");

        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumbersServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            log.info("Received shutdown request");
            server.shutdown();
            log.info("Server stopped");
        }));

        log.info("Server is waiting for client, port:{}", SERVER_PORT);
        server.awaitTermination();
    }
}
