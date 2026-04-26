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
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaVesselBlock;
import net.stln.magitech.content.gui.ManaVesselMenu;
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

public class ManaVesselBlockEntity extends ManaContainerBlockEntity implements GeoBlockEntity {
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ItemStackHandler inventory = new ItemStackHandler(2);

    public ManaVesselBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.MANA_VESSEL_ENTITY.get(), pos, blockState, mana);
    }

    public ManaVesselBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
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
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
            setChanged();
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        ItemStack source = inventory.getStackInSlot(INPUT);
        ItemStack sink = inventory.getStackInSlot(OUTPUT);
        IBasicManaHandler sinkHandler = source.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM, null);
        IBasicManaHandler sourceHandler = sink.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM, null);
        IBasicManaHandler handler = this.getManaHandler(null);
        ManaTransferHelper.transferMana(handler, sinkHandler);
        ManaTransferHelper.transferMana(sourceHandler, handler);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
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
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_vessel");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.set(i, inventory.getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, items.get(i));
        }
    }

    public int getContainerSize() {
        return 2;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side.getAxis() == state.getValue(ManaVesselBlock.AXIS)) {
            return ManaFlowRule.bothWays(0.0F);
        }
        return ManaFlowRule.none();
    }
}
