package net.stln.magitech.content.entity.magicentity.hydrelux;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.stln.magitech.content.entity.magicentity.BombSpellProjectileEntity;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.effect.visual.particle.particle_option.AbstractCustomizableParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.BlowParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.UnstableSquareParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class HydreluxEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation EXPLODE = RawAnimation.begin().thenLoop("explode");
    final int maxVacuumTick = 20;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    int vacuumTick = 0;

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        setPierce(-1);
    }

    public HydreluxEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.HYDRELUX_ENTITY.get(), owner, world, null, damage);
        setPierce(-1);

    }

    public HydreluxEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.HYDRELUX_ENTITY.get(), owner, world, weapon, damage);
        setPierce(-1);
    }

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        setPierce(-1);
    }

    public HydreluxEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
        setPierce(-1);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.HYDRELUX);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.HYDRELUX;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.vacuumTick > 0) {
            this.setNoGravity(true);
            vacuumTick++;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.99));
            float explodeRadius = getExplosionRadius();
            List<Entity> entities = CombatHelper.getEntitiesInBox(this.level(), this, this.position(), new Vec3(explodeRadius, explodeRadius, explodeRadius));
            for (Entity entity : entities) {
                Vec3 targetBodyPos = entity.position().add(0, entity.getBbHeight() * 0.7, 0);
                if (this.level().clip(new ClipContext(targetBodyPos, this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.BLOCK) {
                    Vec3 toEntity = this.position().subtract(entity.position()).normalize();
                    double distance = this.position().distanceTo(entity.position());
                    double pullStrength = Mth.clamp((explodeRadius - distance) / explodeRadius, 0, 1) * 0.35;
                    entity.addDeltaMovement(toEntity.scale(pullStrength));
                }
            }
        }
        if (vacuumTick >= maxVacuumTick) {
            setPierce(0);
            this.onHit(BlockHitResult.miss(position(), Direction.DOWN, BlockPos.containing(position())));
        }
    }

    @Override
    protected boolean isValidHit(HitResult result) {
        return isFinalHit() || result.getType() == HitResult.Type.MISS;
    }

    // パーティクルが表示されないバグの防止用
    @Override
    protected boolean isFinalHit() {
        return pierce == 0;
    }

    @Override
    public void discardOrReflect(HitResult result) {
        super.discardOrReflect(result);
        Vec3 normal = new Vec3(0, 0, 0);
        if (result instanceof EntityHitResult entityResult) {
            normal = result.getLocation().subtract(entityResult.getEntity().position()).normalize();
        } else if (result instanceof BlockHitResult blockResult) {
            normal = Vec3.atLowerCornerOf(blockResult.getDirection().getNormal());
        }
        vacuumTick++;
        reflect(normal);
    }

    protected void reflect(Vec3 normal) {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 reflected = deltaMovement.subtract(normal.scale(deltaMovement.dot(normal) * 2)).normalize().scale(0.2);
        if (vacuumTick < 2) {
            level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundInit.HYDRELUX_BOUNCE.get(), SoundSource.PLAYERS, 2.0F, 1.0F + random.nextFloat() * 0.2F);
        }
        this.setDeltaMovement(reflected);
        this.hasImpulse = true;
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 1F, 0.2F, 0.05F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::leafParticle, new Section(0F, 1F), 2F, 0.1F, 0.05F);
        if (vacuumTick > 0) {
            PointVFX.vortex(level, pos, element, SquareParticles::squareBlastParticle, 5, 0.0F, 3.0F);
        }
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareBlastParticle, 200, 1.0F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> SquareParticles.squareBlastGravityParticle(lvl, position, elm, 0.05F), 200, 1.0F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> PresetHelper.bigger(ElementParticles.leafParticle(lvl, position, elm)), 400, 1.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, (event) -> {
            if (vacuumTick > 0) {
                return event.setAndContinue(EXPLODE);
            }
            return event.setAndContinue(IDLE);
        }));
    }
}
