package net.stln.magitech.api.machine.inspection;

import net.minecraft.core.BlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record MachineInspectionData(
        BlockPos targetPosition,
        BlockPos displayPosition,
        long mana,
        long maxMana,
        long flowRate,
        long maxFlow,
        long productionRate,
        long consumptionRate,
        List<ItemStack> items,
        List<FluidStack> fluids,
        List<Component> extraLines,
        long remainingTimeTicks,
        long remainingTimeDurationTicks,
        long totalRemainingTimeTicks,
        int timeGaugeElement,
        @Nullable ResourceLocation fieldEffect,
        @Nullable FieldInfluenceInstance fieldInfluences
) {
    public static final int MAX_ITEM_SLOTS = 16;
    public static final int MAX_FLUID_TANKS = 16;
    public static final int MAX_EXTRA_LINES = 16;
    public static final int TIME_GAUGE_NONE = 0;
    public static final int TIME_GAUGE_EMBER = 1;
    public static final int TIME_GAUGE_GLACE = 2;

    private static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> ITEM_LIST_STREAM_CODEC =
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(MAX_ITEM_SLOTS));
    private static final StreamCodec<RegistryFriendlyByteBuf, List<FluidStack>> FLUID_LIST_STREAM_CODEC =
            FluidStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(MAX_FLUID_TANKS));
    private static final StreamCodec<RegistryFriendlyByteBuf, List<Component>> EXTRA_LINE_STREAM_CODEC =
            ComponentSerialization.TRUSTED_STREAM_CODEC.apply(ByteBufCodecs.list(MAX_EXTRA_LINES));
    private static final StreamCodec<ByteBuf, Optional<ResourceLocation>> FIELD_EFFECT_STREAM_CODEC =
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC);
    private static final StreamCodec<RegistryFriendlyByteBuf, Optional<FieldInfluenceInstance>> FIELD_INFLUENCES_STREAM_CODEC =
            ByteBufCodecs.optional(FieldInfluenceInstance.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineInspectionData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MachineInspectionData decode(RegistryFriendlyByteBuf buffer) {
            return new MachineInspectionData(
                    BlockPos.STREAM_CODEC.decode(buffer),
                    BlockPos.STREAM_CODEC.decode(buffer),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readLong(),
                    ITEM_LIST_STREAM_CODEC.decode(buffer),
                    FLUID_LIST_STREAM_CODEC.decode(buffer),
                    EXTRA_LINE_STREAM_CODEC.decode(buffer),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readLong(),
                    buffer.readVarInt(),
                    FIELD_EFFECT_STREAM_CODEC.decode(buffer).orElse(null),
                    FIELD_INFLUENCES_STREAM_CODEC.decode(buffer).orElse(null)
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, MachineInspectionData data) {
            BlockPos.STREAM_CODEC.encode(buffer, data.targetPosition());
            BlockPos.STREAM_CODEC.encode(buffer, data.displayPosition());
            buffer.writeLong(data.mana());
            buffer.writeLong(data.maxMana());
            buffer.writeLong(data.flowRate());
            buffer.writeLong(data.maxFlow());
            buffer.writeLong(data.productionRate());
            buffer.writeLong(data.consumptionRate());
            ITEM_LIST_STREAM_CODEC.encode(buffer, data.items());
            FLUID_LIST_STREAM_CODEC.encode(buffer, data.fluids());
            EXTRA_LINE_STREAM_CODEC.encode(buffer, data.extraLines());
            buffer.writeLong(data.remainingTimeTicks());
            buffer.writeLong(data.remainingTimeDurationTicks());
            buffer.writeLong(data.totalRemainingTimeTicks());
            buffer.writeVarInt(data.timeGaugeElement());
            FIELD_EFFECT_STREAM_CODEC.encode(buffer, Optional.ofNullable(data.fieldEffect()));
            FIELD_INFLUENCES_STREAM_CODEC.encode(buffer, Optional.ofNullable(data.fieldInfluences()));
        }
    };

    public MachineInspectionData {
        items = List.copyOf(items == null ? List.of() : items);
        fluids = List.copyOf(fluids == null ? List.of() : fluids);
        extraLines = List.copyOf(extraLines == null ? List.of() : extraLines);
    }

    public double manaRatio() {
        return maxMana > 0 ? (double) mana / maxMana : 0.0D;
    }

    public boolean hasTimeGauge() {
        return timeGaugeElement != TIME_GAUGE_NONE;
    }

    public static Builder builder(BlockPos targetPosition, BlockPos displayPosition) {
        return new Builder(targetPosition, displayPosition);
    }

    public static final class Builder {
        private final BlockPos targetPosition;
        private final BlockPos displayPosition;
        private long mana;
        private long maxMana;
        private long flowRate;
        private long maxFlow;
        private long productionRate;
        private long consumptionRate;
        private final List<ItemStack> items = new ArrayList<>();
        private final List<FluidStack> fluids = new ArrayList<>();
        private final List<Component> extraLines = new ArrayList<>();
        private long remainingTimeTicks;
        private long remainingTimeDurationTicks;
        private long totalRemainingTimeTicks;
        private int timeGaugeElement;
        private @Nullable ResourceLocation fieldEffect;
        private @Nullable FieldInfluenceInstance fieldInfluences;

        private Builder(BlockPos targetPosition, BlockPos displayPosition) {
            this.targetPosition = targetPosition;
            this.displayPosition = displayPosition;
        }

        public Builder setMana(long mana, long maxMana) {
            this.mana = mana;
            this.maxMana = maxMana;
            return this;
        }

        public Builder setFlow(long flowRate, long maxFlow) {
            this.flowRate = flowRate;
            this.maxFlow = maxFlow;
            return this;
        }

        public Builder setProductionRate(long productionRate) {
            this.productionRate = productionRate;
            return this;
        }

        public Builder setConsumptionRate(long consumptionRate) {
            this.consumptionRate = consumptionRate;
            return this;
        }

        public Builder setFieldEffect(@Nullable ResourceLocation fieldEffect) {
            this.fieldEffect = fieldEffect;
            return this;
        }

        public Builder setFieldInfluences(@Nullable FieldInfluenceInstance fieldInfluences) {
            this.fieldInfluences = fieldInfluences;
            return this;
        }

        public Builder setTimeGauge(
                long remainingTimeTicks,
                long remainingTimeDurationTicks,
                long totalRemainingTimeTicks,
                int timeGaugeElement
        ) {
            this.remainingTimeTicks = Math.max(0L, remainingTimeTicks);
            this.remainingTimeDurationTicks = Math.max(0L, remainingTimeDurationTicks);
            this.totalRemainingTimeTicks = Math.max(0L, totalRemainingTimeTicks);
            this.timeGaugeElement = timeGaugeElement;
            return this;
        }

        public Builder setItems(Collection<ItemStack> stacks) {
            this.items.clear();
            if (stacks != null) {
                stacks.stream()
                        .limit(MAX_ITEM_SLOTS)
                        .map(ItemStack::copy)
                        .forEach(this.items::add);
            }
            return this;
        }

        public Builder addItem(ItemStack stack) {
            if (this.items.size() < MAX_ITEM_SLOTS) {
                this.items.add(stack == null ? ItemStack.EMPTY : stack.copy());
            }
            return this;
        }

        public Builder setFluids(Collection<FluidStack> fluids) {
            this.fluids.clear();
            if (fluids != null) {
                fluids.stream()
                        .limit(MAX_FLUID_TANKS)
                        .map(fluid -> fluid == null ? FluidStack.EMPTY : fluid.copy())
                        .forEach(this.fluids::add);
            }
            return this;
        }

        public Builder addFluid(FluidStack fluid) {
            if (this.fluids.size() < MAX_FLUID_TANKS) {
                this.fluids.add(fluid == null ? FluidStack.EMPTY : fluid.copy());
            }
            return this;
        }

        public Builder addExtraLine(Component line) {
            if (line != null && this.extraLines.size() < MAX_EXTRA_LINES) {
                this.extraLines.add(line);
            }
            return this;
        }

        public MachineInspectionData build() {
            return new MachineInspectionData(
                    targetPosition,
                    displayPosition,
                    mana,
                    maxMana,
                    flowRate,
                    maxFlow,
                    productionRate,
                    consumptionRate,
                    items,
                    fluids,
                    extraLines,
                    remainingTimeTicks,
                    remainingTimeDurationTicks,
                    totalRemainingTimeTicks,
                    timeGaugeElement,
                    fieldEffect,
                    fieldInfluences
            );
        }
    }
}
