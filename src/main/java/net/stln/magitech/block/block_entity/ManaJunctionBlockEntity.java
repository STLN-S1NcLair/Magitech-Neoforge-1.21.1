package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.block.BlockInit;

import java.util.ArrayList;
import java.util.List;

public class ManaJunctionBlockEntity extends BlockEntity {

    public ManaJunctionBlockEntity(BlockEntityType type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public ManaJunctionBlockEntity(BlockPos pos, BlockState blockState) {
        this(BlockInit.MANA_JUNCTION_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaJunctionBlockEntity entity) {
        List<IBlockManaHandler.TargetInfo> targetInfos = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof IBlockManaHandler target) {
                targetInfos.add(new IBlockManaHandler.TargetInfo(target, neighborPos, direction));
            }
        }
        for (IBlockManaHandler.TargetInfo targetInfo : targetInfos) {
            List<IBlockManaHandler.TargetInfo> otherTargets = new ArrayList<>();
            for (IBlockManaHandler.TargetInfo otherTargetInfo : targetInfos) {
                if (otherTargetInfo.pos() != targetInfo.pos()) {
                    // 各入出力方向を指定
                    otherTargets.add(new IBlockManaHandler.TargetInfo(otherTargetInfo.handler(), otherTargetInfo.pos(), targetInfo.targetDirection(), otherTargetInfo.targetDirection()));
                }
            }
            targetInfo.handler().transferMana(level, targetInfo.pos(), level.getBlockState(targetInfo.pos()), otherTargets);
        }
    }
}