package com.abrenchev.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K, V> storage = new WeakHashMap<>();

    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        notifyListeners(key, value, CacheEvent.VALUE_ADDED);

        storage.put(key, value);
    }

    @Override
    public void remove(K key) {
        V value = storage.remove(key);

        notifyListeners(key, value, CacheEvent.VALUE_REMOVED);
    }

    @Override
    public V get(K key) {
        V value = storage.get(key);
        if (value == null) {
            notifyListeners(key, null, CacheEvent.CACHE_MISSED);

            return null;
        }

        notifyListeners(key, value, CacheEvent.VALUE_READ);

        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, CacheEvent event) {
        listeners.forEach(listener -> listener.notify(key, value, event));
    }
}
