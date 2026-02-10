package net.stln.magitech.api.mana.handler;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.api.mana.flow.ManaFlowRule;
import net.stln.magitech.util.ComponentHelper;

public class ManaContainerItemManaHandler implements IItemManaHandler {
    private final ItemStack stack;

    public ManaContainerItemManaHandler(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public long getMana() {
        return ComponentHelper.getMana(stack);
    }

    @Override
    public void setMana(long value) {
        ComponentHelper.updateMana(stack, mana -> Math.clamp(value, 0, getMaxMana()));
    }

    @Override
    public long getMaxMana() {
        return ComponentHelper.getMaxMana(stack);
    }

    @Override
    public long getMaxFlow() {
        return ComponentHelper.getMaxFlow(stack);
    }

    @Override
    public long insertMana(long maxInsert, boolean simulate) {
        // アイテムの場合は「今回の要求量」を maxFlow で制限するだけにする
        long effectiveRate = Math.min(maxInsert, getMaxFlow());

        long current = getMana();
        long accepted = Math.min(getMaxMana() - current, effectiveRate);

        if (!simulate && accepted > 0) {
            setMana(current + accepted);
        }
        return accepted;
    }

    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        // 同様に「今回の要求量」を maxFlow で制限
        long effectiveRate = Math.min(maxExtract, getMaxFlow());

        long current = getMana();
        long extracted = Math.min(current, effectiveRate);

        if (!simulate && extracted > 0) {
            setMana(current - extracted);
        }
        return extracted;
    }

    @Override
    public ManaFlowRule getManaFlowRule() {
        return ManaFlowRule.BothWays(0.0F);
    }
}
