package net.stln.magitech.content.entity.magicentity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.DataMapHelper;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.rendering.trail.InterpolatedTrailPoint;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class BombSpellProjectileEntity extends SpellProjectileEntity {

    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, double x, double y, double z, Level level, @Nullable ItemStack wand, float damage) {
        super(entityType, x, y, z, level, wand, damage);
    }

    protected BombSpellProjectileEntity(EntityType<? extends BombSpellProjectileEntity> entityType, LivingEntity owner, Level level, @Nullable ItemStack wand, float damage) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, wand, damage);
        this.setOwner(owner);
    }

    protected float defaultExplosionRadius() {
        return 5.0F;
    }

    @Override
    protected Set<Entity> getTargetList(HitResult result) {
        float explodeRadius = defaultExplosionRadius();
        explodeRadius = getExplosionRadius();
        float finalExplodeRadius = explodeRadius;
        // 範囲内の敵を判定
        return CombatHelper.getEntitiesInBox(this.level(), this, this.position(), new Vec3(explodeRadius * 2, explodeRadius * 2, explodeRadius * 2)).stream()
                .filter(entity -> entity.position().distanceTo(this.position()) < finalExplodeRadius)
                .filter(entity -> this.level().clip(new ClipContext(CombatHelper.getBodyPos(entity), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK)
                .collect(Collectors.toSet());
    }

    public float getExplosionRadius() {
        Optional<Supplier<ISpell>> spell = getSpell();
        float explodeRadius = defaultExplosionRadius();
        if (spell.isPresent()) {
            explodeRadius = spell.get().get().getConfig().properties().get(SpellPropertyInit.EXPLOSION_RADIUS);
            if (getOwner() != null && getOwner() instanceof LivingEntity caster) {
                explodeRadius = MagicPerformanceHelper.getEffectiveExplosionRadius(caster, wand, spell.get().get());
            }
        }
        return explodeRadius;
    }

    @Override
    protected void tickTrail() {
        Vec3 center = this.getCenter();
        Vec3 random = VectorHelper.random(this.random);
        trail.addTrailPoint(new InterpolatedTrailPoint(center, center.add(random), 20));
        trail.tickTrailPoints();
        longTrail.addTrailPoint(new InterpolatedTrailPoint(center, center.add(random), 20));
        longTrail.tickTrailPoints();
    }

    @Override
    protected void playHitSound() {
        SoundHelper.broadcastSound(level(), position(), getHitGroundSoundEvent().get(), SoundHelper.getSoundSource(getOwner()), 10.0F);
    }
}
