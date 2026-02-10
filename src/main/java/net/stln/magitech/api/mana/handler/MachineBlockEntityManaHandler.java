package net.stln.magitech.api.mana.handler;

import net.minecraft.core.Direction;
import net.stln.magitech.api.mana.container.IManaMachineBlockEntity;

public class MachineBlockEntityManaHandler extends ContainerBlockEntityManaHandler {

    // insert / extract: マナの外部との輸送
    // consume / produce: マナの内部生成/消費

    public MachineBlockEntityManaHandler(IManaMachineBlockEntity be, Direction side) {
        super(be, side);
    }

    // マナを消費する。実際に消費した量を返す。
    public long consumeMana(long amount) {
        long actualConsumption = Math.clamp(getMana() - amount, 0, getMaxMana()) - getMana();
        setMana(getMana() - actualConsumption);
        ((IManaMachineBlockEntity) be).addConsumedMana(actualConsumption);
        return actualConsumption;
    }

    // マナを生成する。生成した量を返す。
    public long produceMana(long amount) {
        long actualProduction = Math.clamp(getMana() + amount, 0, getMaxMana()) - getMana();
        setMana(getMana() + actualProduction);
        ((IManaMachineBlockEntity) be).addProducedMana(actualProduction);
        return actualProduction;
    }
}
