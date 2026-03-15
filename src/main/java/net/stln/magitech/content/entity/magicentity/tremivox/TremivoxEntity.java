package net.stln.magitech.content.entity.magicentity.tremivox;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.SpellProjectileEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class TremivoxEntity extends SpellProjectileEntity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public TremivoxEntity(Level world, LivingEntity player, float damage) {
        super(EntityInit.TREMIVOX_ENTITY.get(), player, world, null, damage);

    }

    public TremivoxEntity(Level world, LivingEntity player, ItemStack weapon, float damage) {
        super(EntityInit.TREMIVOX_ENTITY.get(), player, world, weapon, damage);
    }

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public TremivoxEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.TREMIVOX);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.TREMIVOX;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        Vec3 deltaMovement = this.getDeltaMovement();
        if (!level.isClientSide) {
            Vec3 center = this.position().add(this.getDeltaMovement().normalize().scale(10.0));
            LivingEntity target;
            if (this.getOwner() instanceof LivingEntity livingEntity) {
                target = level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), livingEntity, this.getX(), this.getY(), this.getZ(), new AABB(center.subtract(10, 10, 10), center.add(10, 10, 10)));
            } else {
                target = level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), null, this.getX(), this.getY(), this.getZ(), new AABB(center.subtract(10, 10, 10), center.add(10, 10, 10)));
            }
            if (target != null) {
                this.setDeltaMovement(target.position().add(0, target.getBbHeight() * 0.5, 0).subtract(this.position()).normalize().scale(0.15).add(deltaMovement).normalize().scale(deltaMovement.length()));
            }
        }
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.03F);
        LineVFX.spreadLined(level, old, pos, element,
                (lvl, position, elm) -> PresetHelper.smaller(RingParticles.ringParticle(lvl, position, getDeltaMovement().normalize(), elm)),
                new Section(0F, 1F), 0.5F, 0.0F, 0.0F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareGravityParticle, 20, 0.5F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }
}
