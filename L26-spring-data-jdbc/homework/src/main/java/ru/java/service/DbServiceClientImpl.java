package ru.java.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.java.model.ClientEntity;
import ru.java.repository.ClientRepository;
import ru.java.transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(ClientRepository clientRepository, TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public ClientEntity saveClient(ClientEntity client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<ClientEntity> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client by id {}: {}", id, clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<ClientEntity> findAll() {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientList = clientRepository.findAll();
            log.info("findAll clients: {}", clientList.size());
            return clientList;
       });
    }
}
