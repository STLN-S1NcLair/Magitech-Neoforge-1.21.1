package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.container.IManaMachineBlockEntity;
import net.stln.magitech.core.api.mana.handler.MachineBlockEntityManaHandler;

public abstract class ManaMachineBlockEntity extends ManaContainerBlockEntity implements IManaMachineBlockEntity {

    long producedMana;
    long consumedMana;
    private float averageProduce = 0;
    private float averageConsumption = 0;


    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long mana) {
        super(type, pos, blockState, mana);
    }

    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        this(type, pos, blockState, 0);
    }

    @Override
    public MachineBlockEntityManaHandler getManaHandler(Direction side) {
        return new MachineBlockEntityManaHandler(this, side);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state) {
        super.tick(level, pos, state);
        this.producedMana = 0;
        this.consumedMana = 0;
    }

    @Override
    protected void updateFlowAverage() {
        super.updateFlowAverage();
        float factorProduction = 0.0025f;

        this.averageProduce = (this.averageProduce * (1.0f - factorProduction)) + (this.producedMana * factorProduction);
        if (Math.abs(this.averageProduce) < 0.1f) {
            this.averageProduce = 0;
        }

        this.averageConsumption = (this.averageConsumption * (1.0f - factorProduction)) + (this.consumedMana * factorProduction);
        if (Math.abs(this.averageConsumption) < 0.1f) {
            this.averageConsumption = 0;
        }
    }

    @Override
    public long getProducedMana() {
        return producedMana;
    }

    @Override
    public long getConsumedMana() {
        return consumedMana;
    }

    @Override
    public void addProducedMana(long amount) {
        this.producedMana += amount;
    }

    @Override
    public void addConsumedMana(long amount) {
        this.consumedMana += amount;
    }

    @Override
    public long getProductionRate() {
        return Math.round(this.averageProduce);
    }

    @Override
    public long getConsumptionRate() {
        return Math.round(averageConsumption);
    }
}
