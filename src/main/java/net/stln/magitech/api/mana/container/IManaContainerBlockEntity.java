package net.stln.magitech.api.mana.container;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.ManaFlowRule;

public interface IManaContainerBlockEntity {

    // Handlerに依存せずに共通で使うもの: マナ量、流量

    long getMana();

    long getMaxMana();

    long getMaxFlow();

    // 1tickあたりの現在の転送量を取得
    long getCurrentTickTransfer();

    BlockState getBlockState();

    // 充填率バイアス、入出力可否を取得する。
    ManaFlowRule getManaFlowRule(BlockState state, Direction side);

    void setMana(long mana);

    void setMaxMana(long maxMana);

    void setMaxFlow(long maxFlow);

    void setCurrentTickTransfer(long value);
}
