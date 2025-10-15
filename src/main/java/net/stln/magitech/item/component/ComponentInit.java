package net.stln.magitech.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

public class ComponentInit {

    public static final DeferredRegister.DataComponents COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Magitech.MOD_ID);

    public static final Supplier<DataComponentType<PartMaterialComponent>> PART_MATERIAL_COMPONENT = register("part_material_component", PartMaterialComponent.CODEC, PartMaterialComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<MaterialComponent>> MATERIAL_COMPONENT = register("material_component", MaterialComponent.CODEC, MaterialComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<SpellComponent>> SPELL_COMPONENT = register("spell_component", SpellComponent.CODEC, SpellComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<ThreadPageComponent>> THREAD_PAGE_COMPONENT = register("thread_page_component", ThreadPageComponent.CODEC, ThreadPageComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<Integer>> TIER_COMPONENT = register("tier_component", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> PROGRESSION_COMPONENT = register("progression_component", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> MAX_PROGRESSION_COMPONENT = register("max_progression_component", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Integer>> UPGRADE_SEED_COMPONENT = register("upgrade_seed", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<UpgradeComponent>> UPGRADE_COMPONENT = register("upgrade_component", UpgradeComponent.CODEC, UpgradeComponent.STREAM_CODEC);
    public static final Supplier<DataComponentType<Integer>> UPGRADE_POINT_COMPONENT = register("upgrade_point_component", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final Supplier<DataComponentType<Boolean>> BROKEN_COMPONENT = register("broken_component", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID_CONTENT_COMPONENT = register("fluid_content_component", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC);

    public static void registerComponents(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Data Components for" + Magitech.MOD_ID);
        COMPONENT_TYPES.register(eventBus);
    }

    private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return COMPONENT_TYPES.registerComponentType(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec).cacheEncoding());
    }
}
