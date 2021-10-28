package ru.java.service;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
