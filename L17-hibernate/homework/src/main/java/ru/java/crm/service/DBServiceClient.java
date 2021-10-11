package ru.java.crm.service;

import ru.java.crm.model.ClientEntity;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    ClientEntity saveClient(ClientEntity client);

    Optional<ClientEntity> getClient(long id);

    List<ClientEntity> findAll();
}
