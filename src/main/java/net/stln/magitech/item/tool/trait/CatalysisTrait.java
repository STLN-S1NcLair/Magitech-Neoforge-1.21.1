package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class CatalysisTrait extends Trait {

    @Override
    public ToolStats modifyStats2(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats2(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.25F;
        Float elmAtk = stats.getStats().get(ToolStats.ELM_ATK_STAT);
        modified.put(ToolStats.ATK_STAT, elmAtk * mul);
        modified.put(ToolStats.MIN_STAT, elmAtk * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStats2(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats2(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.25F;
        Float elmAtk = stats.getStats().get(ToolStats.ELM_ATK_STAT);
        modified.put(ToolStats.PWR_STAT, elmAtk * mul);
        modified.put(ToolStats.PRJ_STAT, elmAtk * mul);
        modified.put(ToolStats.MNA_STAT, elmAtk * mul / 2);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public int getColor() {
        return 0xF0B020;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.catalysis");
    }
}
