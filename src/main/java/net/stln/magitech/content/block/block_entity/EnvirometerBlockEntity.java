package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.stln.magitech.api.machine.inspection.IMachineInspectionTarget;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import org.jetbrains.annotations.Nullable;

public class EnvirometerBlockEntity extends BlockEntity implements IMachineInspectionTarget {
    public EnvirometerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.ENVIROMETER_ENTITY.get(), pos, blockState);
    }

    @Override
    public BlockPos getInspectionPosition() {
        return worldPosition;
    }

    @Override
    public @Nullable IBlockManaHandler getInspectionManaHandler() {
        return null;
    }

    @Override
    public @Nullable IItemHandler getInspectionItemHandler() {
        return null;
    }

    @Override
    public boolean canInspectWithoutMana() {
        return true;
    }

    @Override
    public boolean showFieldEffectInfluencesInInspection() {
        return true;
    }
}
