package net.stln.magitech.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.element.Element;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ManaContainerBlockEntity extends BlockEntity {

    int mana = 0;
    int maxMana = 100;

    int barGaugeLength = 30;

    public ManaContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.mana = tag.getInt("Mana");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Mana", this.mana);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
        tag.putInt("Mana", this.mana);
        return tag;
    }

    public void addMana(int value) {
        this.mana += value;
        this.mana = Math.clamp(this.mana, 0, this.maxMana);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public void subMana(int value) {
        this.mana -= value;
        this.mana = Math.clamp(this.mana, 0, this.maxMana);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public void setMana(int value) {
        this.mana = value;
        this.mana = Math.clamp(this.mana, 0, this.maxMana);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    public List<Component> getManaInfo() {
        List<Component> list = new ArrayList<>();
        list.add(this.getBlockState().getBlock().getName());
        list.add(Component.empty());
        double chargedRatio = (double) this.mana / this.maxMana;
        int litBarGaugeLength = (int) (chargedRatio * barGaugeLength);
        list.add(Component.translatable("tooltip.magitech.block.mana_capacity").append(": ").withColor(0x808080));
        list.add(Component.literal("|".repeat(litBarGaugeLength)).withColor(Element.NONE.getSpellColor()).append(Component.literal("|".repeat(barGaugeLength - litBarGaugeLength)).withColor(Element.NONE.getSpellDark())));
        list.add(Component.literal(String.valueOf(this.mana)).withColor(Element.NONE.getSpellColor()).append(Component.literal(" / ").withColor(0x808080)).append(Component.literal(String.valueOf(this.maxMana)).withColor(Element.NONE.getSpellDark())));
        return list;
    }

    public List<Component> getSimpleManaInfo() {
        List<Component> list = new ArrayList<>();
        double chargedRatio = (double) this.mana / this.maxMana;
        int litBarGaugeLength = (int) (chargedRatio * barGaugeLength);
        list.add(Component.translatable("tooltip.magitech.block.mana_capacity").append(": ").withColor(0x808080));
        list.add(Component.literal("|".repeat(litBarGaugeLength)).withColor(Element.NONE.getSpellColor()).append(Component.literal("|".repeat(barGaugeLength - litBarGaugeLength)).withColor(Element.NONE.getSpellDark())));
        list.add(Component.literal(String.valueOf(this.mana)).withColor(Element.NONE.getSpellColor()).append(Component.literal(" / ").withColor(0x808080)).append(Component.literal(String.valueOf(this.maxMana)).withColor(Element.NONE.getSpellDark())));
        return list;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public boolean isFull() {
        return mana == maxMana;
    }
}
