package net.stln.magitech.content.entity.magicentity.ignisca;

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

public class IgniscaEntity extends SpellProjectileEntity implements GeoEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public IgniscaEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public IgniscaEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.IGNISCA_ENTITY.get(), owner, world, null, damage);

    }

    public IgniscaEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.IGNISCA_ENTITY.get(), owner, world, weapon, damage);
    }

    public IgniscaEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
    }

    public IgniscaEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.IGNISCA);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.FIREBALL;
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.03F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::smokeParticle, new Section(0F, 1F), 1F, 0.1F, 0.03F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareGravityParticle, 10, 0.3F);
        PointVFX.burst(level, pos, element, ElementParticles::smokeParticle, 10, 0.2F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
