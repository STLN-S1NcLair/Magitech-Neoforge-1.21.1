package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.util.ComponentHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdaptationTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats1(stack, traitLevel, stats);
        List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
        Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> statsMap = stats.getStats();
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = (float) ((materialSet.size() - 1) * 0.15);
        modified.put(ToolStats.ATK_STAT, statsMap.get(ToolStats.ATK_STAT) * mul);
        modified.put(ToolStats.ELM_ATK_STAT, statsMap.get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.SPD_STAT, (statsMap.get(ToolStats.SPD_STAT)) * mul);
        modified.put(ToolStats.MIN_STAT, statsMap.get(ToolStats.MIN_STAT) * mul);
        modified.put(ToolStats.DEF_STAT, statsMap.get(ToolStats.DEF_STAT) * mul);
        modified.put(ToolStats.RNG_STAT, (statsMap.get(ToolStats.RNG_STAT)) * mul);
        modified.put(ToolStats.SWP_STAT, statsMap.get(ToolStats.SWP_STAT) * mul);
        modified.put(ToolStats.DUR_STAT, statsMap.get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats1(stack, traitLevel, stats);
        List<ToolMaterial> materials = ComponentHelper.getPartMaterials(stack);
        Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> statsMap = stats.getStats();
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = (float) ((materialSet.size() - 1) * 0.08);
        modified.put(ToolStats.ATK_STAT, statsMap.get(ToolStats.ATK_STAT) * mul);
        modified.put(ToolStats.ELM_ATK_STAT, statsMap.get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.SPD_STAT, (statsMap.get(ToolStats.SPD_STAT)) * mul);
        modified.put(ToolStats.MIN_STAT, statsMap.get(ToolStats.MIN_STAT) * mul);
        modified.put(ToolStats.DEF_STAT, statsMap.get(ToolStats.DEF_STAT) * mul);
        modified.put(ToolStats.RNG_STAT, (statsMap.get(ToolStats.RNG_STAT)) * mul);
        modified.put(ToolStats.SWP_STAT, statsMap.get(ToolStats.SWP_STAT) * mul);
        modified.put(ToolStats.DUR_STAT, statsMap.get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public int getColor() {
        return 0x805830;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.ataptation");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
