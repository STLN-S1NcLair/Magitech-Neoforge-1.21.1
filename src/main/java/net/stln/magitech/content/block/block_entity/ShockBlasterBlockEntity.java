package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ShockBlasterBlock;
import net.stln.magitech.content.damage.DamageTypeInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.MagicPerformanceHelper;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.DataMapHelper;
import org.jetbrains.annotations.NotNull;

public class ShockBlasterBlockEntity extends ManaMachineBlockEntity {
    public static final long MANA_PER_BLAST = 50_000L;
    private static final int CHARGE_TICKS = 20;
    private static final double EXPLOSION_RADIUS = 5.0;
    private static final double CHARGE_ORBIT_RADIUS = 0.6;
    private static final double CHARGE_SPIRAL_REVOLUTIONS = 0.5;
    private static final double CHARGE_START_FROM_BOTTOM = 0.0;
    private static final double CHARGE_END_FROM_BOTTOM = 1.5;
    private static final int CHARGE_SPIRAL_COUNT = 3;
    private static final float CHARGE_PARTICLE_SPEED = 0.04F;
    private static final double CHARGE_TOP_PARTICLE_OFFSET = 0.51;
    private static final float CHARGE_TOP_PARTICLE_SPEED = 0.2F;
    private static final float BASE_DAMAGE = 9.0F;
    private static final int BLAST_EVENT = 1;
    private static final int CHARGE_EVENT = 2;
    private int chargeTicksElapsed;

    public ShockBlasterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.SHOCK_BLASTER_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (level.isClientSide) {
            return;
        }
        if (state.getValue(ShockBlasterBlock.POWERED)) {
            chargeTicksElapsed = 0;
            return;
        }

        MachineBlockEntityManaHandler manaHandler = getManaHandler(null);
        if (manaHandler == null || manaHandler.getMana() < MANA_PER_BLAST) {
            chargeTicksElapsed = 0;
            return;
        }

        chargeTicksElapsed++;
        level.blockEvent(pos, state.getBlock(), CHARGE_EVENT, chargeTicksElapsed);
        if (chargeTicksElapsed < CHARGE_TICKS) {
            return;
        }

