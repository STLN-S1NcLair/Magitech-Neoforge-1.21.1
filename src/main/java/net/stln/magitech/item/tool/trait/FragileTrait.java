package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class FragileTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.3F;
        float div = 0.5F / traitLevel - 1.0F;
        modified.put(ToolStats.ATK_STAT, stats.getStats().get(ToolStats.ATK_STAT) * mul);
        modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.SPD_STAT, stats.getStats().get(ToolStats.SPD_STAT) * mul);
        modified.put(ToolStats.MIN_STAT, stats.getStats().get(ToolStats.MIN_STAT) * mul);
        modified.put(ToolStats.DEF_STAT, stats.getStats().get(ToolStats.DEF_STAT) * mul);
        modified.put(ToolStats.RNG_STAT, stats.getStats().get(ToolStats.RNG_STAT) * mul);
        modified.put(ToolStats.SWP_STAT, stats.getStats().get(ToolStats.SWP_STAT) * mul);
        modified.put(ToolStats.DUR_STAT, stats.getStats().get(ToolStats.DUR_STAT) * div);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.3F;
        float div = 0.5F / traitLevel - 1.0F;
        modified.put(ToolStats.ATK_STAT, stats.getStats().get(ToolStats.ATK_STAT) * mul);
        modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.SPD_STAT, stats.getStats().get(ToolStats.SPD_STAT) * mul);
        modified.put(ToolStats.MIN_STAT, stats.getStats().get(ToolStats.MIN_STAT) * mul);
        modified.put(ToolStats.DEF_STAT, stats.getStats().get(ToolStats.DEF_STAT) * mul);
        modified.put(ToolStats.RNG_STAT, stats.getStats().get(ToolStats.RNG_STAT) * mul);
        modified.put(ToolStats.SWP_STAT, stats.getStats().get(ToolStats.SWP_STAT) * mul);
        modified.put(ToolStats.DUR_STAT, stats.getStats().get(ToolStats.DUR_STAT) * div);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public int getColor() {
        return 0xC0E8FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.fragile");
    }
}
