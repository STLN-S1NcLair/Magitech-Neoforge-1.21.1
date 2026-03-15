package net.stln.magitech.feature.magic.spell;

import com.mojang.datafixers.util.Function4;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import org.jetbrains.annotations.Nullable;

public abstract class ShotSpell extends Spell implements ISummonEntitySpell {

    private final Function4<Level, LivingEntity, ItemStack, Float, ? extends Entity> bulletProvider;

    public ShotSpell(SpellConfig.Builder builder, Function4<Level, LivingEntity, ItemStack, Float, ? extends Entity> bulletProvider) {
        super(builder);
        this.bulletProvider = bulletProvider;
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        if (!level.isClientSide) {
            Entity bullet = bulletProvider.apply(level, caster, wand, MagicPerformanceHelper.getOutgoingMagicDamage(caster, wand, SpellPropertyInit.DAMAGE, this));
            Vec3 velocity = Vec3.directionFromRotation(caster.getRotationVector());
            float projectileSpeed = MagicPerformanceHelper.getEffectiveProjectileSpeed(caster, wand, this);
            velocity = velocity.normalize().scale(projectileSpeed);
            bullet.setDeltaMovement(velocity);
            bullet.setPos(caster.getX(), caster.getEyeY() - 0.3, caster.getZ());
            level.addFreshEntity(bullet);
        }
    }
}
