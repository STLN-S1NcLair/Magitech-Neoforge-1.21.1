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
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.Capabilities;
import net.stln.magitech.api.mana.IManaHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.gui.ManaVesselMenu;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ManaVesselBlockEntity extends ManaContainerBlockEntity implements GeoBlockEntity, MenuProvider {
    public static final int RECEIVE_SLOT = 0;
    public static final int EXTRACT_SLOT = 1;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public ManaVesselBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockInit.MANA_VESSEL_ENTITY.get(), pos, blockState, 3000000, 3000);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
        ItemStack receive = items.get(RECEIVE_SLOT);
        ItemStack extract = items.get(EXTRACT_SLOT);
        IManaHandler receiveCapability = receive.getCapability(Capabilities.MANA_CONTAINER_ITEM, null);
        IManaHandler extractCapability = extract.getCapability(Capabilities.MANA_CONTAINER_ITEM, null);
        IManaHandler.transferMana(this, receiveCapability);
        IManaHandler.transferMana(extractCapability, this);
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
        return new ManaVesselMenu(containerId, inventory, this, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.magitech.mana_vessel");
    }

    @Override
    protected Component getDefaultName() {
        return null;
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

    @Override
    public boolean canReceiveMana(Direction direction, BlockPos pos, BlockState state) {
        return switch (direction) {
            case UP, DOWN -> true;
            default -> false;
        };
    }

    @Override
    public boolean canExtractMana(Direction direction, BlockPos pos, BlockState state) {
        return switch (direction) {
            case UP, DOWN -> true;
            default -> false;
        };
    }
}
