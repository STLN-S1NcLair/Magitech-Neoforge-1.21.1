package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.comopnent.ComponentInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdaptationTrait extends Trait {

    @Override
    public ToolStats modifyStats(ItemStack stack, int traitLevel) {
        super.modifyStats(stack, traitLevel);
        List<ToolMaterial> materials = stack.getComponents().get(ComponentInit.PART_MATERIAL_COMPONENT.get()).materials();
        Set<ToolMaterial> materialSet = PartToolItem.getMaterialSet(materials);
        ToolStats stats = ToolStats.DEFAULT;
        Map<String, Float> modified = PartToolItem.getDefaultStats(stack).getStats();
        float mul = (float) ((materialSet.size() - 1) * 0.02);
        modified.put(ToolStats.ATK_STAT, modified.get(ToolStats.ATK_STAT) * mul);
        modified.put(ToolStats.ELM_ATK_STAT, modified.get(ToolStats.ELM_ATK_STAT) * mul);
        modified.put(ToolStats.SPD_STAT, (modified.get(ToolStats.SPD_STAT) + 4.0F) * mul);
        modified.put(ToolStats.MIN_STAT, modified.get(ToolStats.MIN_STAT) * mul);
        modified.put(ToolStats.DEF_STAT, modified.get(ToolStats.DEF_STAT) * mul);
        modified.put(ToolStats.RNG_STAT, (modified.get(ToolStats.RNG_STAT) + 3.0F) * mul);
        modified.put(ToolStats.SWP_STAT, modified.get(ToolStats.SWP_STAT) * mul);
        modified.put(ToolStats.DUR_STAT, modified.get(ToolStats.DUR_STAT) * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
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
