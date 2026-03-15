package net.stln.magitech.content.entity.magicentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.ISummonEntitySpell;
import net.stln.magitech.feature.magic.spell.SpellHelper;
import net.stln.magitech.helper.DataMapHelper;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SpellProjectileEntity extends AbstractSpellProjectileEntity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected float damage;
    protected ItemStack wand = null;

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, double x, double y, double z, Level level, @javax.annotation.Nullable ItemStack wand, float damage) {
        this(entityType, level);
        this.setPos(x, y, z);
        this.damage = damage;
        if (wand != null && level instanceof ServerLevel serverlevel) {
            if (wand.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            this.wand = wand.copy();
        }
    }

    protected SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, LivingEntity owner, Level level, @Nullable ItemStack wand, float damage) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, wand, damage);
        this.setOwner(owner);
    }

    protected Element getDefaultElement() {
        return Element.NONE;
    }

    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.empty();
    }

    protected abstract Supplier<SoundEvent> getHitGroundSoundEvent();

    // これをオーバーライドする前にSpell側で効果を実装すること
    protected void applyEffectToTarget(Entity target) {

    }

    protected abstract void spawnTickParticle();

    protected abstract void spawnHitParticle();

    protected Set<Entity> getTargetList(HitResult result) {
        if (result instanceof EntityHitResult entityHitResult) {
            return Set.of(entityHitResult.getEntity());
        }
        return new HashSet<>();
    }

    protected Element getElement() {
        return getSpell().isPresent() ? getSpell().get().get().getConfig().element() : getDefaultElement();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            spawnTickParticle();
            tickTrail();
        }
    }

    protected static final int TRAIL_LENGTH = 10;
    protected static final int LONG_TRAIL_LENGTH = 30;

    protected void tickTrail() {
        Element element = getElement();
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(TRAIL_LENGTH);
        TrailPointBuilder longTrail = TrailPointBuilder.create(LONG_TRAIL_LENGTH);
        TrailData trailData = new TrailData(level(), builderFunc, trail, element.getPrimary(), element.getSecondary(), this.getBbHeight() + 0.1F, 0.9F);
        TrailData longTrailData = new TrailData(level(), builderFunc, longTrail, element.getSecondary(), element.getDark(), this.getBbHeight() / 2, 0.5F);
        Vec3 center = this.getCenter();
        TrailRenderer.updateTrail(this, trailData, new TrailPoint(center), TrailRenderer.TRAIL);
        TrailRenderer.updateTrail(this, longTrailData, new TrailPoint(center), TrailRenderer.LONG_TRAIL);
    }

    protected Vec3 getCenter() {
        return new Vec3(xOld, yOld, zOld).lerp(position(), 0.5F).add(0, getBbHeight() * 0.5, 0);
    }

    protected Vec3 getOldCenter() {
        return new Vec3(xOld, yOld, zOld).add(0, getBbHeight() * 0.5, 0);
    }

    protected Vec3 getCurrentCenter() {
        return position().add(0, getBbHeight() * 0.5, 0);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        onHit(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        onHit(blockHitResult);
    }

    protected void onHit(HitResult result) {
        discardOrReflect(result);
        if (!this.level().isClientSide) {
            if (isValidHit(result)) {
                playHitSound();
                damageEntity(result);
            }
        } else {
            if (isValidHit(result)) {
                spawnHitParticle();
            }
        }
        if (pierce > 0) {
            pierce--;
        }
    }

    protected void playHitSound() {
        SoundHelper.broadcastSound(level(), getOwner(), position(), Optional.of(getHitGroundSoundEvent()));
    }

    public void discardOrReflect(HitResult result) {
        if (isFinalHit()) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EntityEvent.DEATH) {
            if (level().isClientSide) {
                onHit(BlockHitResult.miss(position(), Direction.DOWN, BlockPos.containing(position())));
//                spawnHitParticle();
            }
        }
    }

    protected void damageEntity(HitResult result) {
        Set<Entity> entities = getTargetList(result);
        hitEntity(entities);
    }

    protected void hitEntity(Set<Entity> entities, float multiplier) {
        for (Entity entity : entities) {
            Optional<Supplier<ISpell>> spell = getSpell();
            Element element = getElement();
            float effectiveDamage = damage * DataMapHelper.getElementMultiplier(entity, element) * multiplier;

            LivingEntity owner = getOwner() instanceof LivingEntity living ? living : null;
            ResourceKey<DamageType> damageType = element.getDamageType();
            DamageSource source = owner == null ? this.damageSources().source(damageType) : this.damageSources().source(damageType, owner);
            MagicPerformanceHelper.applyRawMagicDamage(owner, wand, entity, source, effectiveDamage);

            if (entity instanceof ItemEntity item && spell.isPresent()) {
                SpellHelper.applyEffectToItem(level(), spell.get().get(), item);
            }
            if (spell.isPresent() && spell.get().get() instanceof ISummonEntitySpell) {
                ((ISummonEntitySpell) spell.get().get()).applyEffectToTarget(level(), this, getOwner(), entity);
            }
            applyEffectToTarget(entity);
        }
    }

    protected void hitEntity(Set<Entity> entities) {
        hitEntity(entities, 1.0F);
    }

    public void setDamage(float value) {
        this.damage = value;
    }

//    @Override
//    public void handleEntityEvent(byte status) {
//        if (status == EntityEvent.DEATH) {
//            if (this.level().isClientSide) {
//                explode();
//                addHitEffect();
//            } else if (bounceCount >= maxBounces) {
//                this.discard();
//            }
//        }
//    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("damage", this.damage);
        compound.putInt("pierce", this.pierce);
    }

    /**
     * (abstract) Protected helper method child read subclass entity data parent NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("damage", 99)) {
            this.damage = compound.getFloat("damage");
        }
        if (compound.contains("pierce", 99)) {
            this.pierce = compound.getInt("pierce");
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
