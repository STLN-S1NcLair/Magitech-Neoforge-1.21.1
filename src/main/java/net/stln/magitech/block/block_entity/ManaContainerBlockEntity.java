package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.container.IManaContainerBlockEntity;
import net.stln.magitech.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.api.mana.flow.network.connectable.IWiredEndpointManaContainer;
import net.stln.magitech.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.api.mana.handler.ContainerBlockEntityManaHandler;
import net.stln.magitech.util.LongContainerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ManaContainerBlockEntity extends BaseContainerBlockEntity implements IManaContainerBlockEntity, IWiredEndpointManaContainer {

    protected long mana;
    protected long maxMana;
    protected long maxFlow;
    // そのTick内に移動した合計量 (毎Tickリセット)
    protected long currentTickTransfer = 0;
    // 表示用の滑らかな平均流量 (保存不要)
    protected float averageFlow = 0;

    // キャッシュ(ネットワーク軽量化のため、毎Tickの再探索を避ける)
    protected Map<Direction, Set<IBasicManaHandler>> cachedSinks = null;
    protected static final int MAX_HOPS = 64;
    // スキャンリクエスト
    protected boolean needsNetworkRescan = true;


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
        this.ticksSinceLastNetworkRebuild = level.random.nextInt(100); // 初期化時の同時更新を避けるためランダムオフセット
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

    // 充填率バイアス、入出力可否はgetFlowRuleで取得する
    public ContainerBlockEntityManaHandler getManaHandler(Direction side) {
        if (getManaFlowRule(getBlockState(), side).isNone()) {
            return null;
        }
        return new ContainerBlockEntityManaHandler(this, side);
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
        this.ticksSinceLastNetworkRebuild++;
        // ネットワーク再スキャン処理
        if (this.needsNetworkRescan || this.ticksSinceLastNetworkRebuild >= 100) {
            // 定期的 or リクエストによる再スキャン
            rescanNetwork(level);
            this.needsNetworkRescan = false;
        } else {
            // 変化検出による再スキャンリクエスト
            rescanNearbyNetwork(level);
        }
        // 隣接ブロックへのマナ移動処理
        transferManaTick(level, pos, state);
    }

    protected void transferManaTick(Level level, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            Set<IBasicManaHandler> sinks = cachedSinks.get(dir);
            if (sinks.isEmpty()) continue;
            IBasicManaHandler source = ManaTransferHelper.getManaContainer(level, pos, dir);
            ManaTransferHelper.balance(source, sinks);
        }
    }

    protected void rescanNearbyNetwork(Level level) {
        // 毎Tick Fingerprintを計算して変化を検出
        ManaNetworkHelper.NetworkFingerprint currentFingerprint = getCurrentFingerprint(level);
        if (!currentFingerprint.equals(this.cachedNetworkFingerprint)) {
            // 変化あり
            this.requestRescan();
        }
    }

    protected void rescanNetwork(Level level) {
        Map<Direction, ManaNetworkHelper.NetworkSnapshot> newSnapshot = new HashMap<>();
        for (Direction dir : Direction.values()) {
            newSnapshot.put(dir, getNetworkSnapshot(level, dir));
        }
        if (!newSnapshot.equals(this.cachedNetworkSnapshot)) {
            // 変化あり
            this.cachedNetworkSnapshot = newSnapshot;
            this.cachedNetworkFingerprint = getCurrentFingerprint(level);
            this.ticksSinceLastNetworkRebuild = 0;
            // キャッシュ再構築
            rebuildCachedSinks(level, this.worldPosition);
        }
    }

    protected void rebuildCachedSinks(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            this.cachedSinks.put(dir, ManaNetworkHelper.findNetworkWithConnector(level, pos, dir, MAX_HOPS, true));
        }
    }

    private ManaNetworkHelper.@NotNull NetworkFingerprint getCurrentFingerprint(Level level) {
        return ManaNetworkHelper.computeFingerprint(level, this.worldPosition, MAX_HOPS);
    }

    private ManaNetworkHelper.@NotNull NetworkSnapshot getNetworkSnapshot(Level level, Direction dir) {
        return ManaNetworkHelper.getConnectorNetworkSnapshot(level, this.worldPosition, dir, MAX_HOPS);
    }

    public void requestRescan() {
        this.needsNetworkRescan = true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void onRemove() {
        rebuildCachedSinks(this.level, this.worldPosition);
        for (Direction dir : Direction.values()) {
            Set<IBasicManaHandler> sinks = cachedNetworkSnapshot.get(dir);
            for (IBasicManaHandler sink : sinks) {
                if (sink != null) {
                    sink.get(this);
                }
            }
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

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return getDefaultName();
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
    public Set<Direction> getConnectableDirections(BlockState state) {
        Set<Direction> connectableDirs = Set.of();
        for (Direction dir : Direction.values()) {
            if (!getManaFlowRule(state, dir).isNone()) {
                connectableDirs.add(dir);
            }
        }
        return connectableDirs;
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
