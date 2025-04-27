package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class LightweightTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.06F;
        modified.put(ToolStats.SPD_STAT, (stats.getStats().get(ToolStats.SPD_STAT)) * mul);
        modified.put(ToolStats.MIN_STAT, stats.getStats().get(ToolStats.MIN_STAT) * mul / 2.0F);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel());
    }

    @Override
    public int getColor() {
        return 0x80F0FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.lightweight");
    }
}
