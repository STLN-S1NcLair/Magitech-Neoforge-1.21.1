package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.helper.ComponentHelper;

/**
 * ItemStack のマナ容器をアイテム用ハンドラーとして公開します。
 * Exposes an ItemStack mana container through the item mana-handler API.
 */
public class ManaContainerItemManaHandler implements IItemManaHandler {
    private final ItemStack stack;

    /**
     * マナを保持する ItemStack からハンドラーを生成します。
     * Creates a handler from an ItemStack that stores mana.
     */
    public ManaContainerItemManaHandler(ItemStack stack) {
        this.stack = stack;
    }

    /**
     * アイテムの現在のマナ量を返します。
     * Returns the item's current mana amount.
     */
    @Override
    public long getMana() {
        return ComponentHelper.getMana(stack);
    }

    /**
     * アイテムのマナ量を容量範囲内に制限して設定します。
     * Sets the item's mana clamped to its capacity range.
     */
    @Override
    public void setMana(long value) {
        ComponentHelper.updateMana(stack, mana -> Math.clamp(value, 0, getMaxMana()));
    }

    /**
     * アイテムの最大マナ容量を返します。
     * Returns the item's maximum mana capacity.
     */
    @Override
    public long getMaxMana() {
        return ComponentHelper.getMaxMana(stack);
    }

    /**
     * アイテムの最大転送量を返します。
     * Returns the item's maximum transfer amount.
     */
    @Override
    public long getMaxFlow() {
        return ComponentHelper.getMaxFlow(stack);
    }

    /**
     * アイテムへマナを挿入可能な量だけ挿入します。
     * Inserts as much mana into the item as allowed.
     */
    @Override
    public long insertMana(long maxInsert, boolean simulate) {
        // アイテムの場合は「今回の要求量」を maxFlow で制限するだけにする / For items, limit only this request by maxFlow
        long effectiveRate = Math.min(maxInsert, getMaxFlow());

        long current = getMana();
        long accepted = Math.min(getMaxMana() - current, effectiveRate);

        if (!simulate && accepted > 0) {
            setMana(current + accepted);
        }
        return accepted;
    }

    /**
     * アイテムからマナを排出可能な量だけ排出します。
     * Extracts as much mana from the item as allowed.
     */
    @Override
    public long extractMana(long maxExtract, boolean simulate) {
        // 同様に「今回の要求量」を maxFlow で制限 / Likewise, limit only this request by maxFlow
        long effectiveRate = Math.min(maxExtract, getMaxFlow());

        long current = getMana();
        long extracted = Math.min(current, effectiveRate);

        if (!simulate && extracted > 0) {
            setMana(current - extracted);
        }
        return extracted;
    }

    /**
     * アイテムのマナ流量ルールを返します。
     * Returns the item's mana flow rule.
     */
    @Override
    public ManaFlowRule getManaFlowRule() {
        return ManaFlowRule.bothWays(0.0F);
    }
}
