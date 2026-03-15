package net.stln.magitech.content.entity.magicentity.voltaris;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.SpellProjectileEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class VoltarisEntity extends SpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public VoltarisEntity(Level world, LivingEntity player, float damage) {
        super(EntityInit.VOLTARIS_ENTITY.get(), player, world, null, damage);
    }

    public VoltarisEntity(Level world, LivingEntity player, ItemStack weapon, float damage) {
        super(EntityInit.VOLTARIS_ENTITY.get(), player, world, weapon, damage);
    }

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public VoltarisEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.VOLTARIS);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.SPARK;
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.03F);
        PointVFX.zap(level, pos, element, 1, 0.25F, 2F, 2F, 0.5F, 5);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = getCurrentCenter();
        PointVFX.burst(level, pos, element, SquareParticles::squareParticle, 10, 0.2F);
        PointVFX.zap(level, pos, element, 5, 0.25F, 2F, 2F, 0.5F, 10);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }
}
