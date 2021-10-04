package ru.java.crm;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.crm.model.Client;
import ru.java.crm.service.DbServiceClientImpl;
import ru.java.crm.datasource.DriverManagerDataSource;
import ru.java.crm.repository.executor.DbExecutorImpl;
import ru.java.crm.repository.ClientDataTemplateJdbc;
import ru.java.crm.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class DbServiceDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);

        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var clientTemplate = new ClientDataTemplateJdbc(dbExecutor); //реализация DataTemplate, заточена на Client
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, clientTemplate);

        /* Сохранение клиента в бд и поиск его по id */
        var clientFirst = dbServiceClient.saveClient(new Client("John"));
        var clientFirstSelected = dbServiceClient.getClient(clientFirst.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientFirst.getId()));
        log.info("client:{}", clientFirstSelected);

        /* Сохранение клиента в бд и поиск его по id */
        var clientSecond = dbServiceClient.saveClient(new Client("Anna"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("client:{}", clientSecondSelected);

        /* Обновление данных клиента и поиск его по id */
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "Stephani"));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        /* Получение списка всех клиентов */
        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
        /*
            ru.java.crm.service.DbServiceClientImpl - clientList:[Client{id=1, name='John'}, Client{id=2, name='Stephani'}, Client{id=3, name='John'}, Client{id=4, name='Stephani'}]
            ru.java.crm.DbServiceDemo - client:Client{id=1, name='John'}
            ru.java.crm.DbServiceDemo - client:Client{id=2, name='Stephani'}
            ru.java.crm.DbServiceDemo - client:Client{id=3, name='John'}
            ru.java.crm.DbServiceDemo - client:Client{id=4, name='Stephani'}
         */
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
