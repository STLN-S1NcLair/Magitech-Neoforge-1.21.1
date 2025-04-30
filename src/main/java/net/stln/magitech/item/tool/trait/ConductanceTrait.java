package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class ConductanceTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.1F;
        modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel());
    }

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.1F;
        modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel());
    }

    @Override
    public int getColor() {
        return 0xF08060;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.conductance");
    }
}
