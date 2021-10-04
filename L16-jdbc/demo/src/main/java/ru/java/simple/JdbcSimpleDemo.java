package ru.java.simple;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Перед запуском необходимо запустить базу данных:
 * 1) open in terminal L16-jdbc/docker
 * 2) run command: ./runDb.src
 */
public class JdbcSimpleDemo {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger logger = LoggerFactory.getLogger(JdbcSimpleDemo.class);

    public static void main(String[] args) throws SQLException {
        flywayMigrations();

        var demo = new JdbcSimpleDemo();
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false); // для явного управления транзакциями
            var id = 1;
            demo.insertRecord(connection, id);
            demo.selectRecord(connection, id);
        }
    }

    private void insertRecord(Connection connection, int id) throws SQLException {
        try (var prepareStatement = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            var savePoint = connection.setSavepoint("savePointName");
            prepareStatement.setInt(1, id);
            prepareStatement.setString(2, "NameValue");
            try {
                var rowCount = prepareStatement.executeUpdate(); //Блокирующий вызов
                connection.commit();
                logger.info("inserted rowCount: {}", rowCount);
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void selectRecord(Connection connection, int id) throws SQLException {
        try (var prepareStatement = connection.prepareStatement("select name from test where id  = ?")) {
            prepareStatement.setInt(1, id);
            try (var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    var name = resultSet.getString("name");
                    logger.info("name:{}", name);
                }
            }
        }
    }

    private static void flywayMigrations() {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
