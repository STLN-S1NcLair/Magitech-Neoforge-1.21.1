package net.stln.magitech.content.entity.magicentity.nihilflare;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

public class NihilflareEntity extends BombSpellProjectileEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    int lifeTime = 20;

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> entityType, Level world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.setPierce(-1);
    }

    public NihilflareEntity(Level world, LivingEntity owner, float damage) {
        super(EntityInit.NIHILFLARE_ENTITY.get(), owner, world, null, damage);
        this.setNoGravity(true);
        this.setPierce(-1);
    }

    public NihilflareEntity(Level world, LivingEntity owner, ItemStack weapon, float damage) {
        super(EntityInit.NIHILFLARE_ENTITY.get(), owner, world, weapon, damage);
        this.setNoGravity(true);
        this.setPierce(-1);
    }

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> type, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon, float damage) {
        super(type, x, y, z, world, weapon, damage);
        this.setNoGravity(true);
        this.setPierce(-1);
    }

    public NihilflareEntity(EntityType<? extends BombSpellProjectileEntity> type, LivingEntity owner, Level world, ItemStack stack, @Nullable ItemStack shotFrom, float damage) {
        super(type, owner, world, shotFrom, damage);
        this.setNoGravity(true);
        this.setPierce(-1);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    protected Optional<Supplier<ISpell>> getSpell() {
        return Optional.of(SpellInit.NIHILFLARE);
    }

    @Override
    protected Supplier<SoundEvent> getHitGroundSoundEvent() {
        return SoundInit.NIHILFLARE;
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().scale(0.95));
        lifeTime--;
        if (this.lifeTime < 0) {
            setPierce(0);
            onHit(BlockHitResult.miss(position(), Direction.DOWN, BlockPos.containing(position())));
        }
    }

    @Override
    protected boolean isValidHit(HitResult result) {
        return isFinalHit();
    }

    @Override
    public void discardOrReflect(HitResult result) {
        super.discardOrReflect(result);
        if (result instanceof EntityHitResult entityResult) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.001));
        } else if (result instanceof BlockHitResult blockResult) {
            Vec3 normal = Vec3.atLowerCornerOf(blockResult.getDirection().getNormal());
            reflect(normal);
        }
    }

    protected void reflect(Vec3 normal) {
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 reflected = deltaMovement.subtract(normal.scale(deltaMovement.dot(normal) * 2));
        this.setDeltaMovement(reflected);
        this.hasImpulse = true;
        level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundInit.NIHILFLARE_SHOOT.get(), SoundSource.PLAYERS, 2.0F, 1.0F + random.nextFloat() * 0.2F);
    }

    @Override
    protected void spawnTickParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 old = getOldCenter();
        Vec3 pos = getCurrentCenter();
        LineVFX.spreadLinedSquare(level, old, pos, element, new Section(0F, 1F), 1F, 0.2F, 0.05F);
        LineVFX.spreadLined(level, old, pos, element, ElementParticles::riftParticle, new Section(0F, 1F), 2F, 0.1F, 0.05F);
    }

    protected void spawnHitParticle() {
        Level level = level();
        Element element = getElement();
        Vec3 pos = position();
        PointVFX.burst(level, pos, element, SquareParticles::squareBlastParticle, 400, 1.0F);
        PointVFX.burst(level, pos, element, (lvl, position, elm) -> PresetHelper.bigger(ElementParticles.riftParticle(lvl, position, elm)), 400, 1.0F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, (event) -> {
            return event.setAndContinue(IDLE);
        }));
    }
}
