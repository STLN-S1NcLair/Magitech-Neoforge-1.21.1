package net.stln.magitech.content.entity.magicentity.mana_bolt;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.SpellProjectileEntity;
import net.stln.magitech.content.entity.magicentity.mana_wisp.ManaWispEntity;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Optional;
import java.util.function.Supplier;

public class ManaBoltEntity extends SpellProjectileEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final int MIN_WISP_COUNT = 2;
    private static final int MAX_WISP_COUNT = 3;

    public ManaBoltEntity(EntityType<? extends SpellProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    public ManaBoltEntity(Level level, LivingEntity owner) {
        super(EntityInit.MANA_BOLT_ENTITY.get(), owner, level, null, 0.0F);
        this.setNoGravity(true);
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

    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            this.splitIntoWisps(result.getLocation());
        }
        super.onHit(result);
    }

    @Override
    protected void damageEntity(HitResult result) {
    }

    private void splitIntoWisps(Vec3 impactPosition) {
        int count = this.random.nextInt(MIN_WISP_COUNT, MAX_WISP_COUNT + 1);
        Entity owner = this.getOwner();
        for (int i = 0; i < count; i++) {
            Vec3 initialMotion = new Vec3(
                    this.random.nextDouble() - 0.5,
                    this.random.nextDouble() - 0.5,
                    this.random.nextDouble() - 0.5
            ).normalize().scale(0.12);
            ManaWispEntity wisp = new ManaWispEntity(this.level(), owner, impactPosition, initialMotion);
            this.level().addFreshEntity(wisp);
        }
    }

    @Override
    protected void spawnTickParticle() {
        LineVFX.spreadLinedSquare(this.level(), this.getOldCenter(), this.getCurrentCenter(), Element.MANA, new Section(0.0F, 1.0F), 2.0F, 0.18F, 0.02F);
    }

    @Override
    protected void spawnHitParticle() {
        PointVFX.burstSquare(this.level(), this.position(), Element.MANA, 12, 0.2F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", state -> state.setAndContinue(IDLE)));
    }
}
