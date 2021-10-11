package ru.java.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.java.core.repository.DataTemplateHibernate;
import ru.java.core.repository.HibernateUtils;
import ru.java.core.sessionmanager.TransactionManagerHibernate;
import ru.java.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.java.crm.model.AddressEntity;
import ru.java.crm.model.ClientEntity;
import ru.java.crm.model.PhoneEntity;
import ru.java.crm.service.DBServiceClient;
import ru.java.crm.service.DbServiceClientImpl;

import static ru.java.ApplicationDemo.HIBERNATE_CFG_FILE;


public abstract class AbstractHibernateTest {
    protected SessionFactory sessionFactory;
    protected TransactionManagerHibernate transactionManager;
    protected DataTemplateHibernate<ClientEntity> clientTemplate;
    protected DBServiceClient dbServiceClient;

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(
                configuration,
                ClientEntity.class,
                AddressEntity.class,
                PhoneEntity.class
        );

        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(ClientEntity.class);
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(ClientEntity.class.getName());
    }
}
