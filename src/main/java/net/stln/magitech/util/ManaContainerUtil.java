package net.stln.magitech.util;

import net.minecraft.network.chat.Component;

import java.util.List;

public class ManaContainerUtil {

    public static void addManaContainerItemInfo(long mana, long maxMana, long maxFlow, List<Component> components, int color, int titleColor) {
        components.add(Component.translatable("gui.magitech.mana_capacity").append(": ").withColor(titleColor).append(EnergyFormatter.formatEnergy(mana, maxMana).withColor(color)));
        components.add(Component.translatable("gui.magitech.max_flow").append(": ").withColor(titleColor).append(Component.literal(EnergyFormatter.formatValue(maxFlow) + "/t").withColor(color)));
    }

    public static void addManaContainerBlockInfo(long mana, long maxMana, long flow, long maxFlow, long production, long consumption, List<Component> components, int color, int titleColor, boolean hasProduction, boolean hasConsumption) {
        components.add(Component.translatable("gui.magitech.mana_capacity").withColor(titleColor));
        components.add(EnergyFormatter.formatEnergy(mana, maxMana).withColor(color));
        components.add(Component.translatable("gui.magitech.mana_flow").withColor(titleColor));
        components.add(EnergyFormatter.formatFlow(flow, maxFlow).withColor(color));
        if (hasProduction) {
            components.add(Component.translatable("gui.magitech.mana_production").withColor(titleColor));
            components.add(EnergyFormatter.formatAverage(production).withColor(color));
        }
        if (hasConsumption) {
            components.add(Component.translatable("gui.magitech.mana_consumption").withColor(titleColor));
            components.add(EnergyFormatter.formatAverage(consumption).withColor(color));
        }
    }
}
