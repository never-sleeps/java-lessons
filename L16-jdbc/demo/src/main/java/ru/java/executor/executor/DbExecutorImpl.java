package ru.java.executor.executor;

import ru.java.executor.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public long executeStatement(Connection connection, String sql, List<Object> params) {
        try (var prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var idx = 0; idx < params.size(); idx++) {
                prepareStatement.setObject(idx + 1, params.get(idx));
            }
            prepareStatement.executeUpdate();
            try (var resultSet = prepareStatement.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeInsert error", ex);
        }
    }

    @Override
    public <T> Optional<T> executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) {
        try (var prepareStatement = connection.prepareStatement(sql)) {
            for (var idx = 0; idx < params.size(); idx++) {
                prepareStatement.setObject(idx + 1, params.get(idx));
            }
            try (var resultSet = prepareStatement.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeSelect error", ex);
        }
    }
}
