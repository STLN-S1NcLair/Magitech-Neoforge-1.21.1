package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.Map;

public class DuranceTrait extends Trait {

    @Override
    public ToolStats modifyStats(ItemStack stack, int traitLevel) {
        super.modifyStats(stack, traitLevel);
        ToolStats stats = ToolStats.DEFAULT;
        Map<String, Float> modified = stats.getStats();
        float mul = traitLevel * 0.2F;
        modified.put(ToolStats.DUR_STAT, PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
    }

    @Override
    public void onRepair(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, int repairAmount) {
        stack.setDamageValue((int) Math.max(0, repairAmount * traitLevel * 0.3F));
    }

    @Override
    public int getColor() {
        return 0xC0C0C0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.durance");
    }
}
