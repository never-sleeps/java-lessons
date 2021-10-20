package ru.cassandra.demo.schema;

public interface CassandraSchemaInitializer {
    void initSchema();
    void dropSchemaIfExists();
}
