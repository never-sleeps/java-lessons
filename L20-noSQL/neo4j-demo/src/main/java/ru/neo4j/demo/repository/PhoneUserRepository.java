package ru.neo4j.demo.repository;

import ru.neo4j.demo.model.PhoneUser;

import java.util.List;
import java.util.Optional;

public interface PhoneUserRepository {
    void insert(PhoneUser phoneUser);

    Optional<PhoneUser> findOne(String id);

    List<PhoneUser> findAll();
}
