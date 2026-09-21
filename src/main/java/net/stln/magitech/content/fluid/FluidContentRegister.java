package net.stln.magitech.content.fluid;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.DispenseFluidContainer;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * {@link Fluid 液体}を登録する{@link DeferredRegister}の補助クラスです。
 *
 * @author Hiiragi Tsubasa
 */
public final class FluidContentRegister {
    public final String modId;
    private final DeferredRegister<Fluid> fluidRegister;
    private final DeferredRegister<FluidType> fluidTypeRegister;
    private final DeferredRegister.Blocks blockRegister;
    private final DeferredRegister.Items itemRegister;

    public FluidContentRegister(String modId) {
        this.modId = modId;
        this.fluidRegister = DeferredRegister.create(Registries.FLUID, modId);
        this.fluidTypeRegister = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modId);
        this.blockRegister = DeferredRegister.createBlocks(modId);
        this.itemRegister = DeferredRegister.createItems(modId);
    }

    /**
     * 登録された液体の一覧を取得します。
     */
    public @NotNull Stream<? extends DeferredHolder<Fluid, ?>> fluidStream() {
        return fluidRegister.getEntries().stream();
    }

    /**
     * 登録された{@link FluidType}の一覧を取得します。
     */
    public @NotNull Stream<? extends DeferredHolder<FluidType, ?>> fluidTypeStream() {
        return fluidTypeRegister.getEntries().stream();
    }

    /**
     * 登録された液体ブロックの一覧を取得します。
     */
    public @NotNull Stream<? extends DeferredBlock<?>> blockStream() {
        return blockRegister.getEntries().stream().map(holder -> (DeferredBlock<?>) holder);
    }

    /**
     * 登録された液体入りバケツの一覧を取得します。
     */
    public @NotNull Stream<? extends DeferredItem<?>> itemStream() {
        return itemRegister.getEntries().stream().map(holder -> (DeferredItem<?>) holder);
    }

    /**
     * 登録された{@link FluidContent}の一覧
     */
    private final ObjectLinkedOpenHashSet<FluidContent> contents = new ObjectLinkedOpenHashSet<>();

    /**
     * 登録された{@link FluidContent}の一覧を取得します。
     */
    public @UnmodifiableView @NotNull ObjectSet<FluidContent> getContents() {
        return ObjectSets.unmodifiable(contents);
    }

    private final Object2ObjectLinkedOpenHashMap<ResourceKey<Fluid>, FluidContent> contentsCache = new Object2ObjectLinkedOpenHashMap<>();

    /**
     * {@link FluidContent}を取得します。
     *
     * @param key 対応する液体の{@link ResourceKey}
     * @return 対応する{@link FluidContent}がない場合は`null`
     */
    public @Nullable FluidContent getContent(@NotNull ResourceKey<Fluid> key) {
        return contentsCache.get(key);
    }

    public void addAlias(@NotNull ResourceLocation from, @NotNull ResourceLocation to) {
        fluidTypeRegister.addAlias(from, to);

        fluidRegister.addAlias(from, to);
        fluidRegister.addAlias(from.withPrefix("flowing_"), to.withPrefix("flowing_"));

        blockRegister.addAlias(from, to);

        itemRegister.addAlias(from.withSuffix("_bucket"), to.withSuffix("_bucket"));
    }

    /**
     * {@link IEventBus}に登録します。
     */
    public void register(@NotNull IEventBus eventBus) {
        fluidRegister.register(eventBus);
        fluidTypeRegister.register(eventBus);
        blockRegister.register(eventBus);
        itemRegister.register(eventBus);

        eventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> this.itemStream().forEach(holder -> DispenserBlock.registerBehavior(holder, DispenseFluidContainer.getInstance()))));
    }

    /**
     * 液体源のみの新しい液体を登録します。
     *
     * @param name     液体のIDのパス
     * @param consumer {@link VirtualBuilder}を初期化するブロック
     * @return 新しい{@link FluidContent}のインスタンス
     */
    public @NotNull FluidContent.Virtual registerVirtual(@NotNull String name, @NotNull Consumer<VirtualBuilder> consumer) {
        var builder = new VirtualBuilder(name);
        consumer.accept(builder);
        return builder.build();
    }

    /**
     * 液体流をもつの新しい液体を登録します。
     *
     * @param name     液体のIDのパス
     * @param consumer {@link VirtualBuilder}を初期化するブロック
     * @return 新しい{@link FluidContent}のインスタンス
     */
    public @NotNull FluidContent.Flowing registerFlowing(@NotNull String name, @NotNull Consumer<FlowingBuilder> consumer) {
        var builder = new FlowingBuilder(name);
        consumer.accept(builder);
        return builder.build();
    }

    /**
     * {@link FluidContent}のビルダークラスです。
     *
     * @param <FLUID>   液体のクラス
     * @param <CONTENT> 出力する{@link FluidContent}のクラス
     */
    public abstract class Builder<FLUID extends Fluid, CONTENT extends FluidContent> {
        protected final String name;

        /**
         * {@link FluidType}のプロパティ
         */
        public FluidType.Properties properties;

        /**
         * {@link FluidType}を作るブロック
         */
        public @NotNull Function<FluidType.Properties, ? extends FluidType> typeFactory = FluidType::new;

        /**
         * 液体入りバケツを作るブロック
         */
        public @Nullable BiFunction<Fluid, Item.Properties, ? extends Item> bucketFactory = BucketItem::new;

        /**
         * 液体入りバケツのプロパティ
         */
        public @NotNull UnaryOperator<Item.Properties> bucketProperties = UnaryOperator.identity();

        /**
         * 液体の共通タグ
         */
        public @NotNull ResourceLocation fluidTag;

        /**
         * 液体入りバケツの共通タグ
         */
        public @NotNull ResourceLocation bucketTag;

        protected Builder(String name) {
            this.name = name;
            this.fluidTag = ResourceLocation.fromNamespaceAndPath("c", name);
            this.bucketTag = ResourceLocation.fromNamespaceAndPath("c", "buckets/" + name);
        }

        public final @NotNull CONTENT build() {
            // Fluid Type
            Objects.requireNonNull(properties, "Fluid properties is required!");
            DeferredHolder<FluidType, ?> typeHolder = fluidTypeRegister.register(name, () -> typeFactory.apply(properties));
            // Fluid Holder
            DeferredHolder<Fluid, FLUID> sourceHolder = DeferredHolder.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(modId, name));
            // Bucket Item
            DeferredItem<?> bucketHolder;
            if (bucketFactory != null) {
                bucketHolder = itemRegister.registerItem(
                        "%s_bucket".formatted(name),
                        (prop) -> Objects.requireNonNull(bucketFactory).apply(sourceHolder.get(), prop),
                        bucketProperties.apply(new Item.Properties()).stacksTo(1).craftRemainder(Items.BUCKET)
                );
            } else {
                bucketHolder = null;
            }
            CONTENT content = createContent(
                    typeHolder,
                    sourceHolder,
                    bucketHolder,
                    FluidTags.create(fluidTag),
                    ItemTags.create(bucketTag)
            );
            contents.add(content);
            contentsCache.put(content.key(), content);
            if (content instanceof FluidContent.Flowing flowing) {
                contentsCache.put(flowing.flowingHolder().getKey(), content);
            }
            return content;
        }

        protected abstract @NotNull CONTENT createContent(
                @NotNull DeferredHolder<FluidType, ?> typeHolder,
                @NotNull DeferredHolder<Fluid, FLUID> sourceHolder,
                @Nullable DeferredItem<?> bucketHolder,
                @NotNull TagKey<Fluid> fluidTag,
                @NotNull TagKey<Item> bucketTag
        );
    }

    /**
     * {@link FluidContent.Virtual}向けの{@link Builder}の実装クラスです。
     */
    public class VirtualBuilder extends Builder<VirtualFluid, FluidContent.Virtual> {
        protected VirtualBuilder(String name) {
            super(name);
        }

        @Override
        protected @NotNull FluidContent.Virtual createContent(@NotNull DeferredHolder<FluidType, ?> typeHolder, @NotNull DeferredHolder<Fluid, VirtualFluid> sourceHolder, @Nullable DeferredItem<?> bucketHolder, @NotNull TagKey<Fluid> fluidTag, @NotNull TagKey<Item> bucketTag) {
            fluidRegister.register(sourceHolder.getId().getPath(), () -> new VirtualFluid(typeHolder, bucketHolder));
            return new FluidContent.Virtual(typeHolder, sourceHolder, Optional.ofNullable(bucketHolder), fluidTag, bucketTag);
        }
    }

    public class FlowingBuilder extends Builder<BaseFlowingFluid, FluidContent.Flowing> {
        /**
         * 液体のプロパティ
         */
        public @NotNull UnaryOperator<BaseFlowingFluid.Properties> fluidProperties = UnaryOperator.identity();

        /**
         * 液体源を作るブロック
         */
        public @NotNull Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid.Source> sourceFactory = BaseFlowingFluid.Source::new;

        /**
         * 液体流を作るブロック
         */
        public @NotNull Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid.Flowing> flowingFactory = BaseFlowingFluid.Flowing::new;

        /**
         * 液体ブロックを作成するブロック
         * <p>
         * `null`の場合，液体ブロックは作成されません。
         */
        public @Nullable BiFunction<BaseFlowingFluid, BlockBehaviour.Properties, ? extends LiquidBlock> blockFactory = LiquidBlock::new;

        protected FlowingBuilder(String name) {
            super(name);
        }

        @Override
        protected @NotNull FluidContent.Flowing createContent(@NotNull DeferredHolder<FluidType, ?> typeHolder, @NotNull DeferredHolder<Fluid, BaseFlowingFluid> sourceHolder, @Nullable DeferredItem<?> bucketHolder, @NotNull TagKey<Fluid> fluidTag, @NotNull TagKey<Item> bucketTag) {
            // Liquid Block
            DeferredBlock<LiquidBlock> blockHolder;
            if (blockFactory != null) {
                blockHolder = blockRegister.registerBlock(name, (prop) -> Objects.requireNonNull(blockFactory).apply(sourceHolder.get(), prop), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER));
            } else {
                blockHolder = null;
            }
            // Fluids
            DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingHolder = DeferredHolder.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(modId, "flowing_%s".formatted(name)));
            var fluidProperties = this.fluidProperties.apply(new BaseFlowingFluid.Properties(typeHolder, sourceHolder, flowingHolder));
            if (bucketHolder != null) {
                fluidProperties.bucket(bucketHolder);
            }
            if (blockHolder != null) {
                fluidProperties.block(blockHolder);
            }
            fluidRegister.register(sourceHolder.getId().getPath(), () -> sourceFactory.apply(fluidProperties));
            fluidRegister.register(flowingHolder.getId().getPath(), () -> flowingFactory.apply(fluidProperties));
            return new FluidContent.Flowing(
                    typeHolder,
                    sourceHolder,
                    Optional.ofNullable(bucketHolder),
                    fluidTag,
                    bucketTag,
                    flowingHolder,
                    Optional.ofNullable(blockHolder)
            );
        }
    }
}
