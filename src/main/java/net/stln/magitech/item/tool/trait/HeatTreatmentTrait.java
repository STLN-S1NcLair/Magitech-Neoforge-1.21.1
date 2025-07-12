package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.MaterialInit;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeatTreatmentTrait extends Trait {

    @Override
    public ToolStats modifyStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStats1(stack, traitLevel, stats);
        List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
        ToolStats defaultStats = ToolStats.DEFAULT;
        if (materials.contains(MaterialInit.AMETHYST)) {
            Map<String, Float> statsMap = stats.getStats();
            Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
            float mul = 0.15F * traitLevel;
            modified.put(ToolStats.ELM_ATK_STAT, statsMap.get(ToolStats.ELM_ATK_STAT) * mul);
            modified.put(ToolStats.MIN_STAT, statsMap.get(ToolStats.MIN_STAT) * mul);
            return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
        }
        return defaultStats;
    }

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStats1(stack, traitLevel, stats);
        List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
        ToolStats defaultStats = ToolStats.DEFAULT;
        if (materials.contains(MaterialInit.AMETHYST)) {
            Map<String, Float> statsMap = stats.getStats();
            Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
            float mul = 0.15F * traitLevel;
            modified.put(ToolStats.ELM_ATK_STAT, statsMap.get(ToolStats.ELM_ATK_STAT) * mul);
            modified.put(ToolStats.MIN_STAT, statsMap.get(ToolStats.MIN_STAT) * mul);
            return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
        }
        return defaultStats;
    }

    @Override
    public int getColor() {
        return 0xFFF080;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.heat_treatment");
    }
}
