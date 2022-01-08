package ru.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.protobuf.service.RealDBService;
import ru.protobuf.service.RealDBServiceImpl;
import ru.protobuf.service.RemoteDBServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        RealDBService dbService = new RealDBServiceImpl();
        RemoteDBServiceImpl remoteDBService = new RemoteDBServiceImpl(dbService);

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination(); // метод будет работать до тех пор, пока сервер будет работать. Когда в консоли ы завершим программу, сервер тоже завершится
    }
}
