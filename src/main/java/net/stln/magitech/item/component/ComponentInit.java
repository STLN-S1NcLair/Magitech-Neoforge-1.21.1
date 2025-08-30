package net.stln.magitech.item.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.UnaryOperator;

public class ComponentInit {

    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Magitech.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PartMaterialComponent>> PART_MATERIAL_COMPONENT = register("part_material_component",
            builder -> builder.persistent(PartMaterialComponent.CODEC).networkSynchronized(PartMaterialComponent.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MaterialComponent>> MATERIAL_COMPONENT = register("material_component",
            builder -> builder.persistent(MaterialComponent.CODEC).networkSynchronized(MaterialComponent.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellComponent>> SPELL_COMPONENT = register("spell_component",
            builder -> builder.persistent(SpellComponent.CODEC).networkSynchronized(SpellComponent.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ThreadPageComponent>> THREAD_PAGE_COMPONENT = register("thread_page_component",
            builder -> builder.persistent(ThreadPageComponent.CODEC).networkSynchronized(ThreadPageComponent.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TIER_COMPONENT = register("tier_component",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PROGRESSION_COMPONENT = register("progression_component",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_PROGRESSION_COMPONENT = register("max_progression_component",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UPGRADE_SEED_COMPONENT = register("upgrade_seed",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeComponent>> UPGRADE_COMPONENT = register("upgrade_component",
            builder -> builder.persistent(UpgradeComponent.CODEC).networkSynchronized(UpgradeComponent.STREAM_CODEC).cacheEncoding());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> UPGRADE_POINT_COMPONENT = register("upgrade_point_component",
            builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));


    public static void registerComponents(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Data Components for" + Magitech.MOD_ID);
        COMPONENT_TYPES.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
