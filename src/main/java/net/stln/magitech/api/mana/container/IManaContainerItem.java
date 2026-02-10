package net.stln.magitech.api.mana.container;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.ManaFlowRule;

public interface IManaContainerItem {

    // Handlerに依存せずに共通で使うもの: マナ量、流量

    long getMana();

    long getMaxMana();

    long getMaxFlow();

    BlockState getBlockState();

    // 充填率バイアス、入出力可否を取得する。
    ManaFlowRule getManaFlowRule(BlockState state, Direction side);

    void setMana(long mana);

    void setMaxMana(long maxMana);

    void setMaxFlow(long maxFlow);
}
