package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.data.DataAttachmentInit;

// Entityのmax_manaは1kJ単位で管理し、max_mana = 100のとき100kJのマナ容量とする / An entity's max_mana is managed in 1 kJ units, so max_mana = 100 represents 100 kJ of mana capacity
/**
 * LivingEntity に保存されたマナをハンドラーとして公開します。
 * Exposes mana stored on a LivingEntity through the mana-handler API.
 */
public record EntityManaHandler(LivingEntity entity) implements IBasicManaHandler {

    /**
     * エンティティのマナ流量ルールを返します。
     * Returns the entity's mana flow rule.
     */
    @Override
    public ManaFlowRule getManaFlowRule() {
        return ManaFlowRule.bothWays(0.0F);
    }

    /**
     * エンティティの現在のマナ量を返します。
     * Returns the entity's current mana amount.
     */
    @Override
    public long getMana() {
        return this.entity.getData(DataAttachmentInit.ENTITY_MANA);
    }

    /**
     * エンティティのマナ量を容量範囲内に制限して設定します。
     * Sets the entity's mana clamped to its capacity range.
     */
    @Override
    public void setMana(long mana) {
        this.entity.setData(DataAttachmentInit.ENTITY_MANA, Math.clamp(mana, 0, getMaxMana()));
    }

    /**
     * エンティティの最大マナ容量を返します。
     * Returns the entity's maximum mana capacity.
     */
    @Override
    public long getMaxMana() {
        return EntityManaHelper.getEnergyMaxMana(entity);
    }

    // 本質的に「アイテムのマナハンドラーをEntityに適用するためのラッパー」なので、最大流量は特に定めず、常に最大マナと同じ値を返すようにする / This is essentially a wrapper that applies an item mana handler to an entity, so it has no separate flow limit and always returns max mana as the maximum flow
    /**
     * エンティティの最大転送量を返します。
     * Returns the entity's maximum transfer amount.
     */
    @Override
    public long getMaxFlow() {
        return getMaxMana(); // とりあえず、最大流量は最大マナと同じにする / For now, use max mana as the maximum flow
    }

    /**
     * エンティティのマナ回復量を返します。
     * Returns the entity's mana regeneration amount.
     */
    public long getManaRegen() {
        return EntityManaHelper.getEnergyManaRegen(entity);
    }

    /**
     * マナを挿入可能な量だけ挿入します。
     * Inserts as much mana as allowed.
     */
    @Override
    public long insertMana(long maxInsert, boolean simulate) {
        // アイテムの場合は「今回の要求量」を maxFlow で制限するだけにする / As with items, limit only this request by maxFlow
        long effectiveRate = Math.min(maxInsert, getMaxFlow());

        long current = getMana();
        long accepted = Math.min(getMaxMana() - current, effectiveRate);

        if (!simulate && accepted > 0) {
            setMana(current + accepted);
        }
        return accepted;
    }

    /**
     * マナを排出可能な量だけ排出します。
     * Extracts as much mana as allowed.
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
}
