package ru.java.dao;

import ru.java.model.User;

import java.util.*;

public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> users;

    // симуляция базы данных
    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "John Williams", "user1", "qwerty"));
        users.put(2L, new User(2L, "Amanda Scott", "user2", "qwerty"));
        users.put(3L, new User(3L, "Nick Brown", "user3", "qwerty"));
        users.put(4L, new User(4L, "Charlotta Richardson", "user4", "qwerty"));
        users.put(5L, new User(5L, "Taylor Davies", "user5", "qwerty"));
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findRandomUser() {
        return users.values().stream()
                .skip(new Random().nextInt(users.size() - 1))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream()
                .filter(v -> v.getLogin().equals(login))
                .findFirst();
    }
}
