package net.stln.magitech.content.entity.magicentity.frigala;

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
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellInit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class FrigalaEntity extends SpellProjectileEntity implements GeoEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public FrigalaEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public FrigalaEntity(Level world, LivingEntity player, float damage) {
        super(EntityInit.FRIGALA_ENTITY.get(), player, world, null, damage);

    }

    public FrigalaEntity(Level world, LivingEntity player, ItemStack weapon, float damage) {
        super(EntityInit.FRIGALA_ENTITY.get(), player, world, weapon, damage);
    }

    public FrigalaEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public FrigalaEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.FRIGALA);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.GLACE_LAUNCH;
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.03F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::snowParticle, new Section(0F, 1F), 1F, 0.1F, 0.03F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, ((lev, position, elm) -> SquareParticles.squareGravityParticle(level, position, elm, 0.2F)), 10, 0.2F);
        PointVFX.burst(level, pos, element, ElementParticles::snowParticle, 10, 0.2F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
