package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.content.block.block_entity.ManaLanternBlockEntity;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.effect.visual.spawner.StarParticles;
import net.stln.magitech.feature.element.Element;

public class ManaLanternBlock extends LanternBlock implements EntityBlock {
    public static final MapCodec<LanternBlock> CODEC = simpleCodec(ManaLanternBlock::new);
    private static final VoxelShape NORMAL_SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 9, 12),
            Block.box(5, 9, 5, 11, 11, 11)
    );
    private static final VoxelShape HANGING_SHAPE = Shapes.or(
            Block.box(4, 2, 4, 12, 11, 12),
            Block.box(5, 11, 5, 11, 13, 11)
    );

    public ManaLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<LanternBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : NORMAL_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : NORMAL_SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaLanternBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide || !blockEntityType.equals(BlockInit.MANA_LANTERN_ENTITY.get())) {
            return null;
        }
        return (tickLevel, tickPos, tickState, blockEntity) -> {
            if (blockEntity instanceof ManaLanternBlockEntity manaLantern) {
                manaLantern.clientTick(tickLevel, tickPos);
            }
        };
    }

    public static void spawnParticles(Level level, BlockPos pos, double verticalOffset, long tick) {
        if (tick % 4 != 0) {
            return;
        }
        Vec3 center = pos.getCenter().add(0, -0.2 + verticalOffset, 0);
        PointVFX.spray(level, center, Element.MANA, ((level1, vec3, element) -> PresetHelper.noClip(PresetHelper.smaller(SquareParticles.squareShrinkParticle(level1, vec3, element)))), new Vec3(0, 1, 0), 1, 0.02F * (tick % 8 == 0 ? 1 : -1), 0.01F);

        PointVFX.spray(level, center, Element.MANA, StarParticles::starParticle, new Vec3(0, 0, 0), 1, 0.0F, 0.001F);
    }
}
