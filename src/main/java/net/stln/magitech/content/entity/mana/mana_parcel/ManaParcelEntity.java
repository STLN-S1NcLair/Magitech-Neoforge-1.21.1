package net.stln.magitech.content.entity.mana.mana_parcel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.SpellProjectileEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ManaParcelEntity extends SpellProjectileEntity {

    private long mana;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public ManaParcelEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ManaParcelEntity(Level world, Vec3 pos, long mana) {
        super(EntityInit.MANA_PARCEL_ENTITY.get(), pos.x, pos.y, pos.z, world, null, mana / 20000F);
        this.mana = mana;
    }

    @Override
    protected Element getDefaultElement() {
        return Element.MANA;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.empty();
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.MANA_PARCEL;
    }

    protected void tickTrail() {
        Element element = getElement();
        Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> builderFunc = TrailRenderHelper.defaultBuilderFunc();
        TrailPointBuilder trail = TrailPointBuilder.create(TRAIL_LENGTH);
        TrailPointBuilder longTrail = TrailPointBuilder.create(LONG_TRAIL_LENGTH);
        TrailData trailData = new TrailData(level(), builderFunc, trail, element.getPrimary(), element.getSecondary(), this.getBbHeight() * 2 + 0.1F, 0.9F);
        TrailData longTrailData = new TrailData(level(), builderFunc, longTrail, element.getSecondary(), element.getDark(), this.getBbHeight(), 0.5F);
        Vec3 center = this.getCenter();
        TrailRenderer.updateTrail(this, trailData, new TrailPoint(center), TrailRenderer.TRAIL);
        TrailRenderer.updateTrail(this, longTrailData, new TrailPoint(center), TrailRenderer.LONG_TRAIL);
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.01F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareParticle, 10, 0.2F);
    };

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("mana", this.mana);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.mana = compound.getLong("mana");
    }

    public long getMana() {
        return mana;
    }

    public void setMana(long mana) {
        this.mana = mana;
    }
}