        if (manaHandler.consumeMana(MANA_PER_BLAST) == MANA_PER_BLAST) {
            blast(level, pos, state);
            level.blockEvent(pos, state.getBlock(), BLAST_EVENT, 0);
            SoundHelper.broadcastSound(level, getBlastOrigin(pos), SoundInit.SHOCKVANE.get(), SoundSource.BLOCKS, 3.0F);
        }
        chargeTicksElapsed = 0;
    }

    private void blast(Level level, BlockPos pos, BlockState state) {
        Vec3 origin = getBlastOrigin(pos);
        Vec3 searchSize = new Vec3(EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2);

        for (Entity target : CombatHelper.getEntitiesInBox(level, null, origin, searchSize)) {
            double distance = target.position().distanceTo(origin);
            if (distance >= EXPLOSION_RADIUS) {
                continue;
            }
            Vec3 targetBodyPos = CombatHelper.getBodyPos(target);
            Vec3 blastSurfacePoint = getBlastSurfacePoint(origin, targetBodyPos);
            if (level.clip(new ClipContext(targetBodyPos, blastSurfacePoint,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity) null)).getType() == HitResult.Type.BLOCK) {
                continue;
            }

            float falloff = (float) (1.0 - distance / EXPLOSION_RADIUS);
            float damage = BASE_DAMAGE * DataMapHelper.getElementMultiplier(target, Element.TREMOR) * falloff;
            MagicPerformanceHelper.applyRawMagicDamage(null, null, target,
                    level.damageSources().source(DamageTypeInit.TREMOR_DAMAGE), damage);
        }
    }

    private Vec3 getBlastOrigin(BlockPos pos) {
        return pos.getCenter();
    }

    private Vec3 getBlastSurfacePoint(Vec3 blastOrigin, Vec3 targetPosition) {
        Vec3 direction = targetPosition.subtract(blastOrigin);
        double furthestAxis = Math.max(Math.abs(direction.x), Math.max(Math.abs(direction.y), Math.abs(direction.z)));
        if (furthestAxis <= 0.5) {
            return targetPosition;
        }

        Vec3 surfacePoint = blastOrigin.add(direction.scale(0.5 / furthestAxis));
        return surfacePoint.add(direction.normalize().scale(1.0E-3));
    }

    private void spawnChargeParticle(Level level, int chargeTick) {
        Direction facing = getBlockState().getValue(ShockBlasterBlock.FACING);
        double progress = (chargeTick - 1.0) / (CHARGE_TICKS - 1.0);
        Vec3 facingDirection = Vec3.atLowerCornerOf(facing.getNormal());
        double heightFromBottom = CHARGE_START_FROM_BOTTOM
                + progress * (CHARGE_END_FROM_BOTTOM - CHARGE_START_FROM_BOTTOM);
        double axialOffsetFromCenter = heightFromBottom - 0.5;

        for (int spiral = 0; spiral < CHARGE_SPIRAL_COUNT; spiral++) {
            double angle = progress * Math.PI * 2.0 * CHARGE_SPIRAL_REVOLUTIONS
                    + spiral * Math.PI * 2.0 / CHARGE_SPIRAL_COUNT;
            Vec3 orbitDirection = getOrbitDirection(facing.getAxis(), angle);
            Vec3 position = getBlastOrigin(worldPosition)
                    .add(facingDirection.scale(axialOffsetFromCenter))
                    .add(orbitDirection.scale(CHARGE_ORBIT_RADIUS));

            PointVFX.spray(level, position, Element.TREMOR,
                    (particleLevel, particlePos, element) -> PresetHelper.modifyBloomTransparency(
                            PresetHelper.longer(SquareParticles.squareParticle(particleLevel, particlePos, element), 2.0F), 0.5F),
                    facingDirection, 1, CHARGE_PARTICLE_SPEED, 0.0F);
        }

        Vec3 topPosition = getBlastOrigin(worldPosition).add(facingDirection.scale(CHARGE_TOP_PARTICLE_OFFSET));
        PointVFX.spray(level, topPosition, Element.TREMOR,
                (particleLevel, particlePos, element) -> PresetHelper.modifyBloomTransparency(
                        PresetHelper.friction(PresetHelper.longer(SquareParticles.squareShrinkParticle(particleLevel, particlePos, element), 2.0F), 0.6F), 0.5F),
                facingDirection, 1, CHARGE_TOP_PARTICLE_SPEED, 0.1F);
    }

    private Vec3 getOrbitDirection(Direction.Axis axis, double angle) {
        double firstOffset = Math.cos(angle);
        double secondOffset = Math.sin(angle);
        return switch (axis) {
            case X -> new Vec3(0.0, firstOffset, secondOffset);
            case Y -> new Vec3(firstOffset, 0.0, secondOffset);
            case Z -> new Vec3(firstOffset, secondOffset, 0.0);
        };
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == CHARGE_EVENT) {
            if (level != null && level.isClientSide) {
                spawnChargeParticle(level, Mth.clamp(type, 1, CHARGE_TICKS));
            }
            return true;
        }
        if (id == BLAST_EVENT) {
            if (level != null && level.isClientSide) {
                Vec3 origin = getBlastOrigin(worldPosition);
                PointVFX.burst(level, origin, Element.TREMOR,
                        (lvl, position, element) -> PresetHelper.noClip(SquareParticles.squareBlastParticle(lvl, position, element), 2),
                        50, 0.8F);
                PointVFX.burst(level, origin, Element.TREMOR,
                        (lvl, position, element) -> PresetHelper.noClip(SquareParticles.squareBlastGravityParticle(lvl, position, element, 0.2F), 2),
                        100, 1.0F);
                PointVFX.burst(level, origin, Element.TREMOR,
                        (lvl, position, element) -> PresetHelper.bigger(PresetHelper.noClip(RingParticles.ringReversedParticle(lvl, position, Vec3.atLowerCornerOf(getBlockState().getValue(ShockBlasterBlock.FACING).getNormal()), element), 2), 9.0F),
                        1, 0.0F);
            }
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public MachineBlockEntityManaHandler getManaHandler(Direction side) {
        Direction inputSide = getBlockState().getValue(ShockBlasterBlock.FACING).getOpposite();
        if (side != null && side != inputSide) {
            return null;
        }
        return super.getManaHandler(side);
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        Direction inputSide = state.getValue(ShockBlasterBlock.FACING).getOpposite();
        return side == null || side == inputSide ? ManaFlowRule.insertOnly(-1.0F) : ManaFlowRule.none();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.shock_blaster");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("charge_ticks_elapsed", chargeTicksElapsed);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        chargeTicksElapsed = Math.clamp(tag.getInt("charge_ticks_elapsed"), 0, CHARGE_TICKS);
    }
}
