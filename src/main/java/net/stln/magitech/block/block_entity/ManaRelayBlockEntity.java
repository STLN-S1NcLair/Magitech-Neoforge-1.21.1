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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.api.mana.IManaNode;
import net.stln.magitech.api.mana.ManaNodeLogicHelper;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.ManaNodeBlock;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ManaRelayBlockEntity extends ManaContainerBlockEntity implements IManaNode {

    // ★ ノード機能を委譲するためのロジッククラス
    private final ManaNodeLogicHelper nodeLogic;


    public ManaRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long maxMana, long maxManaFlow) {
        super(type, pos, blockState);
        this.mana = 0;
        this.maxMana = maxMana;
        this.maxFlow = maxManaFlow;
        this.nodeLogic = new ManaNodeLogicHelper(this, this);
    }

    public ManaRelayBlockEntity(BlockPos pos, BlockState blockState) {
        this(BlockInit.MANA_RELAY_ENTITY.get(), pos, blockState, 50000, 5000);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaRelayBlockEntity entity) {
        entity.tick(level, pos, state);
        entity.nodeLogic.tick(level, pos, state);
    }

    @Override
    public void requestRescan() {
        this.nodeLogic.requestRescan();
    }

    @Override
    public long receiveMana(long maxReceive, boolean simulate) {
        // ★ ここで条件分岐
        // 「もし有効な送信先がないなら、受け入れを拒否する (0を返す)」
        // これにより、ネットワークの末端や孤立したリレーにはマナが入らなくなる
        if (!nodeLogic.hasValidTarget()) {
            return 0;
        }

        // 送信先があるなら、親クラス(Container)の通常のロジックで受け取る
        // (容量がいっぱいになるまで受け取る = Maxまで保持)
        return super.receiveMana(maxReceive, simulate);
    }

    // --- その他 ---

    @Override
    public boolean canReceiveMana(Direction direction, BlockPos pos, BlockState state) {
        // マナノードを介してのみアクセスできる
        return false;
    }

    @Override
    public boolean canExtractMana(Direction direction, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.magitech.mana_relay");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.withSize(0, ItemStack.EMPTY);
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}