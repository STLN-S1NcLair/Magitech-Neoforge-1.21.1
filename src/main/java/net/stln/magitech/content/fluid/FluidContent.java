package net.stln.magitech.content.fluid;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 液体に関する要素を束ねたクラスです。
 * @author Hiiragi Tsubasa
 */
public sealed interface FluidContent extends Supplier<Fluid> {
    @NotNull DeferredHolder<FluidType, ?> typeHolder();

    @NotNull DeferredHolder<Fluid, ?> sourceHolder();

    @NotNull Optional<DeferredItem<?>> bucketHolder();

    @NotNull TagKey<Fluid> fluidTag();

    @NotNull TagKey<Item> bucketTag();

    default @NotNull FluidStack toStack(int amount, @NotNull DataComponentPatch patch) {
        if (sourceHolder().isBound()) {
            return new FluidStack(sourceHolder(), amount, patch);
        } else {
            return FluidStack.EMPTY;
        }
    }

    default @NotNull FluidStack toStack(int amount) {
        return toStack(amount, DataComponentPatch.EMPTY);
    }

    default @NotNull FluidType fluidType() {
        return typeHolder().get();
    }

    @Override
    default @NotNull Fluid get() {
        return sourceHolder().get();
    }

    default @NotNull ResourceKey<Fluid> key() {
        return sourceHolder().getKey();
    }

    default @NotNull ResourceLocation id() {
        return key().location();
    }

    //    Virtual    //

    /**
     * 基本的な{@link FluidContent}の実装クラスです。
     * @param typeHolder 液体の種類の{@link DeferredHolder}
     * @param sourceHolder 液体源の{@link DeferredHolder}
     * @param bucketHolder 液体入りバケツの{@link DeferredHolder}
     * @param fluidTag 液体の共通タグ
     * @param bucketTag 液体入りバケツの共通タグ
     */
    record Virtual(
            @NotNull DeferredHolder<FluidType, ?> typeHolder,
            @NotNull DeferredHolder<Fluid, ?> sourceHolder,
            @NotNull Optional<DeferredItem<?>> bucketHolder,
            @NotNull TagKey<Fluid> fluidTag,
            @NotNull TagKey<Item> bucketTag
    ) implements FluidContent {

    }

    //    Flowing    //

    /**
     * {@link FlowingFluid}に基づいた{@link FluidContent}の実装クラスです。
     * @param typeHolder 液体の種類の{@link DeferredHolder}
     * @param sourceHolder 液体源の{@link DeferredHolder}
     * @param bucketHolder 液体入りバケツの{@link DeferredHolder}
     * @param fluidTag 液体の共通タグ
     * @param bucketTag 液体入りバケツの共通タグ
     * @param flowingHolder 液体流の{@link DeferredHolder}
     * @param blockHolder 液体ブロックの{@link DeferredHolder}
     */
    record Flowing(
            @NotNull DeferredHolder<FluidType, ?> typeHolder,
            @NotNull DeferredHolder<Fluid, ? extends FlowingFluid> sourceHolder,
            @NotNull Optional<DeferredItem<?>> bucketHolder,
            @NotNull TagKey<Fluid> fluidTag,
            @NotNull TagKey<Item> bucketTag,
            @NotNull DeferredHolder<Fluid, ? extends FlowingFluid> flowingHolder,
            @NotNull Optional<DeferredBlock<LiquidBlock>> blockHolder
    ) implements FluidContent {
    }
}
