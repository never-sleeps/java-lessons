package ru.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.java.server.UsersWebServer;
import ru.java.server.UsersWebServerWithFilterBasedSecurity;
import ru.java.service.TemplateProcessor;
import ru.java.service.TemplateProcessorImpl;
import ru.java.service.UserAuthService;
import ru.java.service.UserAuthServiceImpl;

import java.util.Collections;

/**
 *     // Стартовая страница
 *     http://localhost:8080
 */
public class ApplicationWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final Logger log = LoggerFactory.getLogger(ApplicationWithFilterBasedSecurityDemo.class);

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigrations(configuration);
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, ClientEntity.class, AddressEntity.class, PhoneEntity.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(ClientEntity.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var dbServiceClientWithCache = new DbServiceClientWithCacheProxyImpl(new MyCache<>(), dbServiceClient);
        initDataBase(dbServiceClientWithCache);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceClientWithCache);
        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT,
                gson,
                templateProcessor,
                authService,
                dbServiceClientWithCache
        );

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void initDataBase(DbServiceClientWithCacheProxyImpl dbServiceClientWithCache) {
        var client1 = new ClientEntity(
                "user1",
                "password1",
                new AddressEntity("London", "Trafalgar Square", "11/2/34"),
                Collections.singletonList(new PhoneEntity("111-222"))
        );
        var client2 = new ClientEntity(
                "user2",
                "password2",
                new AddressEntity("Moscow", "Arbat", "1"),
                Collections.singletonList(new PhoneEntity("22-33-44"))
        );
        var client3 = new ClientEntity(
                "user3",
                "password3",
                new AddressEntity("NY", "Valley Drive Woodside", "11377"),
                Collections.singletonList(new PhoneEntity("5-6-7-88"))
        );
        dbServiceClientWithCache.saveClient(client1);
        dbServiceClientWithCache.saveClient(client2);
        dbServiceClientWithCache.saveClient(client3);

        var allClients = dbServiceClientWithCache.findAll();
        log.info("findAll(): Clients: {}", allClients.size());
    }

    private static void executeMigrations(Configuration configuration) {
        new MigrationsExecutorFlyway(
                configuration.getProperty("hibernate.connection.url"),
                configuration.getProperty("hibernate.connection.username"),
                configuration.getProperty("hibernate.connection.password")
        ).executeMigrations();
    }
}
