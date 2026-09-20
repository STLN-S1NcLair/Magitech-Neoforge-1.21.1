package net.stln.magitech.core.api.mana.container;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;

/**
 * アイテム型のマナ容器が公開する共通 API です。
 * Common API exposed by item-based mana containers.
 */
public interface IManaContainerItem {

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
     * 容器が使用するブロック状態を返します。
     * Returns the block state used by the container.
     */
    BlockState getBlockState();

    // 充填率バイアス、入出力可否を取得する。 / Gets the fill-ratio bias and whether insertion or extraction is allowed.
    /**
     * 指定面に対するマナ流量ルールを返します。
     * Returns the mana flow rule for the specified face.
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
}
