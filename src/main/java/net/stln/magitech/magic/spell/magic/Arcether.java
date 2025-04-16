package net.stln.magitech.magic.spell.magic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.entity.MagicBulletEntity;
import net.stln.magitech.magic.mana.ManaUtil;
import net.stln.magitech.magic.spell.Spell;

import java.util.HashMap;
import java.util.Map;

public class Arcether extends Spell {
    @Override
    public Map<ManaUtil.ManaType, Double> getCost() {
        Map<ManaUtil.ManaType, Double> cost = new HashMap<>();
        cost.put(ManaUtil.ManaType.MANA, 15.0);
        cost.put(ManaUtil.ManaType.LUMINIS, 0.5);
        cost.put(ManaUtil.ManaType.FLUXIA, 0.7);
        return cost;
    }

    @Override
    public Map<ManaUtil.ManaType, Double> getTickCost() {
        return new HashMap<>();
    }

    @Override
    public void use(Level level, Player user, InteractionHand hand) {
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS);
        if (!level.isClientSide) {
            MagicBulletEntity bullet = new MagicBulletEntity(level, user);
            Vec3 velocity = Vec3.directionFromRotation(user.getRotationVector());
            double d = 1 / velocity.length();
            velocity = velocity.multiply(d, d, d);
            bullet.setDeltaMovement(velocity);
            bullet.setPos(user.getX(), user.getEyeY() - 0.3, user.getZ());
            level.addFreshEntity(bullet);
        }
    }

    @Override
    public void usingTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {

    }

    @Override
    public void finishUsing(ItemStack stack, Level level, LivingEntity livingEntity) {

    }
}
