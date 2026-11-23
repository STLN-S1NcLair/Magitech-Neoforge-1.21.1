package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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
    private static final int COOLDOWN_TICKS = 20;
    private static final double EXPLOSION_RADIUS = 5.0;
    private static final float BASE_DAMAGE = 9.0F;
    private static final int BLAST_EVENT = 1;
    private int cooldownTicksRemaining;

    public ShockBlasterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.SHOCK_BLASTER_ENTITY.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        if (level.isClientSide) {
            return;
        }
        if (cooldownTicksRemaining > 0) {
            cooldownTicksRemaining--;
        }
        if (state.getValue(ShockBlasterBlock.POWERED) || cooldownTicksRemaining > 0) {
            return;
        }

        MachineBlockEntityManaHandler manaHandler = getManaHandler(null);
        if (manaHandler != null && manaHandler.getMana() >= MANA_PER_BLAST
                && manaHandler.consumeMana(MANA_PER_BLAST) == MANA_PER_BLAST) {
            blast(level, pos, state);
            level.blockEvent(pos, state.getBlock(), BLAST_EVENT, 0);
            SoundHelper.broadcastSound(level, getBlastOrigin(pos), SoundInit.SHOCKVANE.get(), SoundSource.BLOCKS, 3.0F);
            cooldownTicksRemaining = COOLDOWN_TICKS;
        }
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

    @Override
    public boolean triggerEvent(int id, int type) {
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
        tag.putInt("cooldown_ticks_remaining", cooldownTicksRemaining);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        cooldownTicksRemaining = Math.clamp(tag.getInt("cooldown_ticks_remaining"), 0, COOLDOWN_TICKS);
    }
}
