package net.stln.magitech.content.entity.magicentity.shockvane;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.BombSpellProjectileEntity;
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
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class ShockvaneEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    boolean exploded = false;

    public ShockvaneEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ShockvaneEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.SHOCKVANE_ENTITY.get(), owner, world, null, damage);

    }

    public ShockvaneEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.SHOCKVANE_ENTITY.get(), owner, world, weapon, damage);
    }

    public ShockvaneEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public ShockvaneEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.SHOCKVANE);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.SHOCKVANE;
    }

    @Override
    protected void onHit(HitResult result) {
        if (!exploded) {
            exploded = true;
            TickScheduler.schedule(12, () -> {
                this.onHit(result);
            }, level().isClientSide);
        }
        super.onHit(result);
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.05F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareBlastParticle, 200, 1.0F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> SquareParticles.squareBlastGravityParticle(lvl, position, elm, 0.2F), 200, 1.2F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> PresetHelper.bigger(RingParticles.ringReversedParticle(lvl, position, elm), 9.0F), 1, 0.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }
}
