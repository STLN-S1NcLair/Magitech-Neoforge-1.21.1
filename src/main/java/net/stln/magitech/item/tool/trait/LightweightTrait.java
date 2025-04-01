package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.HashMap;
import java.util.Map;

public class LightweightTrait extends Trait {

    @Override
    public ToolStats modifyStats(ItemStack stack, int traitLevel) {
        super.modifyStats(stack, traitLevel);
        ToolStats stats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(stats.getStats());
        float mul = traitLevel * 0.06F;
        modified.put(ToolStats.SPD_STAT, (PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.SPD_STAT) + 4.0F) * mul);
        modified.put(ToolStats.MIN_STAT, PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.MIN_STAT) * mul / 2.0F);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
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
