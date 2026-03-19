package net.stln.magitech.feature.tool.property;

import net.stln.magitech.feature.tool.tool_group.ToolGroup;

public class ToolPropertyHelper {

    // 計算不能な値はaを継承
    public static ToolProperties simpleAdd(ToolProperties a, ToolProperties b) {
        ToolGroup group = a.getGroup();
        if (group != b.getGroup()) {
            throw new IllegalArgumentException("These two ToolProperties must have the same ToolGroup");
        }
        ToolProperties result = new ToolProperties(group);
        for (ToolProperty<?> key : group.keys()) {
            if (key instanceof CalculableToolProperty<?> calKey) {
                addAndSet(calKey, a, b, result);
            }
        }
        return result;
    }

    private static <T> void addAndSet(CalculableToolProperty<T> calKey, ToolProperties a, ToolProperties b, ToolProperties result) {
        T valA = a.get(calKey);
        T valB = b.get(calKey);

        // calKey自体に計算ロジックがある
        T summedValue = calKey.add(valA, valB);

        result.set(calKey, summedValue);
    }

    // 計算不能な値はaを継承
    public static ToolProperties simpleMul(ToolProperties a, ToolProperties b) {
        ToolGroup group = a.getGroup();
        if (group != b.getGroup()) {
            throw new IllegalArgumentException("These two ToolProperties must have the same ToolGroup");
        }
        ToolProperties result = new ToolProperties(group);
        for (ToolProperty<?> key : group.keys()) {
            if (key instanceof CalculableToolProperty<?> calKey) {
                mulAndSet(calKey, a, b, result);
            }
        }
        return result;
    }

    private static <T> void mulAndSet(CalculableToolProperty<T> calKey, ToolProperties a, ToolProperties b, ToolProperties result) {
        T valA = a.get(calKey);
        T valB = b.get(calKey);

        // calKey自体に計算ロジックがある
        T summedValue = calKey.add(valA, valB);

        result.set(calKey, summedValue);
    }
}
