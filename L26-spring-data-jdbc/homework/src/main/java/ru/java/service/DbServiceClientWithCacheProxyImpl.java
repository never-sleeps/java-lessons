package ru.java.service;

import ru.java.cache.Cache;
import ru.java.cache.Listener;
import ru.java.model.ClientEntity;

import java.util.List;
import java.util.Optional;

/**
 * Класс-прокси на DbServiceClientImpl.
 * Добавлен функционал работы с кэшем
 */
public class DbServiceClientWithCacheProxyImpl implements DBServiceClient {

    private final Cache<String, ClientEntity> cache;
    private final DBServiceClient dbServiceClient;

    public DbServiceClientWithCacheProxyImpl(Cache<String, ClientEntity> cache, DBServiceClient dbServiceClient) {
        this.cache = cache;
        this.dbServiceClient = dbServiceClient;
    }

    public DbServiceClientWithCacheProxyImpl(
            Cache<String, ClientEntity> cache,
            DBServiceClient dbServiceClient,
            Listener<String, ClientEntity>... cacheListeners
    ) {
        this(cache, dbServiceClient);
        for (var listener : cacheListeners) {
            this.cache.addListener(listener);
        }
    }

    @Override
    public ClientEntity saveClient(ClientEntity client) {
        var savedClient = dbServiceClient.saveClient(client);
        cache.put(String.valueOf(savedClient.getId()), savedClient);
        return savedClient;
    }

    @Override
    public Optional<ClientEntity> getClient(long id) {
        var key = String.valueOf(id);
        var valueFromCache = cache.get(key);
        if (valueFromCache == null) {
            var client = dbServiceClient.getClient(id);
            client.ifPresent(value -> cache.put(key, value));
            return client;
        }
        return Optional.of(valueFromCache);
    }

    @Override
    public List<ClientEntity> findAll() {
        var clients = dbServiceClient.findAll();
        clients.forEach(client -> cache.put(String.valueOf(client.getId()), client));
        return clients;
    }
}
