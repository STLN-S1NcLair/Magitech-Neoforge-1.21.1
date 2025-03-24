package net.stln.magitech.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Map2d<K1, K2, V> {
    Map<K1, Map<K2, V>> map = new HashMap<>();

    public void put(K1 k1, K2 k2, V v) {
        map.computeIfAbsent(k1, k -> new HashMap<>()).put(k2, v);
    }

    public V get(K1 k1, K2 k2) {
        return Optional.ofNullable(map.get(k1))
                .map(s -> s.get(k2))
                .orElse(null);
    }

    public V getOrDefault(K1 k1, K2 k2, V v) {
        return Optional.ofNullable(map.get(k1))
                .map(s -> s.get(k2))
                .orElse(v);
    }

    public boolean containsKey1(K1 k1) {
        return map.containsKey(k1);
    }

    public boolean containsKey2(K1 k1, K2 k2) {
        return map.containsKey(k1) && map.get(k1).containsKey(k2);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
