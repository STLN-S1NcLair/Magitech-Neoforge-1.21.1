package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.AbstractManaPumpBlock;
import net.stln.magitech.content.block.ManaVesselBlock;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractManaPumpBlockEntity extends ManaContainerBlockEntity {

    public AbstractManaPumpBlockEntity(BlockEntityType<? extends AbstractManaPumpBlockEntity> type, BlockPos pos, BlockState blockState, long mana) {
        super(type, pos, blockState, mana);
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
    public abstract AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player);

    @Override
    protected abstract Component getDefaultName();

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    public int getContainerSize() {
        return 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || state.getValue(BlockStateProperties.POWERED)) {
            return ManaFlowRule.bothWays(0.0F);
        }
        if (side == state.getValue(AbstractManaPumpBlock.FACING)) {
            return ManaFlowRule.extractOnly(1.0F);
        }
        if (side == state.getValue(AbstractManaPumpBlock.FACING).getOpposite()) {
            return ManaFlowRule.insertOnly(-1.0F);
        }
        return ManaFlowRule.none();
    }
}
