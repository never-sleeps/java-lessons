package ru.java.jdbc.mapper;

import ru.java.exception.EntitySQLMetaDataException;

import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;
    private String selectAllSql;
    private String selectByIdSql;
    private String insertSql;
    private String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        initSelectAllSql();
        initSelectByIdSql();
        initInsertSql();
        initUpdateSql();
    }

    @Override
    public String getSelectAllSql() {
        return this.selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private void checkFieldsWithoutIdExist() {
        if (entityClassMetaData.getFieldsWithoutId().isEmpty()) {
            throw new EntitySQLMetaDataException("Class must contain at least one field");
        }
    }

    private void initSelectAllSql() {
        var fieldsString = entityClassMetaData.getAllFields().stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        this.selectAllSql = String.format("SELECT %s FROM %s",
                fieldsString,
                entityClassMetaData.getName().toLowerCase()
        );
    }

    private void initSelectByIdSql() {
        var fieldsString = entityClassMetaData.getAllFields().stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        this.selectByIdSql = String.format("SELECT %s FROM %s WHERE %s = ?",
                fieldsString,
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    private void initInsertSql() {
        checkFieldsWithoutIdExist();

        var fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        var fieldsWithoutIdString = fieldsWithoutId.stream()
                .map(it -> it.getName().toLowerCase())
                .collect(Collectors.joining(", "));

        String questionMarks = (fieldsWithoutId.size() == 1) ? "?" : ("?" + ", ?".repeat(fieldsWithoutId.size() - 1));
        this.insertSql =  String.format("INSERT INTO %s (%s) VALUES (%s)",
                entityClassMetaData.getName().toLowerCase(),
                fieldsWithoutIdString,
                questionMarks);
    }

    private void initUpdateSql() {
        checkFieldsWithoutIdExist();

        var fieldsWithoutIdString = entityClassMetaData.getFieldsWithoutId().stream()
                .map(it -> it.getName().toLowerCase() + " = ?")
                .collect(Collectors.joining(", "));

        this.updateSql =  String.format("UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName().toLowerCase(),
                fieldsWithoutIdString,
                entityClassMetaData.getIdField().getName().toLowerCase());
    }
}
