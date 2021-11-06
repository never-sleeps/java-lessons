package ru.java.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements Cache<K, V> {

    Logger log = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache;
    private final List<WeakReference<Listener<K, V>>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var removedValue = cache.remove(key);
        notify(key, removedValue, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(Listener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(Listener<K, V> listener) {
        listeners.removeIf(it -> listener == it.get());
    }

    private void notify(K key, V value, String action) {
        Iterator<WeakReference<Listener<K, V>>> iterator = listeners.iterator();

        while (iterator.hasNext()) {
            try {
                WeakReference<Listener<K, V>> reference = iterator.next();
                Listener<K, V> listener = reference.get();
                if (listener != null) {
                    listener.notify(key, value, action);
                } else {
                    iterator.remove();
                }
            } catch (Exception ex) {
                log.error("Listener notification error", ex);
            }
        }
    }
}
