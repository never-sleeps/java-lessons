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

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            logger.error("Entity class initialization exception: missing constructor", e);
            throw new EntityClassInitializationException("Entity class initialization exception: missing constructor");
        }
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(it -> !it.isAnnotationPresent(Id.class))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }
}