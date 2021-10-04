package ru.java.core.executor;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.core.executor.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Перед запуском необходимо запустить базу данных:
 * 1) open in terminal L16-jdbc/docker
 * 2) run command: ./runDb.src
 */
public class ExecutorDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) throws SQLException {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);

        try (var connection = dataSource.getConnection()) {
            var executor = new DbExecutorImpl();
            var clientId = executor.executeStatement(
                    connection,
                    "insert into client(name) values (?)",
                    Collections.singletonList("testUserName")
            );
            log.info("created client:{}", clientId);
            connection.commit();

            Optional<Client> user = executor.executeSelect(
                    connection,                                         // connection
                    "select id, name from client where id  = ?",    // sql
                    List.of(clientId),                                  // params
                    rs -> {                                             // resultSet Handler
                        try {
                            if (rs.next()) {
                                return new Client(
                                        rs.getLong("id"),
                                        rs.getString("name")
                                );
                            }
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    }
            );
            log.info("client:{}", user);
        }
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
