package ru.java.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
