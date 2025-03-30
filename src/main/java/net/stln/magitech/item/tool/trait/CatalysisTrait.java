package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.Map;

public class CatalysisTrait extends Trait {

    @Override
    public ToolStats modifyStats(ItemStack stack, int traitLevel) {
        super.modifyStats(stack, traitLevel);
        ToolStats stats = ToolStats.DEFAULT;
        Map<String, Float> modified = stats.getStats();
        float mul = traitLevel * 0.05F;
        modified.put(ToolStats.ATK_STAT, PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.MIN_STAT, PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.ELM_ATK_STAT) * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
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
