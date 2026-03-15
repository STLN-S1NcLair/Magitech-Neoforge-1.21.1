package net.stln.magitech.content.entity.magicentity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.InterpolatedTrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
        Element element = getElement();
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(TRAIL_LENGTH);
        TrailPointBuilder longTrail = TrailPointBuilder.create(LONG_TRAIL_LENGTH);
        TrailData trailData = new TrailData(level(), builderFunc, trail, element.getPrimary(), element.getSecondary(), this.getBbHeight() + 0.1F, 0.9F);
        TrailData longTrailData = new TrailData(level(), builderFunc, longTrail, element.getSecondary(), element.getDark(), this.getBbHeight() / 2, 0.5F);
        Vec3 center = this.getCenter();
        Vec3 random = VectorHelper.random(this.random);
        Vec3 end = center.add(random);
        TrailRenderer.updateTrail(this, trailData, new InterpolatedTrailPoint(center, end, LONG_TRAIL_LENGTH), TrailRenderer.TRAIL);
        TrailRenderer.updateTrail(this, longTrailData, new InterpolatedTrailPoint(center, end, LONG_TRAIL_LENGTH), TrailRenderer.LONG_TRAIL);
    }

    @Override
    protected void playHitSound() {
        SoundHelper.broadcastSound(level(), position(), getHitGroundSoundEvent().get(), SoundHelper.getSoundSource(getOwner()), 10.0F);
    }
}
