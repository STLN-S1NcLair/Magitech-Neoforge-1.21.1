package net.stln.magitech.content.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@EventBusSubscriber
public final class FluidInit {
    private FluidInit() {
    }

    public static final FluidContentRegister REGISTER = new FluidContentRegister(Magitech.MOD_ID);

    private static final Consumer<FluidContentRegister.VirtualBuilder> DEFAULT_BUILDER = builder -> {
        builder.properties = liquid();
        builder.bucketFactory = null;
    };

    public static final FluidContent.Virtual SULFURIC_ACID = REGISTER.registerVirtual("sulfuric_acid", DEFAULT_BUILDER);

    public static final FluidContent.Virtual MANA_SOLUTION = REGISTER.registerVirtual("mana_solution", DEFAULT_BUILDER);

    public static final FluidContent.Virtual MANA_POTION = REGISTER.registerVirtual("mana_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual HEALING_POTION = REGISTER.registerVirtual("healing_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual EMBER_POTION = REGISTER.registerVirtual("ember_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual GLACE_POTION = REGISTER.registerVirtual("glace_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual SURGE_POTION = REGISTER.registerVirtual("surge_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual PHANTOM_POTION = REGISTER.registerVirtual("phantom_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual TREMOR_POTION = REGISTER.registerVirtual("tremor_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual MAGIC_POTION = REGISTER.registerVirtual("magic_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual FLOW_POTION = REGISTER.registerVirtual("flow_potion", DEFAULT_BUILDER);

    public static final FluidContent.Virtual HOLLOW_POTION = REGISTER.registerVirtual("hollow_potion", DEFAULT_BUILDER);

    private static @NotNull FluidType.Properties create(@NotNull SoundEvent fill, @NotNull SoundEvent empty) {
        return FluidType.Properties
                .create()
                .sound(SoundActions.BUCKET_FILL, fill)
                .sound(SoundActions.BUCKET_EMPTY, empty);
    }

    private static @NotNull FluidType.Properties liquid() {
        return create(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY);
    }

    public static void registerFluids(@NotNull IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Fluids for" + Magitech.MOD_ID);
        REGISTER.register(eventBus);
    }

    @SubscribeEvent
    public static void registerIClientFluidExtensions(RegisterClientExtensionsEvent event) {
        Magitech.LOGGER.info("Registering IClientFluidExtensions for" + Magitech.MOD_ID);
        registerIClientFluidExtension(event, "block/sulfuric_acid_still", SULFURIC_ACID);
        registerIClientFluidExtension(event, "block/mana_solution_still", MANA_SOLUTION);
        registerIClientFluidExtension(event, "block/mana_potion_still", MANA_POTION);
        registerIClientFluidExtension(event, "block/healing_potion_still", HEALING_POTION);
        registerIClientFluidExtension(event, "block/ember_potion_still", EMBER_POTION);
        registerIClientFluidExtension(event, "block/glace_potion_still", GLACE_POTION);
        registerIClientFluidExtension(event, "block/surge_potion_still", SURGE_POTION);
        registerIClientFluidExtension(event, "block/phantom_potion_still", PHANTOM_POTION);
        registerIClientFluidExtension(event, "block/tremor_potion_still", TREMOR_POTION);
        registerIClientFluidExtension(event, "block/magic_potion_still", MAGIC_POTION);
        registerIClientFluidExtension(event, "block/flow_potion_still", FLOW_POTION);
        registerIClientFluidExtension(event, "block/hollow_potion_still", HOLLOW_POTION);
    }

    private static void registerIClientFluidExtension(@NotNull RegisterClientExtensionsEvent event, @NotNull String name, @NotNull FluidContent content) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private final ResourceLocation stillTexture = Magitech.id(name);

            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return stillTexture;
            }
        }, content.fluidType());
    }

    /*public static void registerFluidRenderTypes() {
        Magitech.LOGGER.info("Registering Fluid Render Types for" + Magitech.MOD_ID);
        setRenderLayerTranslucent(SULFURIC_ACID);
        setRenderLayerTranslucent(MANA_SOLUTION);
        setRenderLayerTranslucent(MANA_POTION);
        setRenderLayerTranslucent(HEALING_POTION);
        setRenderLayerTranslucent(EMBER_POTION);
        setRenderLayerTranslucent(GLACE_POTION);
        setRenderLayerTranslucent(SURGE_POTION);
        setRenderLayerTranslucent(PHANTOM_POTION);
        setRenderLayerTranslucent(TREMOR_POTION);
        setRenderLayerTranslucent(MAGIC_POTION);
        setRenderLayerTranslucent(FLOW_POTION);
        setRenderLayerTranslucent(HOLLOW_POTION);
    }

    private static void setRenderLayerTranslucent(DeferredHolder<Fluid, Fluid> fluid) {
        ItemBlockRenderTypes.setRenderLayer(fluid.get(), RenderType.translucent());
    }*/
}
