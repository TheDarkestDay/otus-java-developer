package com.abrenchev.cachehw;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K, SoftReference<V>> storage = new WeakHashMap<>();

    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        notifyListeners(key, value, CacheEvent.VALUE_ADDED);

        storage.put(key, new SoftReference<>(value));
    }

    @Override
    public void remove(K key) {
        SoftReference<V> valueRef = storage.remove(key);

        notifyListeners(key, valueRef.get(), CacheEvent.VALUE_REMOVED);
    }

    @Override
    public V get(K key) {
        SoftReference<V> valueRef = storage.get(key);
        if (valueRef == null) {
            notifyListeners(key, null, CacheEvent.CACHE_MISSED);

            return null;
        }

        V value = valueRef.get();

        notifyListeners(key, value, CacheEvent.VALUE_READ);

        if (value == null) {
            remove(key);
        }

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
