package net.stln.magitech.api.mana;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.util.ComponentHelper;

public class ItemManaHandler implements IManaHandler {
    private final ItemStack stack;
    private final long capacity;
    private final long maxFlow;

    public ItemManaHandler(ItemStack stack, long capacity, long maxFlow) {
        this.stack = stack;
        this.capacity = capacity;
        this.maxFlow = maxFlow;
    }

    @Override
    public long getMana() {
        return ComponentHelper.getMana(stack);
    }

    @Override
    public void setMana(long value) {
        ComponentHelper.updateMana(stack, mana -> Math.clamp(value, 0, capacity));
    }

    @Override
    public long getMaxMana() {
        return capacity;
    }

    @Override
    public long getPrevMana() {
        // アイテムでは「前回値」を保持しないため、現在値を返して
        // 「差分なし」として扱うのが安全。
        return getMana();
    }

    @Override
    public long getMaxFlow() {
        return maxFlow;
    }

    @Override
    public long receiveMana(long maxReceive, boolean simulate) {
        // アイテムの場合は「今回の要求量」を maxFlow で制限するだけにする
        long effectiveRate = Math.min(maxReceive, maxFlow);

        long current = getMana();
        long accepted = Math.min(capacity - current, effectiveRate);

        if (!simulate && accepted > 0) {
            setMana(current + accepted);
        }
        return accepted;
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        // 同様に「今回の要求量」を maxFlow で制限
        long effectiveRate = Math.min(maxExtract, maxFlow);

        long current = getMana();
        long extracted = Math.min(current, effectiveRate);

        if (!simulate && extracted > 0) {
            setMana(current - extracted);
        }
        return extracted;
    }
}
