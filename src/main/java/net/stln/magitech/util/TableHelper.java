package net.stln.magitech.util;

import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class TableHelper {
    public static <R, C, V> @NotNull V getOrDefault(@NotNull Table<R, C, V> table, @NotNull R row, @NotNull C column, @NotNull V defaultValue) {
        var value = table.get(row, column);
        return value == null ? defaultValue : value;
    }

    public static <R, C, V> @NotNull V getOrDefault(@NotNull Table<R, C, V> table, @NotNull R row, @NotNull C column, @NotNull BiFunction<R, C, V> function) {
        var value = table.get(row, column);
        return value == null ? function.apply(row, column) : value;
    }

    public static <R, C, V> @NotNull V computeIfAbsent(@NotNull Table<R, C, V> table, @NotNull R row, @NotNull C column, @NotNull BiFunction<R, C, V> function) {
        V value = table.get(row, column);
        if (value == null) {
            value = function.apply(row, column);
            table.put(row, column, value);
        }
        return value;
    }

    public static <R, C, V> void removeByRow(@NotNull Table<R, C, V> table, @NotNull R row) {
        Set<C> columnSet = new HashSet<>(table.columnKeySet());
        for (C column : columnSet) {
            table.remove(row, column);
        }
    }

    public static <R, C, V> void forEach(@NotNull Table<R, C, V> table, TriConsumer<R, C, V> consumer) {
        for (Table.Cell<R, C, V> cell : table.cellSet()) {
            consumer.accept(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }
}
