package net.stln.magitech.magic.spell;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.stln.magitech.magic.mana.ManaUtil;

import java.util.Map;

public abstract class Spell {

    public abstract Map<ManaUtil.ManaType, Double> getCost();

    public abstract Map<ManaUtil.ManaType, Double> getTickCost();

    public abstract void use(Level level, Player user, InteractionHand hand);

    public abstract void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration);

    public abstract void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity);
}
