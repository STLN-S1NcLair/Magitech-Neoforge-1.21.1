package net.stln.magitech.core.api.mana.handler;

import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.core.api.mana.flow.ManaFlowRule;
import net.stln.magitech.data.DataAttachmentInit;

// Entityのmax_manaは1kJ単位で管理し、max_mana = 100のとき100kJのマナ容量とする
public record EntityManaHandler(LivingEntity entity) implements IBasicManaHandler {

    @Override
    public ManaFlowRule getManaFlowRule() {
        return ManaFlowRule.BothWays(0.0F);
    }

    @Override
    public long getMana() {
        return this.entity.getData(DataAttachmentInit.ENTITY_MANA);
    }

    @Override
    public void setMana(long mana) {
        this.entity.setData(DataAttachmentInit.ENTITY_MANA, Math.clamp(mana, 0, getMaxMana()));
    }

    @Override
    public long getMaxMana() {
        return EntityManaHelper.getEnergyMaxMana(entity);
    }

    // 本質的に「アイテムのマナハンドラーをEntityに適用するためのラッパー」なので、最大流量は特に定めず、常に最大マナと同じ値を返すようにする
    @Override
    public long getMaxFlow() {
        return getMaxMana(); // とりあえず、最大流量は最大マナと同じにする
    }

    public long getManaRegen() {
        return EntityManaHelper.getEnergyManaRegen(entity);
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
}
