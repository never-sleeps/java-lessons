package ru.java.hibernate.crm.service;

import ru.java.hibernate.core.repository.DataTemplate;
import ru.java.hibernate.core.sessionmanager.TransactionManager;
import ru.java.hibernate.crm.model.ClientEntity;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {

    private final DataTemplate<ClientEntity> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<ClientEntity> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public ClientEntity saveClient(ClientEntity client) {
        return transactionManager.doInTransaction(session -> {
            ClientEntity clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<ClientEntity> getClient(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            return clientOptional;
        });
    }

    @Override
    public List<ClientEntity> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            return clientList;
       });
    }
}
