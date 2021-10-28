package ru.java.service;

import ru.java.hibernate.crm.service.DBServiceClient;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceClient dbServiceClient;

    public UserAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient.findAll().stream()
                .filter(it -> it.getLogin().equals(login))
                .findFirst()
                .map(client -> client.getPassword().equals(password))
                .orElse(false);
    }
}
