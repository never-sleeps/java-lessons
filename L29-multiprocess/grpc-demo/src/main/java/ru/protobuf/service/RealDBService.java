package ru.protobuf.service;

import ru.protobuf.model.User;

import java.util.List;

public interface RealDBService {
    User saveUser(String firstName, String lastName);
    List<User> findAllUsers();
}
