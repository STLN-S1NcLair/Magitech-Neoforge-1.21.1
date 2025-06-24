package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class DuranceTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.2F;
        modified.put(ToolStats.DUR_STAT, stats.getStats().get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats1(stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.2F;
        modified.put(ToolStats.DUR_STAT, stats.getStats().get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
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
