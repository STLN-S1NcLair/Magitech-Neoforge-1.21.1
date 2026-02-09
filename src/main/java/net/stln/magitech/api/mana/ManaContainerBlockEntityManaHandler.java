package net.stln.magitech.api.mana;

import net.minecraft.world.item.ItemStack;

public class ManaContainerBlockEntityManaHandler implements IBlockManaHandler {
    protected final IManaContainerBlockEntity be;

    protected final boolean canReceive;
    protected final boolean canExtract;

    public ManaContainerBlockEntityManaHandler(IManaContainerBlockEntity be, float flowBias, boolean canReceive, boolean canExtract) {
        this.be = be;
        this.canReceive = canReceive;
        this.canExtract = canExtract;
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
    public long receiveMana(long maxReceive, boolean simulate) {
        if (!canReceive) {
            return 0;
        }
        // 流量制限による受入可能残量 = (許容上限) - (現在の量)
        long flowCapacity = Math.clamp(getMaxFlow() - be.getCurrentTickTransfer(), 0, getMaxFlow());

        // タンク容量による空き容量
        long tankCapacity = getMaxMana() - getMana();

        // すべての条件の中で最小の値を採用
        long accepted = Math.min(maxReceive, Math.min(flowCapacity, tankCapacity));

        if (!simulate && accepted > 0) {
            setMana(getMana() + accepted);
            be.setCurrentTickTransfer(be.getCurrentTickTransfer() + accepted);
        }
        return accepted;
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        if (!canExtract) {
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
}
