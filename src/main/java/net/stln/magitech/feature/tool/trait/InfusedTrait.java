package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.BlockHelper;
import net.stln.magitech.helper.DataMapHelper;

import java.awt.*;
import java.util.Set;

public class InfusedTrait extends Trait {

    private static final int MANA_COST = 5000;

    @Override
    public void additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, Set<BlockPos> posSet, int damageAmount, Direction direction, boolean simulate) {
        if (effectEnabled(player, level, stack, traitLevel, properties)) {
            posSet.addAll(BlockHelper.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel * 5));
            if (!simulate) {
                useMana(player, traitLevel);
            }
        }
        super.additionalBlockBreak(player, level, stack, traitLevel, properties, blockState, pos, posSet, damageAmount, direction, simulate);
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, properties, target);
        if (effectEnabled(player, level, stack, traitLevel, properties)) {
            DamageSource elementalDamageSource = player.damageSources().source(Element.MANA.getDamageType(), player);
            target.invulnerableTime = 0;
            target.hurtMarked = false;
            float effectiveDamage = 2 * traitLevel * DataMapHelper.getElementMultiplier(target, Element.MANA);
            target.hurt(elementalDamageSource, effectiveDamage);
            useMana(player, traitLevel);
        }
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        EntityManaHandler handler = player.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        return handler != null && handler.getMana() > MANA_COST * traitLevel;
    }

    private void useMana(Player player, int traitLevel) {
        EntityManaHandler handler = player.getCapability(ManaCapabilities.MANA_CAPABLE_ENTITY);
        if (handler != null) {
            handler.addMana(-MANA_COST * traitLevel);
        }
    }

    @Override
    public Color getColor() {
        return new Color(0xD5BC9B);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xEAE5B1);
    }

    @Override
    public Color getSecondary() {
        return new Color(0X816972);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("infused");
    }
}
