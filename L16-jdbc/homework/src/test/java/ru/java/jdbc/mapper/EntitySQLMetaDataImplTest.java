package ru.java.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.java.model.Client;

class EntitySQLMetaDataImplTest {

    private EntitySQLMetaData entitySQLMetaData;

    @BeforeEach
    void setUp() {
        EntityClassMetaDataImpl<Client> entityClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
        entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Test
    void getSelectAllSq_shouldReturnQuery() {
        // when
        String result = entitySQLMetaData.getSelectAllSql();

        // then
        Assertions.assertEquals("SELECT id, name FROM client", result);
    }

    @Test
    void getSelectByIdSql_shouldReturnQuery() {
        // when
        String result = entitySQLMetaData.getSelectByIdSql();

        // then
        Assertions.assertEquals("SELECT id, name FROM client WHERE id = ?", result);
    }

    @Test
    void getInsertSql_shouldReturnQuery() {
        // when
        String result = entitySQLMetaData.getInsertSql();

        // then
        Assertions.assertEquals("INSERT INTO client (name) VALUES (?)", result);
    }

    @Test
    void getUpdateSql_shouldReturnQuery() {
        // when
        String result = entitySQLMetaData.getUpdateSql();

        // then
        Assertions.assertEquals("UPDATE client SET name = ? WHERE id = ?", result);
    }
}