package net.stln.magitech.content.block;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.InfusionAltarBlockEntity;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

import javax.annotation.Nullable;

public class InfusionAltarBlock extends ManaContainerBlock implements IPedestalBlock {
    public static final MapCodec<InfusionAltarBlock> CODEC = simpleCodec(InfusionAltarBlock::new);

    protected InfusionAltarBlock(Properties properties, int maxMana, int maxFlow) {
        super(properties, maxMana, maxFlow);
    }

    protected InfusionAltarBlock(Properties properties) {
        this(properties, 0, 0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 4, 14),
            Block.box(4, 4, 4, 12, 10, 12),
            Block.box(0, 10, 0, 16, 16, 16)
    );

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfusionAltarBlockEntity(blockPos, blockState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.INFUSION_ALTAR_ENTITY.get());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return interact(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof InfusionAltarBlockEntity) {
                player.openMenu((MenuProvider) blockentity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 dir = new Vec3(0, 1, 0);
        Function3<Level, Vec3, Element, ParticleEffectSpawner> topSup = (lvl, vec, elm) -> PresetHelper.longer(SquareParticles.squareParticle(lvl, vec, elm));
        Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier = (lvl, vec, elm) -> PresetHelper.longer(SquareParticles.squareShrinkParticle(lvl, vec, elm));
        PointVFX.ring(level, pos.getCenter().add(dir.scale(0.5)), Element.MANA, topSup, dir, 1, 0.03F, 0.0F, 0.0F);
        PointVFX.ring(level, pos.getCenter().add(dir.scale(-0.5)), Element.MANA, supplier, dir.reverse(), 1, 0.05F, 0.05F, 0.0F);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof InfusionAltarBlockEntity infuserBlockEntity) {
                infuserBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
