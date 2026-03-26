package net.stln.magitech.content.item.tool.toolitem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.SpellComponent;
import net.stln.magitech.content.network.TraitTickPayload;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.register.ToolMaterialRegister;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.feature.tool.trait.TraitHelper;
import net.stln.magitech.helper.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SpellCasterItem extends SynthesisedToolItem {

    public SpellCasterItem(Properties settings, ToolTypeLike toolTypeLike) {
        super(settings, toolTypeLike);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (ComponentHelper.isBroken(stack)) return InteractionResultHolder.pass(stack);

        ItemStack threadbound = CuriosHelper.getThreadBoundStack(player).orElse(ItemStack.EMPTY);

        if (!threadbound.isEmpty()) {
            SpellComponent spells = ComponentHelper.getSpells(threadbound);
            if (spells.selected() < spells.spells().size()) {
                ISpell spell = spells.getSelectedSpell();
                if (spell.canCast(level, player)) {
                    spell.cast(level, player, stack, usedHand, true);
                    TraitHelper.getTrait(stack).forEach(((instance) -> {
                        instance.trait().onCastSpell(player, level, stack, instance.level(), getAppliedProperties(player, level, stack));
                    }));
                } else {
                    player.releaseUsingItem();
                    return InteractionResultHolder.consume(stack);
                }
            } else {
                threadbound.set(ComponentInit.SPELL_COMPONENT, spells.setSelected(0));
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        if (ComponentHelper.isBroken(stack)) return;
        if (livingEntity instanceof Player user) {
            ItemStack threadbound = CuriosHelper.getThreadBoundStack(user).orElse(ItemStack.EMPTY);
            InteractionHand hand = user.getMainHandItem().equals(stack) ? InteractionHand.MAIN_HAND : user.getOffhandItem().equals(stack) ? InteractionHand.OFF_HAND : null;

            if (!threadbound.isEmpty()) {
                SpellComponent spellComponent = ComponentHelper.getSpells(threadbound);
                ISpell spell = spellComponent.getSelectedSpell();
                if (spell.canContinuousCast(level, livingEntity)) {
                    spell.tick(level, livingEntity, stack, hand, getUseDuration(stack, livingEntity) - remainingUseDuration, true);
                } else {
                    livingEntity.releaseUsingItem();
                }
            } else {
                livingEntity.releaseUsingItem();
            }
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeCharged) {

        if (ComponentHelper.isBroken(stack)) return;
        super.releaseUsing(stack, level, livingEntity, timeCharged);
        if (livingEntity instanceof Player user) {
            InteractionHand hand = user.getMainHandItem().equals(stack) ? InteractionHand.MAIN_HAND : user.getOffhandItem().equals(stack) ? InteractionHand.OFF_HAND : null;
            CuriosHelper.getThreadBoundStack(user).ifPresent(threadbound -> {
                SpellComponent spells = ComponentHelper.getSpells(threadbound);
                ISpell spell = spells.getSelectedSpell();

                spell.end(level, livingEntity, stack, hand, true);
            });
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResult onLeftClick(Player user, InteractionHand hand, Level world) {
        return InteractionResult.PASS;
    }
}
