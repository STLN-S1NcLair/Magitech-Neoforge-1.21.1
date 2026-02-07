package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.IBlockManaHandler;
import net.stln.magitech.util.LongContainerData;
import org.jetbrains.annotations.Nullable;

public abstract class ManaContainerBlockEntity extends BaseContainerBlockEntity implements IBlockManaHandler {

    long mana;
    long prevMana;
    long producedMana;
    long consumedMana;
    long maxMana;
    long maxFlow;

    // そのTick内に移動した合計量 (毎Tickリセット)
    private long currentTickTransfer = 0;

    // 表示用の滑らかな平均流量 (保存不要)
    private float averageFlow = 0;
    private float averageProduce = 0;
    private float averageConsumption = 0;


    public final LongContainerData dataAccess = new LongContainerData() {

        @Override
        public long getLong(int index) {
            return switch (index) {
                case 0 -> ManaContainerBlockEntity.this.getMana();
                case 1 -> ManaContainerBlockEntity.this.getMaxMana();
                case 2 -> ManaContainerBlockEntity.this.getFlowRate();
                case 3 -> ManaContainerBlockEntity.this.getMaxFlow();
                case 4 -> ManaContainerBlockEntity.this.getProductionRate();
                case 5 -> ManaContainerBlockEntity.this.getConsumptionRate();
                default -> 0;
            };
        }

        @Override
        public void setLong(int index, long value) {
            if (index == 0) {
                ManaContainerBlockEntity.this.setMana(value);
            }
        }

        @Override
        public int getLongCount() {
            return 6;
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

    public static void ticker(Level level, BlockPos pos, BlockState state, ManaContainerBlockEntity blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        this.prevMana = this.mana;
        setChanged(level, pos, state);

        // Tickの最後に流量の平均化を行う
        this.updateFlowAverage();
        // Tick終了時にリセット
        this.currentTickTransfer = 0;
        this.producedMana = 0;
        this.consumedMana = 0;
        // 隣接ブロックへのマナ移動処理
        transferManaTick(level, pos, state);
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

    @Override
    public long getMana() {
        return mana;
    }

    // setManaは強制設定用なので制限を無視する場合が多いが、
    // 必要ならここでも clamp する
    @Override
    public void setMana(long value) {
        this.mana = Math.clamp(value, 0, maxMana);
        setChanged();
    }

    @Override
    public long getMaxMana() {
        return maxMana;
    }

    @Override
    public long getPrevMana() {
        return prevMana;
    }

    @Override
    public long getMaxFlow() {
        return maxFlow;
    }

    public void consumeMana(long amount) {
        this.setMana(Math.clamp(this.getMana() - amount, 0, this.getMaxMana()));
        this.prevMana = Math.clamp(this.prevMana - amount, 0, this.getMaxMana());
        this.consumedMana += amount;
    }

    public void produceMana(long amount) {
        this.setMana(Math.clamp(this.getMana() + amount, 0, this.getMaxMana()));
        this.prevMana = Math.clamp(this.prevMana + amount, 0, this.getMaxMana());
        this.producedMana += amount;
    }

    private void updateFlowAverage() {
        // 平滑化係数 (0.05 ～ 0.1 くらいがおすすめ)
        // 値が小さいほど変化がゆっくりになり、大きいほど敏感になる
        // 0.05 なら、約1秒(20tick)かけて目標値に追従するイメージ
        float factor = 0.05f;
        float factorProduction = 0.0025f;

        // 計算式: 新しい平均 = (今の平均 * (1 - 係数)) + (今回の値 * 係数)
        this.averageFlow = (this.averageFlow * (1.0f - factor)) + (this.currentTickTransfer * factor);

        // 完全に停止したときに 0.001 みたいな小さな値が残るのを防ぐ
        if (Math.abs(this.averageFlow) < 0.1f) {
            this.averageFlow = 0;
        }

        this.averageProduce = (this.averageProduce * (1.0f - factorProduction)) + (this.producedMana * factorProduction);
        if (Math.abs(this.averageProduce) < 0.1f) {
            this.averageProduce = 0;
        }

        this.averageConsumption = (this.averageConsumption * (1.0f - factorProduction)) + (this.consumedMana * factorProduction);
        if (Math.abs(this.averageConsumption) < 0.1f) {
            this.averageConsumption = 0;
        }
    }

    // ★マナを受け取った/出した時に呼び出す
    private void onTransfer(long amount) {
        this.currentTickTransfer += amount;
    }

    @Override
    public long receiveMana(long maxReceive, boolean simulate) {
        // 1. 許容される上限値 = (Tick開始時の量) + (最大流量)
        long limitUpper = prevMana + maxFlow;

        // 2. 流量制限による受入可能残量 = (許容上限) - (現在の量)
        // ※すでにこのTickで大量に受け取っている場合、ここが減る
        long flowCapacity = Math.clamp(limitUpper - mana, 0, maxFlow);

        // 3. タンク容量による空き容量
        long tankCapacity = maxMana - mana;

        // 4. すべての条件の中で最小の値を採用
        long accepted = Math.min(maxReceive, Math.min(flowCapacity, tankCapacity));

        if (!simulate && accepted > 0) {
            setMana(mana + accepted);
            onTransfer(accepted);
        }
        return accepted;
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        // 1. 許容される下限値 = (Tick開始時の量) - (最大流量)
        long limitLower = Math.max(0, prevMana - maxFlow);

        // 2. 流量制限による排出可能残量 = (現在の量) - (許容下限)
        long flowCapacity = Math.clamp(mana - limitLower, 0, maxFlow);

        // 3. 現在のマナ量 (これ以上は出せない)
        long currentMana = mana;

        // 4. 最小値を採用
        long extracted = Math.min(maxExtract, Math.min(flowCapacity, currentMana));

        if (!simulate && extracted > 0) {
            setMana(mana - extracted);
            onTransfer(-extracted);
        }
        return extracted;
    }

    public int getFlowRate() {
        return Math.round(this.averageFlow);
    }

    public long getProducedMana() {
        return producedMana;
    }

    public int getProductionRate() {
        return Math.round(this.averageProduce);
    }

    public long getConsumedMana() {
        return consumedMana;
    }

    public int getConsumptionRate() {
        return Math.round(averageConsumption);
    }
}
