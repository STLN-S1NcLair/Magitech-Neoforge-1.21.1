package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.block.block_entity.InfuserBlockEntity;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.util.VoxelShapeUtil;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class InfuserBlock extends ManaContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<InfuserBlock> CODEC = simpleCodec(InfuserBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("block.magitech.infuser");

    protected InfuserBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16),
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(2, 4, 13, 14, 12, 16),
            Block.box(0, 4, 4, 2, 12, 12),
            Block.box(14, 4, 4, 16, 12, 12)
    );
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeUtil.rotateShape(SHAPE_NORTH, Direction.NORTH, Direction.WEST);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case Direction.NORTH -> SHAPE_NORTH;
            case Direction.SOUTH -> SHAPE_SOUTH;
            case Direction.EAST -> SHAPE_EAST;
            case Direction.WEST -> SHAPE_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(FACING));
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfuserBlockEntity(blockPos, blockState);
    }

    @Override
    public @org.jetbrains.annotations.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTicker(level, blockEntityType, BlockInit.INFUSER_ENTITY.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof InfuserBlockEntity) {
                player.openMenu((MenuProvider) blockentity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        double x = center.x + Mth.nextDouble(random, -0.4, 0.4);
        double y = center.y + Mth.nextDouble(random, -0.4, 0.4);
        double z = center.z + Mth.nextDouble(random, -0.4, 0.4);
        level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, 0, 15, 1.0F), x, y, z, 0, 0, 0);
        for (int i = 0; i < 2; i++) {
            double x2 = center.x + Mth.nextDouble(random, -0.3, 0.3);
            double y2 = center.y + Mth.nextDouble(random, -0.3, 0.3);
            double z2 = center.z + Mth.nextDouble(random, -0.3, 0.3);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.0F), x2, y2, z2, 0, 0.03, 0);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof InfuserBlockEntity infuserBlockEntity) {
                infuserBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
