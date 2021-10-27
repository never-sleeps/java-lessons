package ru.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
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

/**
 *     // Стартовая страница
 *     http://localhost:8080
 */
public class ApplicationWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        executeMigrations(configuration);
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, ClientEntity.class, AddressEntity.class, PhoneEntity.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(ClientEntity.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var dbServiceClientWithCache = new DbServiceClientWithCacheProxyImpl(new MyCache<>(), dbServiceClient);

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

    private static void executeMigrations(Configuration configuration) {
        new MigrationsExecutorFlyway(
                configuration.getProperty("hibernate.connection.url"),
                configuration.getProperty("hibernate.connection.username"),
                configuration.getProperty("hibernate.connection.password")
        ).executeMigrations();
    }
}
