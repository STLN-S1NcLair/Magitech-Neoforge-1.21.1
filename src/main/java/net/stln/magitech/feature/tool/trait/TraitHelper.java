package net.stln.magitech.feature.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.helper.ComponentHelper;

import java.util.ArrayList;
import java.util.List;

public class TraitHelper {

    // 同じ特性をまとめてレベル化する
    public static List<TraitInstance> getTrait(ItemStack stack) {
        List<Trait> traits = ComponentHelper.getPartMaterials(stack).stream().map(ToolMaterial::trait).toList();
        List<TraitInstance> instances = new ArrayList<>();

        for (Trait trait : traits) {
            boolean contained = false;
            ArrayList<TraitInstance> copyInstances = new ArrayList<>(instances);

            // あるならレベルをインクリメント
            for (int i = 0; i < copyInstances.size(); i++) {
                TraitInstance instance = copyInstances.get(i);

                if (instance.trait().equals(trait)) {
                    instances.set(i, instance.increment());
                    contained = true;
                }
            }

            // なければ新規追加
            if (!contained) {
                instances.add(TraitInstance.create(trait));
            }
        }

        return instances;
    }

    public static MutableComponent getTooltip(TraitInstance instance) {
        Trait trait = instance.trait();
        int level = instance.level();
        int max = trait.getMaxLevel();
        String bar = "|".repeat(level);
        if (max == -1) {
            return trait.getComponent().append(Component.literal(String.format(" [ %02d ] " + bar, level)));
        } else {
            return trait.getComponent().append(Component.literal(String.format(" [ %02d / %02d ] " + bar, level, max)));
        }
    }
}
