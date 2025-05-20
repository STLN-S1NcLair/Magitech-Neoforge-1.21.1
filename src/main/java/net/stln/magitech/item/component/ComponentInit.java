package net.stln.magitech.item.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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

    public static void registerComponents(IEventBus eventBus) {
        COMPONENT_TYPES.register(eventBus);
    }

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
