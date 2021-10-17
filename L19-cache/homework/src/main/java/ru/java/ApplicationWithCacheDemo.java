package ru.java;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.cache.Listener;
import ru.java.cache.MyCache;
import ru.java.hibernate.core.repository.DataTemplateHibernate;
import ru.java.hibernate.core.repository.HibernateUtils;
import ru.java.hibernate.core.sessionmanager.TransactionManagerHibernate;
import ru.java.hibernate.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.java.hibernate.crm.model.AddressEntity;
import ru.java.hibernate.crm.model.ClientEntity;
import ru.java.hibernate.crm.model.PhoneEntity;
import ru.java.hibernate.crm.service.DbServiceClientImpl;
import ru.java.hibernate.crm.service.DbServiceClientWithCacheProxyImpl;

import java.util.Arrays;

public class ApplicationWithCacheDemo {

    private static final Logger log = LoggerFactory.getLogger(ApplicationWithCacheDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigrations(configuration);
        var sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                ClientEntity.class,
                AddressEntity.class,
                PhoneEntity.class
        );
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(ClientEntity.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        AddressEntity address = new AddressEntity("012", "London", "Trafalgar Square", "11/2/34");
        PhoneEntity phone1 = new PhoneEntity("123456789");
        PhoneEntity phone2 = new PhoneEntity("987654321");
        var client1 = new ClientEntity("Anna Smith", address, Arrays.asList(phone1, phone2));
        var client2 = new ClientEntity("Alexandr Smith", address, Arrays.asList(phone1, phone2));
        var client3 = new ClientEntity("Helena Miller", address, Arrays.asList(phone1));
        var client4 = new ClientEntity("Victor Windzor", address, Arrays.asList(phone1, phone2));
        var client5 = new ClientEntity("Harry Scott", address, Arrays.asList(phone1));

        var savedClient1 = dbServiceClient.saveClient(client1);
        var savedClient2 = dbServiceClient.saveClient(client2);
        var savedClient3 = dbServiceClient.saveClient(client3);
        var savedClient4 = dbServiceClient.saveClient(client4);
        var savedClient5 = dbServiceClient.saveClient(client5);



        log.info("-------------Without cache-------------");
        long startTimeWithoutCache = System.nanoTime();
        var receivedClient = dbServiceClient.getClient(savedClient1.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient1.getId()));
        long endTimeWithoutCache = System.nanoTime();
        double totalSeconds = (endTimeWithoutCache - startTimeWithoutCache) / 1_000_000_000.0;
        log.info("getClient() without cache: Client: {}, Time: {}", receivedClient.getName(), totalSeconds);
        // getClient() without cache: Client: Anna Smith, Time: 0.02313528

        startTimeWithoutCache = System.nanoTime();
        var allClients = dbServiceClient.findAll();
        endTimeWithoutCache = System.nanoTime();
        totalSeconds = (endTimeWithoutCache - startTimeWithoutCache) / 1_000_000_000.0;
        log.info("findAll() without cache: Clients: {}, Time: {}", allClients.size(), totalSeconds);
        // findAll() without cache: Clients: 5, Time: 0.121648648


        log.info("-------------With cache-------------");
        Listener<String, ClientEntity> listener = new Listener<String, ClientEntity>() {
            @Override
            public void notify(String key, ClientEntity value, String action) {
                log.info("key:{}, clientId:{}, action: {}", key, value == null ? null : value.getId(), action);
            }
        };
        var dbServiceClientWithCache = new DbServiceClientWithCacheProxyImpl(new MyCache<>(), dbServiceClient, listener);
        startTimeWithoutCache = System.nanoTime();
        var receivedClient2 = dbServiceClientWithCache.getClient(savedClient1.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient1.getId()));
        endTimeWithoutCache = System.nanoTime();
        totalSeconds = (endTimeWithoutCache - startTimeWithoutCache) / 1_000_000_000.0;
        log.info("getClient() with cache: Client: {}, Time: {}", receivedClient2.getName(), totalSeconds);
        // getClient() with cache: Client: Anna Smith, Time: 0.005794738

        startTimeWithoutCache = System.nanoTime();
        var allClients2 = dbServiceClientWithCache.findAll();
        endTimeWithoutCache = System.nanoTime();
        totalSeconds = (endTimeWithoutCache - startTimeWithoutCache) / 1_000_000_000.0;
        log.info("findAll() with cache: Clients: {}, Time: {}", allClients2.size(), totalSeconds);
        // findAll() with cache: Clients: 5, Time: 0.026917336
    }

    private static void executeMigrations(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
