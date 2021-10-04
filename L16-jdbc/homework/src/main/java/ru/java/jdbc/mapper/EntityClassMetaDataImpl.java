package ru.java.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.java.core.Id;
import ru.java.exception.EntityClassInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private static final Logger logger = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final Class<T> clazz;
    private final Constructor<T> constructor;
    private final List<Field> fields;
    private final Field idField;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        try {
            this.clazz = clazz;
            this.constructor = clazz.getConstructor();
            this.fields = Arrays.stream(clazz.getDeclaredFields())
                    .sorted(Comparator.comparing(Field::getName))
                    .collect(Collectors.toList());
            this.idField = fields.stream()
                    .filter(it -> it.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            this.fieldsWithoutId = fields.stream()
                    .filter(it -> !it.isAnnotationPresent(Id.class))
                    .sorted(Comparator.comparing(Field::getName))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Entity class initialization exception", e);
            throw new EntityClassInitializationException("Entity class initialization exception", e);
        }
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}