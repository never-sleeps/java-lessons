package ru.redis.demo.template;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RedisTemplateImpl implements RedisTemplate {

    private final Jedis jedis;
    private final Gson mapper;

    public RedisTemplateImpl(Jedis jedis, Gson mapper) {
        this.jedis = jedis;
        this.mapper = mapper;
    }

    private String buildKey(Class<?> tClass, String id) {
        return String.format("%s:%s", tClass.getName(), id);
    }

    @Override
    public <T> void insert(String id, T value) {
        var key = buildKey(value.getClass(), id);
        jedis.set(key, mapper.toJson(value));
    }

    @Override
    public <T> Optional<T> findOne(String id, Class<T> tClass) {
        var key = buildKey(tClass, id);
        return Optional.ofNullable(jedis.get(key)).map(v -> mapper.fromJson(v, tClass));

    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) {
        var keys = jedis.keys(String.format("%s:*", tClass.getName()));
        var values = jedis.mget(keys.toArray(new String[0]));
        return values.stream().map(s -> mapper.fromJson(s, tClass)).collect(Collectors.toList());
    }
}
