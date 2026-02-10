package net.stln.magitech.api.mana.handler;

import net.minecraft.core.Direction;
import net.stln.magitech.api.mana.container.IManaContainerBlockEntity;
import net.stln.magitech.api.mana.flow.ManaFlowRule;

public class ContainerBlockEntityManaHandler implements IBlockManaHandler {
    protected final IManaContainerBlockEntity be;
    protected final Direction side;

    public ContainerBlockEntityManaHandler(IManaContainerBlockEntity be, Direction side) {
        this.be = be;
        this.side = side;
    }

    @Override
    public long getMana() {
        return be.getMana();
    }

    @Override
    public void setMana(long value) {
        be.setMana(Math.clamp(value, 0, getMaxFlow()));
    }

    @Override
    public long getMaxMana() {
        return be.getMaxMana();
    }

    @Override
    public long getMaxFlow() {
        return be.getMaxFlow();
    }

    @Override
    public long insertMana(long maxInsert, boolean simulate) {
        if (!getManaFlowRule().canInsert()) {
            return 0;
        }
        // 流量制限による受入可能残量 = (許容上限) - (現在の量)
        long flowCapacity = Math.clamp(getMaxFlow() - be.getCurrentTickTransfer(), 0, getMaxFlow());

        // タンク容量による空き容量
        long tankCapacity = getMaxMana() - getMana();

        // すべての条件の中で最小の値を採用
        long accepted = Math.min(maxInsert, Math.min(flowCapacity, tankCapacity));

        if (!simulate && accepted > 0) {
            setMana(getMana() + accepted);
            be.setCurrentTickTransfer(be.getCurrentTickTransfer() + accepted);
        }
        return accepted;
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        if (!getManaFlowRule().canExtract()) {
            return 0;
        }
        // 流量制限による排出可能残量 = (現在の量) - (許容下限)
        long flowCapacity = Math.clamp(getMaxFlow() + be.getCurrentTickTransfer(), 0, getMaxFlow());
        // 最小値を採用
        long extracted = Math.min(maxExtract, Math.min(flowCapacity, getMana()));

        if (!simulate && extracted > 0) {
            setMana(getMana() - extracted);
            be.setCurrentTickTransfer(be.getCurrentTickTransfer() - extracted);
        }
        return extracted;
    }

    @Override
    public ManaFlowRule getManaFlowRule() {
        return be.getManaFlowRule(be.getBlockState(), side);
    }
}
