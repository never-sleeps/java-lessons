package ru.neo4j.demo.repository;

import ru.neo4j.demo.model.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository {
    void insert(Phone phone);

    Optional<Phone> findOne(String id);

    List<Phone> findAll();

    List<Phone> findAllByUserId(String userId);
}
