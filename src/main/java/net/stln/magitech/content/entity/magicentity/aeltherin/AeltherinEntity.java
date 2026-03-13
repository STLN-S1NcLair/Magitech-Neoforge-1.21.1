package net.stln.magitech.content.entity.magicentity.aeltherin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
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
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Optional;
import java.util.function.Supplier;

public class AeltherinEntity extends SpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public AeltherinEntity(EntityType<? extends SpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
    }

    public AeltherinEntity(Level world, LivingEntity player, float damage) {
        super(EntityInit.AELTHERIN_ENTITY.get(), player, world, null, damage);
    }

    public AeltherinEntity(Level world, LivingEntity player, ItemStack wand, float damage) {
        super(EntityInit.AELTHERIN_ENTITY.get(), player, world, wand, damage);
    }

    public AeltherinEntity(EntityType<? extends SpellProjectileEntity> type, double x, double y, double z, Level world, @Nullable ItemStack wand, float damage) {
        super(type, x, y, z, world, wand, damage);
    }

    public AeltherinEntity(EntityType<? extends SpellProjectileEntity> type, LivingEntity owner, Level world, @Nullable ItemStack wand, float damage) {
        super(type, owner, world, wand, damage);
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.AELTHERIN);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.AELTHERIN;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            this.addDeltaMovement(this.getDeltaMovement().scale(0.3));
            this.hasImpulse = true;
            this.hurtMarked = true;
        }
        if (this.getDeltaMovement().length() > 100) {
            this.discard();
        }
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 2F, 0.2F, 0.03F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::leafParticle, new Section(0F, 1F), 1F, 0.1F, 0.03F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> SquareParticles.squareGravityParticle(lvl, position, elm, 0.1F), 10, 0.4F);
        PointVFX.burst(level, pos, element, ElementParticles::leafParticle, 10, 0.2F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", event -> event.setAndContinue(IDLE)));
    }
}
