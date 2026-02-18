package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.ManaFlowRule;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.InfuserBlock;
import net.stln.magitech.gui.InfuserMenu;
import org.jetbrains.annotations.Nullable;

public class InfuserBlockEntity extends ManaMachineBlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    protected int progress = 0;
    protected int maxProgress = 200;

    public final ContainerData machineData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> InfuserBlockEntity.this.getProgress();
                case 1 -> InfuserBlockEntity.this.getMaxProgress();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> InfuserBlockEntity.this.setProgress(value);
                case 1 -> InfuserBlockEntity.this.setMaxProgress(value);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };


    public InfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.INFUSER_ENTITY.get(), pos, blockState, 3000000, 3000);
    }

    public void clearContents() {
        for (int i = 0; i < items.size(); i++) {
            items.set(i, ItemStack.EMPTY);
            setChanged();
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        this.progress++;
        if (this.progress > this.maxProgress) {
            this.progress = 0;
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) {
            inv.setItem(i, items.get(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new InfuserMenu(containerId, inventory, this, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess, this.machineData);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.infuser");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public int getContainerSize() {
        return 2;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side == state.getValue(InfuserBlock.FACING).getOpposite() || side == Direction.DOWN) {
            return ManaFlowRule.BothWays(-1.0F);
        }
        return ManaFlowRule.None();
    }
}
