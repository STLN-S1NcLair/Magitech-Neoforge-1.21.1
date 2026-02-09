package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.*;
import net.stln.magitech.util.LongContainerData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ManaContainerBlockEntity extends BaseContainerBlockEntity implements IManaContainerBlockEntity {

    protected long mana;
    protected long maxMana;
    protected long maxFlow;
    // そのTick内に移動した合計量 (毎Tickリセット)
    protected long currentTickTransfer = 0;
    // 表示用の滑らかな平均流量 (保存不要)
    protected float averageFlow = 0;


    public LongContainerData dataAccess = new LongContainerData() {

        @Override
        public long getLong(int index) {
            return switch (index) {
                case 0 -> ManaContainerBlockEntity.this.getMana();
                case 1 -> ManaContainerBlockEntity.this.getMaxMana();
                case 2 -> ManaContainerBlockEntity.this.getFlowRate();
                case 3 -> ManaContainerBlockEntity.this.getMaxFlow();
                default -> 0;
            };
        }

        @Override
        public void setLong(int index, long value) {
            if (index == 0) {
                ManaContainerBlockEntity.this.mana = Math.clamp(value, 0, ManaContainerBlockEntity.this.maxMana);
            }
        }

        @Override
        public int getLongCount() {
            return 4;
        }
    };

    public ManaContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long mana, long maxMana, long maxManaFlow) {
        super(type, pos, blockState);
        this.mana = mana;
        this.maxMana = maxMana;
        this.maxFlow = maxManaFlow;
    }

    public ManaContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long maxMana, long maxManaFlow) {
        this(type, pos, blockState, 0, maxMana, maxManaFlow);
    }

    public ManaContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long maxMana) {
        this(type, pos, blockState, 0, maxMana, Long.MAX_VALUE);
    }

    public ManaContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        this(type, pos, blockState, 0, 1000, 1000);
    }

    protected final IBasicManaHandler BOTH_SIDE = new ManaContainerBlockEntityManaHandler(this, true, true);
    protected final IBasicManaHandler INPUT_ONLY = new ManaContainerBlockEntityManaHandler(this, true, false);
    protected final IBasicManaHandler OUTPUT_ONLY = new ManaContainerBlockEntityManaHandler(this, false, true);

    // 側面指定なしの場合は内部アクセスとみなす
    // 設定する場合はオーバーライドする
    public IBasicManaHandler getManaHandler(BlockState state, Direction side) {
        if (side == null) {
            return getInternalHandler();
        }
        return getExternalHandler(state, side);
    }

    // 外部アクセス用ハンドラを返す
    protected abstract IBasicManaHandler getExternalHandler(BlockState state, Direction side);

    // 内部アクセス用ハンドラを返す
    protected IBasicManaHandler getInternalHandler() {
        return BOTH_SIDE;
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, ManaContainerBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        setChanged(level, pos, state);

        // Tickの最後に流量の平均化を行う
        this.updateFlowAverage();
        // Tick終了時にリセット
        this.currentTickTransfer = 0;
        // 隣接ブロックへのマナ移動処理
        transferManaTick(level, pos, state);
    }

    protected void transferManaTick(Level level, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            List<IBasicManaHandler> sinks = ManaNetworkHelper.findNetworkWithConnector(level, pos, dir, true);
            if (sinks.isEmpty()) continue;
            IBasicManaHandler source = ManaTransferHelper.getManaContainer(level, pos, dir);
            ManaTransferHelper.balance(source, sinks);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.mana = tag.getLong("mana");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("mana", this.mana);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.putLong("mana", this.mana);
        return tag;
    }

    protected void updateFlowAverage() {
        // 平滑化係数 (0.05 ～ 0.1 くらいがおすすめ)
        // 値が小さいほど変化がゆっくりになり、大きいほど敏感になる
        // 0.05 なら、約1秒(20tick)かけて目標値に追従するイメージ
        float factor = 0.05f;

        // 計算式: 新しい平均 = (今の平均 * (1 - 係数)) + (今回の値 * 係数)
        this.averageFlow = (this.averageFlow * (1.0f - factor)) + (this.currentTickTransfer * factor);

        // 完全に停止したときに 0.001 みたいな小さな値が残るのを防ぐ
        if (Math.abs(this.averageFlow) < 0.1f) {
            this.averageFlow = 0;
        }
    }

    @Override
    public long getMana() {
        return mana;
    }

    @Override
    public void setMana(long mana) {
        this.mana = mana;
    }

    @Override
    public long getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(long maxMana) {
        this.maxMana = maxMana;
    }

    @Override
    public long getMaxFlow() {
        return maxFlow;
    }

    @Override
    public void setMaxFlow(long maxFlow) {
        this.maxFlow = maxFlow;
    }

    @Override
    public long getCurrentTickTransfer() {
        return currentTickTransfer;
    }

    @Override
    public void setCurrentTickTransfer(long currentTickTransfer) {
        this.currentTickTransfer = currentTickTransfer;
    }

    public int getFlowRate() {
        return Math.round(this.averageFlow);
    }
}
