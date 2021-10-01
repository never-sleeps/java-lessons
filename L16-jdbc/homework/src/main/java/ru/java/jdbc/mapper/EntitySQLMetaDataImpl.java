package ru.java.jdbc.mapper;

import ru.java.exception.EntitySQLMetaDataException;

import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        var fieldsString = entityClassMetaData.getAllFields().stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        return String.format("SELECT %s FROM %s", fieldsString, entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        var fieldsString = entityClassMetaData.getAllFields().stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        return String.format("SELECT %s FROM %s WHERE %s = ?",
                fieldsString,
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        checkFieldsWithoutIdExist();

        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsWithoutIdString = fieldsWithoutId.stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        String questionMarks = (fieldsWithoutId.size() == 1) ? "?" : ("?" + ", ?".repeat(fieldsWithoutId.size() - 1));

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                entityClassMetaData.getName().toLowerCase(),
                fieldsWithoutIdString,
                questionMarks);
    }

    @Override
    public String getUpdateSql() {
        checkFieldsWithoutIdExist();

        var fieldsWithoutIdString = entityClassMetaData.getFieldsWithoutId().stream()
                .map(it -> it.getName().toLowerCase() + " = ?")
                .collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName().toLowerCase(),
                fieldsWithoutIdString,
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    private void checkFieldsWithoutIdExist() {
        if (entityClassMetaData.getFieldsWithoutId().isEmpty()) {
            throw new EntitySQLMetaDataException("Class must contain at least one field");
        }
    }
}
