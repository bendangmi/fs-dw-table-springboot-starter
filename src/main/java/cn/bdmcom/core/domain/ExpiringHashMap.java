package cn.bdmcom.core.domain;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 轻量级过期缓存（基于内存）。
 *
 * <p>适用于低并发场景的短期缓存，过期后按访问触发清理。</p>
 */
public class ExpiringHashMap<K, V> {
    private final ConcurrentHashMap<K, ExpiringValue<V>> map = new ConcurrentHashMap<>();

    /**
     * 添加元素并设置过期时间（毫秒）。
     */
    public void put(K key, V value, long expirationMillis) {
        long expirationTime = System.currentTimeMillis() + expirationMillis;
        ExpiringValue<V> expiringValue = new ExpiringValue<>(value, expirationTime);
        map.put(key, expiringValue);
    }

    /**
     * 获取元素（若已过期则移除并返回 null）。
     */
    public V get(K key) {
        ExpiringValue<V> expiringValue = map.get(key);
        if (expiringValue != null && !expiringValue.isExpired()) {
            return expiringValue.getValue();
        } else {
            map.remove(key);
            return null;
        }
    }

    /**
     * 内部包装类：保存过期时间。
     */
    private static class ExpiringValue<V> {
        private final V value;
        private final long expirationTime;

        ExpiringValue(V value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        V getValue() {
            return value;
        }

        boolean isExpired() {
            return System.currentTimeMillis() >= expirationTime;
        }
    }
}
