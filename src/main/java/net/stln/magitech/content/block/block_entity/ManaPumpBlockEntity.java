package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.gui.ManaPumpMenu;
import net.stln.magitech.content.gui.ManaVesselMenu;

public class ManaPumpBlockEntity extends AbstractManaPumpBlockEntity {

    public ManaPumpBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_PUMP_ENTITY.get(), pos, blockState, mana);
    }

    public ManaPumpBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ManaPumpMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_pump");
    }
}
