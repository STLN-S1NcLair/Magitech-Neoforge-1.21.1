package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;

public class ToolPropertyHelper {

    // 計算不能な値はaを継承
    public static ToolProperties simpleAdd(ToolProperties a, ToolProperties b) {
        ToolCategory group = a.getGroup();
        if (group != b.getGroup()) {
            group = ToolCategoryInit.ALL.get();
        }
        ToolProperties result = new ToolProperties(group);
        for (ToolPropertyLike<?> key : group.keys()) {
            if (key.asToolProperty() instanceof CalculableToolProperty<?> calKey) {
                addAndSet(calKey, a, b, result);
            }
        }
        return result;
    }

    private static <T> void addAndSet(CalculableToolProperty<T> calKey, ToolProperties a, ToolProperties b, ToolProperties result) {
        T valA = a.getOrDefault(calKey, calKey.identity());
        T valB = b.getOrDefault(calKey, calKey.identity());

        // calKey自体に計算ロジックがある
        T summedValue = calKey.add(valA, valB);

        result.set(calKey, summedValue);
    }

    // 計算不能な値はaを継承
    public static ToolProperties simpleMul(ToolProperties a, ToolProperties b) {
        ToolCategory group = a.getGroup();
        if (group != b.getGroup()) {
            group = ToolCategoryInit.ALL.get();
        }
        ToolProperties result = new ToolProperties(group);
        for (ToolPropertyLike<?> key : group.keys()) {
            if (key.asToolProperty() instanceof CalculableToolProperty<?> calKey) {
                mulAndSet(calKey, a, b, result);
            }
        }
        return result;
    }

    private static <T> void mulAndSet(CalculableToolProperty<T> calKey, ToolProperties a, ToolProperties b, ToolProperties result) {
        T valA = a.getOrDefault(calKey, calKey.identity());
        T valB = b.getOrDefault(calKey, calKey.identity());

        // calKey自体に計算ロジックがある
        T summedValue = calKey.add(valA, valB);

        result.set(calKey, summedValue);
    }

    // 計算不能な値はaを継承
    public static ToolProperties scalarMul(ToolProperties a, float b) {
        ToolCategory group = a.getGroup();
        ToolProperties result = new ToolProperties(group);
        for (ToolPropertyLike<?> key : group.keys()) {
            if (key.asToolProperty() instanceof CalculableToolProperty<?> calKey) {
                scalarMulAndSet(calKey, a, b, result);
            }
        }
        return result;
    }

    private static <T> void scalarMulAndSet(CalculableToolProperty<T> calKey, ToolProperties a, float b, ToolProperties result) {
        T valA = a.getOrDefault(calKey, calKey.identity());

        // calKey自体に計算ロジックがある
        T summedValue = calKey.scalarMul(valA, b);

        result.set(calKey, summedValue);
    }

    public static MutableComponent getToolTipComponent(ToolProperty<?> toolProperty) {
        return toolProperty.getDisplayName().withColor(0xa0a0a0).append(": ");
    }
}
