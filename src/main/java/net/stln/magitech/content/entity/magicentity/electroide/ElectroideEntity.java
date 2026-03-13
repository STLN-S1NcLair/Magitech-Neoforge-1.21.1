package net.stln.magitech.content.entity.magicentity.electroide;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.magicentity.BombSpellProjectileEntity;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellInit;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.effect.visual.particle.particle_option.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ElectroideEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ElectroideEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.ELECTROIDE_ENTITY.get(), owner, world, null, damage);

    }

    public ElectroideEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.ELECTROIDE_ENTITY.get(), owner, world, weapon, damage);
    }

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public ElectroideEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.ELECTROIDE);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.ELECTROIDE;
    }

    @Override
    protected void onHit(HitResult result) {
        chain(result);
        super.onHit(result);
    }

    private void chain(HitResult result) {
        Set<Entity> targets = getTargetList(result);
        float chainRadius = getExplosionRadius();
        TickScheduler.schedule(15, () -> {
            Level level = level();
            for (Entity target : targets) {
                Set<Entity> chainTargets = CombatHelper.getEntitiesInBox(level, target, target.position(), new Vec3(chainRadius, chainRadius, chainRadius)).stream().filter(entity -> entity != getOwner() && !targets.contains(entity))
                        .filter(entity -> entity.position().distanceTo(target.position()) < chainRadius).collect(Collectors.toSet());
                if (!level.isClientSide && !chainTargets.isEmpty()) {
                    hitEntity(chainTargets, 0.5F);
                    SoundHelper.broadcastSound(level, getOwner(), target.position(), Optional.of(SoundInit.ARCLUME));
                } else {
                    for (Entity chainTarget : chainTargets) {
                        addChainVFX(level, CombatHelper.getBodyPos(target), CombatHelper.getBodyPos(chainTarget));
                    }
                }
            }
        }, level().isClientSide);
    }

    protected void addChainVFX(Level level, Vec3 start, Vec3 end) {
        Element element = getElement();
        TrailVFX.zapTrail(level, start, end, 0.5F, 1.0F, 0.5F, 10, element);
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 3, 0.0F, 0.1F);
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 1F, 0.2F, 0.05F);
        PointVFX.zap(level, pos, element, 1, 0.25F, 2F, 2F, 0.5F, 5);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareBlastParticle, 400, 1.0F);
        PointVFX.zap(level, pos, element, 10, 0.5F, 6F, 2F, 0.5F, 30);
        PointVFX.zap(level, pos, element, 10, 0.5F, 6F, 2F, 0.5F, 15);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }
}
