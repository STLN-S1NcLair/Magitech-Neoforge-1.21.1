package net.stln.magitech.content.block.block_entity;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaCollectorBlock;
import net.stln.magitech.content.block.ManaStranderBlock;
import net.stln.magitech.content.gui.ItemCollectorMenu;
import net.stln.magitech.content.gui.ManaCollectorMenu;
import net.stln.magitech.content.network.ItemCollectorCollectPayload;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.CombatHelper;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

import java.util.List;

public class ItemCollectorBlockEntity extends ManaMachineBlockEntity {

    protected long consumptionRate = 50;

    public ItemCollectorBlockEntity(BlockPos pos, BlockState blockState, long mana) {
        super(BlockInit.ITEM_COLLECTOR_ENTITY.get(), pos, blockState, mana);
    }

    public ItemCollectorBlockEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
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
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        collectItem(level, pos, state);
    }

    protected void collectItem(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.POWERED)) return; // レッドストーン信号がある場合は動作しない
        MachineBlockEntityManaHandler handler = getManaHandler(null);
        if (handler.getMana() < consumptionRate) return; // マナが足りない場合は動作しない
        Direction direction = state.getValue(ManaCollectorBlock.FACING);
        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(direction.getOpposite()), direction);
        if (itemHandler == null) return; // アイテムハンドラーがない場合は動作しない
        List<ItemEntity> items = CombatHelper.getEntitiesInBox(level, null, pos.getCenter(), new Vec3(5, 5, 5)).stream()
                .filter(e -> e instanceof ItemEntity)
                .map(e -> (ItemEntity) e).toList();
        for (ItemEntity item : items) {
            ItemStack stack = item.getItem().copy();
            if (stack.isEmpty()) continue;
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                stack = itemHandler.insertItem(i, stack, false);
                if (stack.isEmpty()) {
                    item.discard();
                    break;
                }
            }
            if (stack.equals(item.getItem())) continue; // アイテムが全く減らなかった場合は次のアイテムへ
            item.setItem(stack);
             // アイテムが減った場合はエフェクトを出す
            PacketDistributor.sendToAllPlayers(new ItemCollectorCollectPayload(this.getBlockPos(), item.position().toVector3f()));
        }
        handler.consumeMana(consumptionRate);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ItemCollectorMenu(containerId, inventory, ContainerLevelAccess.create(level, this.getBlockPos()), this.dataAccess);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.magitech.mana_collector");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public ManaFlowRule getManaFlowRule(BlockState state, Direction side) {
        if (side == null || side == state.getValue(ManaStranderBlock.FACING)) {
            return ManaFlowRule.bothWays(-1.0F);
        }
        return ManaFlowRule.none();
    }
}
