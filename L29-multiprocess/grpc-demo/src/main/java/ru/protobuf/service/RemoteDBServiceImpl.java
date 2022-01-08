package ru.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.protobuf.generated.Empty;
import ru.protobuf.generated.RemoteDBServiceGrpc;
import ru.protobuf.generated.UserMessage;
import ru.protobuf.model.User;

import java.util.List;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    private final RealDBService realDBService;

    public RemoteDBServiceImpl(RealDBService realDBService) {
        this.realDBService = realDBService;
    }

    @Override
    public void saveUser(UserMessage request, StreamObserver<UserMessage> responseObserver) {
        User user = realDBService.saveUser(request.getFirstName(), request.getLastName());
        UserMessage response = mapUserToUserMessage(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findAllUsers(Empty request, StreamObserver<UserMessage> responseObserver) {
        List<User> allUsers = realDBService.findAllUsers();
        allUsers.forEach(user -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UserMessage response = mapUserToUserMessage(user);
            responseObserver.onNext(response);
        });
        responseObserver.onCompleted();
    }

    private UserMessage mapUserToUserMessage(User user) {
        return UserMessage.newBuilder()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .build();
    }
}
