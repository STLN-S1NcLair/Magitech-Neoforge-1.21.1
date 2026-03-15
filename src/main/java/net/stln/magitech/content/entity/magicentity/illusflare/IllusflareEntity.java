package net.stln.magitech.content.entity.magicentity.illusflare;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.entity.EntityInit;
import net.stln.magitech.content.entity.magicentity.BombSpellProjectileEntity;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class IllusflareEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public IllusflareEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        setPierce(2);
    }

    public IllusflareEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.ILLUSFLARE_ENTITY.get(), owner, world, null, damage);
        setPierce(2);

    }

    public IllusflareEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.ILLUSFLARE_ENTITY.get(), owner, world, weapon, damage);
        setPierce(2);
    }

    public IllusflareEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        setPierce(2);
    }

    public IllusflareEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
        setPierce(2);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.ILLUSFLARE);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.ILLUSFLARE;
    }

    @Override
    protected boolean isValidHit(HitResult result) {
        return true;
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
        reflect(normal);
    }

    protected void reflect(Vec3 normal) {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 reflected = deltaMovement.subtract(normal.scale(deltaMovement.dot(normal) * 2));
        this.setDeltaMovement(reflected);
        this.hasImpulse = true;
        this.markHurt();
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 1F, 0.2F, 0.05F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::glintParticle, new Section(0F, 1F), 2F, 0.1F, 0.05F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareBlastParticle, 400, 1.0F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> PresetHelper.bigger(ElementParticles.glintParticle(lvl, position, elm)), 400, 1.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", (event) -> event.setAndContinue(IDLE)));
    }
}
