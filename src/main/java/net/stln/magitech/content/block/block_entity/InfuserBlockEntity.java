package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.gui.InfuserMenu;

public class InfuserBlockEntity extends InfusionAltarBlockEntity {

    public InfuserBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.INFUSER_ENTITY.get(), pos, blockState, mana);
        this.maxProgress = 100;
    }

    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new InfuserMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.infuser");
    }
}
