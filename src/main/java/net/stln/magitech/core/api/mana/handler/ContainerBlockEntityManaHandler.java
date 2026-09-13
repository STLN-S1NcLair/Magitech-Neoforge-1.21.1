package net.stln.magitech.core.api.mana.handler;

import net.minecraft.core.Direction;
import net.stln.magitech.core.api.mana.container.IManaContainerBlockEntity;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;

/**
 * ブロックエンティティのマナ容器を NeoForge handler として公開します。
 * Exposes a block-entity mana container as a NeoForge-style handler.
 */
public class ContainerBlockEntityManaHandler implements IBlockManaHandler {
    protected final IManaContainerBlockEntity be;
    protected final Direction side;

    /**
     * ブロックエンティティと接続面からハンドラーを生成します。
     * Creates a handler from a block entity and its connected face.
     */
    public ContainerBlockEntityManaHandler(IManaContainerBlockEntity be, Direction side) {
        this.be = be;
        this.side = side;
    }

    /**
     * 現在のマナ量を返します。
     * Returns the current mana amount.
     */
    @Override
    public long getMana() {
        return be.getMana();
    }

    /**
     * マナ量を容量範囲内に制限して設定します。
     * Sets the mana amount clamped to the capacity range.
     */
    @Override
    public void setMana(long value) {
        be.setMana(Math.clamp(value, 0, getMaxMana()));
    }

    /**
     * 最大マナ容量を返します。
     * Returns the maximum mana capacity.
     */
    @Override
    public long getMaxMana() {
        return be.getMaxMana();
    }

    /**
     * 1 tick あたりの最大転送量を返します。
     * Returns the maximum transfer amount per tick.
     */
    @Override
    public long getMaxFlow() {
        return be.getMaxFlow();
    }

    /**
     * マナの挿入可能量を計算し、必要なら挿入します。
     * Calculates the insertable amount and inserts it when requested.
     */
    @Override
    public long insertMana(long maxInsert, boolean simulate) {
        if (!getManaFlowRule().canInsert()) {
            return 0;
        }
        // 流量制限による受入可能残量 = (許容上限) - (現在の量) / Remaining insertion capacity from the flow limit = allowed upper limit - current amount
        long flowCapacity = Math.clamp(getMaxFlow() - be.getCurrentTickTransfer(), 0, getMaxFlow());

        // タンク容量による空き容量 / Free capacity from the tank capacity
        long tankCapacity = getMaxMana() - getMana();

        // すべての条件の中で最小の値を採用 / Use the smallest value among all constraints
        long accepted = Math.min(maxInsert, Math.min(flowCapacity, tankCapacity));

        if (!simulate && accepted > 0) {
            setMana(getMana() + accepted);
            be.setCurrentTickTransfer(be.getCurrentTickTransfer() + accepted);
        }
        return accepted;
    }

    /**
     * マナの排出可能量を計算し、必要なら排出します。
     * Calculates the extractable amount and extracts it when requested.
     */
    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        if (!getManaFlowRule().canExtract()) {
            return 0;
        }
        // 流量制限による排出可能残量 = (現在の量) - (許容下限) / Remaining extraction capacity from the flow limit = current amount - allowed lower limit
        long flowCapacity = Math.clamp(getMaxFlow() + be.getCurrentTickTransfer(), 0, getMaxFlow());
        // 最小値を採用 / Use the minimum value
        long extracted = Math.min(maxExtract, Math.min(flowCapacity, getMana()));

        if (!simulate && extracted > 0) {
            setMana(getMana() - extracted);
            be.setCurrentTickTransfer(be.getCurrentTickTransfer() - extracted);
        }
        return extracted;
    }

    /**
     * 接続面に適用される流量ルールを返します。
     * Returns the flow rule applied to the connected face.
     */
    @Override
    public ManaFlowRule getManaFlowRule() {
        return be.getManaFlowRule(be.getBlockState(), side);
    }
}
