package net.stln.magitech.api.mana;

public class ManaMachineBlockEntityManaHandler extends ManaContainerBlockEntityManaHandler {


    public ManaMachineBlockEntityManaHandler(IManaMachineBlockEntity be, boolean canReceive, boolean canExtract) {
        super(be, canReceive, canExtract);
    }

    // マナを消費する。実際に消費した量を返す。
    public long consumeMana(long amount) {
        long actualConsumption = Math.clamp(getMana() - amount, 0, getMaxMana()) - getMana();
        setMana(getMana() + actualConsumption);
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
