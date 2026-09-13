package net.stln.magitech.core.api.mana.container;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;

/**
 * ブロックエンティティ型のマナ容器が公開する共通 API です。
 * Common API exposed by block-entity mana containers.
 */
public interface IManaContainerBlockEntity {

    // Handlerに依存せずに共通で使うもの: マナ量、流量 / Common values independent of the handler: mana amount and flow rate

    /**
     * 現在のマナ量を返します。
     * Returns the current mana amount.
     */
    long getMana();

    /**
     * 最大マナ容量を返します。
     * Returns the maximum mana capacity.
     */
    long getMaxMana();

    /**
     * 1 tick あたりの最大転送量を返します。
     * Returns the maximum transfer amount per tick.
     */
    long getMaxFlow();

    /**
     * 現在の tick で転送済みの量を返します。
     * Returns the amount transferred during the current tick.
     */
    // 1tickあたりの現在の転送量を取得 / Gets the current transfer amount for this tick
    long getCurrentTickTransfer();

    /**
     * 現在のブロック状態を返します。
     * Returns the current block state.
     */
    BlockState getBlockState();

    // 充填率バイアス、入出力可否を取得する。 / Gets the fill-ratio bias and whether insertion or extraction is allowed.
    /**
     * 面ごとのマナ流量ルールを返します。
     * Returns the mana flow rule for a block face.
     */
    ManaFlowRule getManaFlowRule(BlockState state, Direction side);

    /**
     * 現在のマナ量を設定します。
     * Sets the current mana amount.
     */
    void setMana(long mana);

    /**
     * 最大マナ容量を設定します。
     * Sets the maximum mana capacity.
     */
    void setMaxMana(long maxMana);

    /**
     * 1 tick あたりの最大転送量を設定します。
     * Sets the maximum transfer amount per tick.
     */
    void setMaxFlow(long maxFlow);

    /**
     * 現在の tick の転送量を設定します。
     * Sets the transfer amount for the current tick.
     */
    void setCurrentTickTransfer(long value);
}
