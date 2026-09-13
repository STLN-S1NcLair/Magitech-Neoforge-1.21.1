package net.stln.magitech.core.api.mana.handler;

import net.minecraft.core.Direction;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;

/**
 * マナの生成・消費機能を持つブロックエンティティ用ハンドラーです。
 * Mana handler for block entities that can produce and consume mana.
 */
public class MachineBlockEntityManaHandler extends ContainerBlockEntityManaHandler {

    // insert / extract: マナの外部との輸送 / insert / extract: transfer mana to and from the outside
    // consume / produce: マナの内部生成/消費 / consume / produce: generate and consume mana internally

    /**
     * 機械ブロックエンティティと接続面からハンドラーを生成します。
     * Creates a handler from a machine block entity and its connected face.
     */
    public MachineBlockEntityManaHandler(IManaMachineBlockEntity be, Direction side) {
        super(be, side);
    }

    /**
     * 内部処理としてマナを消費します。
     * Consumes mana as an internal machine operation.
     *
     * @return 実際に消費した量 / the amount actually consumed
     */
    public long consumeMana(long amount) {
        long actualConsumption = -(Math.clamp(getMana() - amount, 0, getMaxMana()) - getMana());
        setMana(getMana() - actualConsumption);
        ((IManaMachineBlockEntity) be).addConsumedMana(actualConsumption);
        return actualConsumption;
    }

    /**
     * 内部処理としてマナを生成します。
     * Produces mana as an internal machine operation.
     *
     * @return 実際に生成した量 / the amount actually produced
     */
    public long produceMana(long amount) {
        long actualProduction = Math.clamp(getMana() + amount, 0, getMaxMana()) - getMana();
        setMana(getMana() + actualProduction);
        ((IManaMachineBlockEntity) be).addProducedMana(actualProduction);
        return actualProduction;
    }
}
