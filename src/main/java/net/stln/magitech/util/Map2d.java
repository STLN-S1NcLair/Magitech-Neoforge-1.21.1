package net.stln.magitech.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

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

    public Map<K2, V> get(K1 k1) {
        return map.get(k1);
    }

    public V getOrDefault(K1 k1, K2 k2, V v) {
        return Optional.ofNullable(map.get(k1))
                .map(s -> s.get(k2))
                .orElse(v);
    }

    public V remove(K1 k1, K2 k2) {
        Map<K2, V> inner = map.get(k1);
        if (inner == null) return null;
        V removed = inner.remove(k2);
        // 中のMapが空になったら第一キーごと削除（クリーンアップ）
        if (inner.isEmpty()) {
            map.remove(k1);
        }
        return removed;
    }

    public Map<K2, V> remove(K1 k1) {
        return map.remove(k1);
    }

    public void forEach(TriConsumer<? super K1, ? super K2, ? super V> action) {
        for (Map.Entry<K1, Map<K2, V>> entry1 : map.entrySet()) {
            K1 k1 = entry1.getKey();
            for (Map.Entry<K2, V> entry2 : entry1.getValue().entrySet()) {
                K2 k2 = entry2.getKey();
                V v = entry2.getValue();
                action.accept(k1, k2, v);
            }
        }
    }

    public void forEach(K1 k1, BiConsumer<? super K2, ? super V> action) {
        Map<K2, V> inner = map.get(k1);
        if (inner != null) {
            inner.forEach(action);
        }
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

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
