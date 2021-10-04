package ru.java;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.core.datasource.DriverManagerDataSource;
import ru.java.core.executor.DbExecutorImpl;
import ru.java.jdbc.mapper.*;
import ru.java.model.Client;
import ru.java.model.Manager;
import ru.java.repository.DataTemplateJdbc;
import ru.java.service.DbServiceClientImpl;
import ru.java.service.DbServiceManagerImpl;
import ru.java.core.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class Application {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с Client
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(
                dbExecutor,
                entitySQLMetaDataClient,
                entityClassMetaDataClient
        );
        // Слой Service (непосредственно сохранение клиентов в бд и получение их из бд)
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        // Сохранение, обновление и получение клиента
        var firstClient = dbServiceClient.saveClient(new Client("John 1"));
        dbServiceClient.getClient(firstClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + firstClient.getId()));
        firstClient.setName("John 2");
        dbServiceClient.saveClient(firstClient);
        dbServiceClient.getClient(firstClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + firstClient.getId()));
        System.out.println();

        // Сохранение, обновление и получение клиента
        var secondClient = dbServiceClient.saveClient(new Client("Anna 1"));
        dbServiceClient.getClient(secondClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + secondClient.getId()));
        secondClient.setName("Anna 2");
        dbServiceClient.saveClient(secondClient);
        dbServiceClient.getClient(firstClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + firstClient.getId()));
        System.out.println();

        //--------------------------------------------------------------------------------------------
        // Работа с Manager
        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(
                dbExecutor,
                entitySQLMetaDataManager,
                entityClassMetaDataManager
        );
        // Слой Service (непосредственно сохранение клиентов в бд и получение их из бд)
        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);

        // Сохранение, обновление и получение менеджера
        var firstManager = dbServiceManager.saveManager(new Manager("ManagerFirst"));
        dbServiceManager.getManager(firstManager.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + firstManager.getNo()));
        firstManager.setParam1("param for first manager");
        dbServiceManager.saveManager(firstManager);
        dbServiceManager.getManager(firstManager.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + firstManager.getNo()));
        System.out.println();

        // Сохранение, обновление и получение менеджера
        var secondManager = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        dbServiceManager.getManager(secondManager.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + secondManager.getNo()));
        secondManager.setParam1("param for second manager");
        dbServiceManager.saveManager(secondManager);
        dbServiceManager.getManager(secondManager.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + secondManager.getNo()));
        System.out.println();
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
    }
}
