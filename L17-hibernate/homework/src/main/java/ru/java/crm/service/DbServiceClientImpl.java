package ru.java.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.core.repository.DataTemplate;
import ru.java.core.sessionmanager.TransactionManager;
import ru.java.crm.model.ClientEntity;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

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
//                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
//            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<ClientEntity> getClient(long id) {
        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
//            log.info("received client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<ClientEntity> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
//            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
