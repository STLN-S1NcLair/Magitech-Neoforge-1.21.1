package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.Capabilities;
import net.stln.magitech.api.mana.*;
import net.stln.magitech.util.LongContainerData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ManaMachineBlockEntity extends ManaContainerBlockEntity implements IManaMachineBlockEntity {

    long producedMana;
    long consumedMana;
    private float averageProduce = 0;
    private float averageConsumption = 0;


    public final LongContainerData dataAccess = new LongContainerData() {

        @Override
        public long getLong(int index) {
            return switch (index) {
                case 0 -> ManaMachineBlockEntity.this.getMana();
                case 1 -> ManaMachineBlockEntity.this.getMaxMana();
                case 2 -> ManaMachineBlockEntity.this.getFlowRate();
                case 3 -> ManaMachineBlockEntity.this.getMaxFlow();
                case 4 -> ManaMachineBlockEntity.this.getProductionRate();
                case 5 -> ManaMachineBlockEntity.this.getConsumptionRate();
                default -> 0;
            };
        }

        @Override
        public void setLong(int index, long value) {
            if (index == 0) {
                ManaMachineBlockEntity.this.mana = Math.clamp(value, 0, ManaMachineBlockEntity.this.maxMana);
            }
        }

        @Override
        public int getLongCount() {
            return 6;
        }
    };

    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long mana, long maxMana, long maxManaFlow) {
        super(type, pos, blockState, mana, maxMana, maxManaFlow);
    }

    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long maxMana, long maxManaFlow) {
        super(type, pos, blockState, 0, maxMana, maxManaFlow);
    }

    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long maxMana) {
        super(type, pos, blockState, 0, maxMana, Long.MAX_VALUE);
    }

    public ManaMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, 0, 1000, 1000);
    }

    // 生産、消費可能Handler
    protected final IBasicManaHandler MACHINE_HANDLER = new ManaMachineBlockEntityManaHandler(this, true, true);

    @Override
    protected IBasicManaHandler getInternalHandler() {
        return MACHINE_HANDLER;
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

    public int getProductionRate() {
        return Math.round(this.averageProduce);
    }

    public int getConsumptionRate() {
        return Math.round(averageConsumption);
    }
}
