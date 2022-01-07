package ru.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.protobuf.generated.Empty;
import ru.protobuf.generated.RemoteDBServiceGrpc;
import ru.protobuf.generated.UserMessage;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        RemoteDBServiceGrpc.RemoteDBServiceBlockingStub stub = RemoteDBServiceGrpc.newBlockingStub(channel);

        UserMessage savedUser = stub.saveUser(
                UserMessage.newBuilder().setFirstName("Arya").setLastName("Stark").build()
        );
        System.out.printf(
                "We saved new user with name 'Arya': {id: %d, name: %s %s}%n",
                savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName()
        );

        Iterator<UserMessage> allUsersIterator = stub.findAllUsers(Empty.getDefaultInstance());
        System.out.println("Great! We have received user data. Arya should be among them!");
        allUsersIterator.forEachRemaining(um ->
                System.out.printf(
                        "{id: %d, name: %s %s}%n",
                        um.getId(), um.getFirstName(), um.getLastName()
                )
        );

        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("\nAnd now the same thing, only asynchronously:");
        RemoteDBServiceGrpc.RemoteDBServiceStub newStub = RemoteDBServiceGrpc.newStub(channel);
        newStub.findAllUsers(Empty.getDefaultInstance(), new StreamObserver<>() {
            @Override
            public void onNext(UserMessage um) {
                System.out.printf(
                        "{id: %d, name: %s %s}%n",
                        um.getId(), um.getFirstName(), um.getLastName()
                );
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("\nend of work!");
                latch.countDown();
            }
        });

        latch.await();

        channel.shutdown();
    }
}
