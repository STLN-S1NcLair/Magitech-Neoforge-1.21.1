package net.stln.magitech.item.tool.partitem;


import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.trait.Trait;
import net.stln.magitech.util.ColorHelper;
import net.stln.magitech.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PartItem extends Item {
    public PartItem(Properties settings) {
        super(settings);
    }

    public static ToolStats getDefaultStats(ItemStack stack) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            ToolMaterial material = stack.getComponents().get(ComponentInit.MATERIAL_COMPONENT.get()).material();
            ToolStats stats = material.getStats();
            return stats;
        }
        return ToolStats.DEFAULT;
    }

    public static ToolStats getSpellCasterStats(ItemStack stack) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            ToolMaterial material = stack.getComponents().get(ComponentInit.MATERIAL_COMPONENT.get()).material();
            ToolStats stats = material.getSpellCasterStats();
            return stats;
        }
        return ToolStats.DEFAULT;
    }

    public static Trait getTrait(ItemStack stack) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            ToolMaterial material = stack.getComponents().get(ComponentInit.MATERIAL_COMPONENT.get()).material();
            return material.getTrait();
        }
        return null;
    }

    public abstract ToolPart getPart();

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            ResourceLocation resourceLocation = stack.get(ComponentInit.MATERIAL_COMPONENT).getMaterialId();
            return Component.translatable("item." + resourceLocation.getNamespace() + "." + getPart().get(), Component.translatable("material.magitech." + resourceLocation.getPath()));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        addStatsHoverText(stack, tooltipComponents);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    protected void setTier(ItemStack stack, ToolStats finalStats) {
        if (!stack.has(ComponentInit.TIER_COMPONENT)) {
            stack.set(ComponentInit.TIER_COMPONENT, finalStats.getTier());
        }
    }

    protected void addStatsHoverText(@NotNull ItemStack stack, List<Component> tooltipComponents) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            ToolStats finalStats = getDefaultStats(stack);
            setTier(stack, finalStats);

            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("attribute.magitech.tier").append(" ")
                    .append(String.valueOf(stack.get(ComponentInit.TIER_COMPONENT))
                    ).withColor(ColorHelper.getTierColor(stack.get(ComponentInit.TIER_COMPONENT) * 5)));

            if (Screen.hasShiftDown()) {

                tooltipComponents.add(Component.translatable("attribute.magitech.attack_damage").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.ATK_STAT), 2)
                        )).withColor(0xFF4040)));

                tooltipComponents.add(Component.translatable("attribute.magitech.elemental_damage").append(": ").withColor(0xa0a0a0)
                        .append(Component.translatable("element.magitech." + finalStats.getElement().get())
                                .append(" ")
                                .append(Component.literal("x" + String.valueOf(
                                        MathUtil.round(finalStats.getStats().get(ToolStats.ELM_ATK_STAT), 2)
                                ))).withColor(finalStats.getElement().getColor())));

                tooltipComponents.add(Component.translatable("attribute.magitech.attack_speed").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.SPD_STAT), 2)
                        )).withColor(0x40FFC0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.mining_speed").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.MIN_STAT), 2)
                        )).withColor(0x4080C0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.defense").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.DEF_STAT), 2)
                        )).withColor(0xA0C0C0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.attack_range").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.RNG_STAT), 2)
                        )).withColor(0x80c0FF)));

                tooltipComponents.add(Component.translatable("attribute.magitech.sweep_range").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(finalStats.getStats().get(ToolStats.SWP_STAT), 2)
                        )).withColor(0xFFFF80)));

                tooltipComponents.add(Component.translatable("attribute.magitech.durability").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" +
                                MathUtil.round(finalStats.getStats().get(ToolStats.DUR_STAT), 2)
                        ).withColor(0xFFFFFF)));
                ToolStats spellCasterStats = getSpellCasterStats(stack);
                tooltipComponents.add(Component.empty());

                tooltipComponents.add(Component.empty());
                tooltipComponents.add(Component.translatable("attribute.magitech.spell_power").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.ATK_STAT), 2)
                        )).withColor(0xFF4040)));

                tooltipComponents.add(Component.translatable("attribute.magitech.elemental_spell_power").append(": ").withColor(0xa0a0a0)
                        .append(Component.translatable("element.magitech." + spellCasterStats.getElement().get())
                                .append(" ")
                                .append(Component.literal("x" + String.valueOf(
                                        MathUtil.round(spellCasterStats.getStats().get(ToolStats.ELM_ATK_STAT), 2)
                                ))).withColor(spellCasterStats.getElement().getColor())));

                tooltipComponents.add(Component.translatable("attribute.magitech.casting_speed").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.SPD_STAT), 2)
                        )).withColor(0x40FFC0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.cooldown_speed").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.MIN_STAT), 2)
                        )).withColor(0x4080C0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.defense").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.DEF_STAT), 2)
                        )).withColor(0xA0C0C0)));

                tooltipComponents.add(Component.translatable("attribute.magitech.projectile_speed").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.RNG_STAT), 2)
                        )).withColor(0x80c0FF)));

                tooltipComponents.add(Component.translatable("attribute.magitech.mana_efficiency").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" + String.valueOf(
                                MathUtil.round(spellCasterStats.getStats().get(ToolStats.SWP_STAT), 2)
                        )).withColor(0xFFFF80)));

                tooltipComponents.add(Component.translatable("attribute.magitech.durability").append(": ").withColor(0xa0a0a0)
                        .append(Component.literal("x" +
                                MathUtil.round(finalStats.getStats().get(ToolStats.DUR_STAT), 2)
                        ).withColor(0xFFFFFF)));
            } else {
                tooltipComponents.add(Component.translatable("tooltip.magitech.shift").withColor(0x404040));
            }

            Trait trait = getTrait(stack);
            if (trait != null) {
                MutableComponent component = trait.getComponent().append(" ");
                component.append("|");
                tooltipComponents.add(component);
            }
        }
    }
}
