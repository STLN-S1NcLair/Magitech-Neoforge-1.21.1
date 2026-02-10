package net.stln.magitech.api.mana.container;

public interface IManaMachineBlockEntity extends IManaContainerBlockEntity {

    long getProducedMana();

    long getConsumedMana();

    void addProducedMana(long amount);

    void addConsumedMana(long amount);
}
