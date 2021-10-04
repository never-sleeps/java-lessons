package ru.java.repository;

import ru.java.core.executor.DbExecutor;
import ru.java.exception.DataTemplateException;
import ru.java.jdbc.mapper.EntityClassMetaData;
import ru.java.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<?> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return createObject(resultSet);
                        }
                        return null;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                resultSet -> {
                    var clientList = new ArrayList<T>();
                    try {
                        while (resultSet.next()) {
                            clientList.add(createObject(resultSet));
                        }
                        return clientList;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }
        ).orElseThrow(() -> new DataTemplateException("Unexpected database error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            var objectFieldsValues = extractObjectFieldsValues(object);
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    objectFieldsValues
            );
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            var objectFieldsValues = extractObjectFieldsValues(object);
            objectFieldsValues.add(extractObjectIdFieldValue(object));
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    objectFieldsValues
            );
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T createObject(java.sql.ResultSet resultSet) throws IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        var instance = entityClassMetaData.getConstructor().newInstance();

        for (Field field : entityClassMetaData.getAllFields() ) {
            var value = resultSet.getObject(field.getName());
            field.setAccessible(true);
            field.set(instance, value);
        }
        return (T) instance;
    }

    private List<Object> extractObjectFieldsValues(T object) throws IllegalAccessException {
        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var objectFieldsValues = new ArrayList<>();

        for (Field field : fieldsWithoutId) {
            field.setAccessible(true);
            objectFieldsValues.add(field.get(object));
        }
        return objectFieldsValues;
    }

    private Object extractObjectIdFieldValue(T object) throws IllegalAccessException {
        var idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        return idField.get(object);
    }
}
