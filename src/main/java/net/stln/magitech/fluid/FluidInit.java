package net.stln.magitech.fluid;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

@EventBusSubscriber
public class FluidInit {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Magitech.MOD_ID);

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Magitech.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> SULFURIC_ACID_TYPE = registerFluidType("sulfuric_acid", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> MANA_SOLUTION_TYPE = registerFluidType("mana_solution", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> MANA_POTION_TYPE = registerFluidType("mana_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> HEALING_POTION_TYPE = registerFluidType("healing_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> EMBER_POTION_TYPE = registerFluidType("ember_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> GLACE_POTION_TYPE = registerFluidType("glace_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> SURGE_POTION_TYPE = registerFluidType("surge_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> PHANTOM_POTION_TYPE = registerFluidType("phantom_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> TREMOR_POTION_TYPE = registerFluidType("tremor_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> MAGIC_POTION_TYPE = registerFluidType("magic_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> FLOW_POTION_TYPE = registerFluidType("flow_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> HOLLOW_POTION_TYPE = registerFluidType("hollow_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<Fluid, Fluid> SULFURIC_ACID = registerFluid("sulfuric_acid", SulfuricAcidFluid::new);

    public static final DeferredHolder<Fluid, Fluid> MANA_SOLUTION = registerFluid("mana_solution", ManaSolutionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> MANA_POTION = registerFluid("mana_potion", ManaPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> HEALING_POTION = registerFluid("healing_potion", HealingPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> EMBER_POTION = registerFluid("ember_potion", EmberPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> GLACE_POTION = registerFluid("glace_potion", GlacePotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> SURGE_POTION = registerFluid("surge_potion", SurgePotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> PHANTOM_POTION = registerFluid("phantom_potion", PhantomPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> TREMOR_POTION = registerFluid("tremor_potion", TremorPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> MAGIC_POTION = registerFluid("magic_potion", MagicPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> FLOW_POTION = registerFluid("flow_potion", FlowPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> HOLLOW_POTION = registerFluid("hollow_potion", HollowPotionFluid::new);

    private static <S extends FluidType> DeferredHolder<FluidType, S> registerFluidType(String name, S type) {
        return FLUID_TYPES.register(name, () -> type);
    }

    private static <S extends Fluid> DeferredHolder<Fluid, S> registerFluid(String name, Supplier<S> type) {
        return FLUIDS.register(name, type);
    }

    public static void registerFluids(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Fluids for" + Magitech.MOD_ID);
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerIClientFluidExtensions(RegisterClientExtensionsEvent event) {
        Magitech.LOGGER.info("Registering IClientFluidExtensions for" + Magitech.MOD_ID);
        registerIClientFluidExtension(event, "block/sulfuric_acid_still", SULFURIC_ACID_TYPE);
        registerIClientFluidExtension(event, "block/mana_solution_still", MANA_SOLUTION_TYPE);
        registerIClientFluidExtension(event, "block/mana_potion_still", MANA_POTION_TYPE);
        registerIClientFluidExtension(event, "block/healing_potion_still", HEALING_POTION_TYPE);
        registerIClientFluidExtension(event, "block/ember_potion_still", EMBER_POTION_TYPE);
        registerIClientFluidExtension(event, "block/glace_potion_still", GLACE_POTION_TYPE);
        registerIClientFluidExtension(event, "block/surge_potion_still", SURGE_POTION_TYPE);
        registerIClientFluidExtension(event, "block/phantom_potion_still", PHANTOM_POTION_TYPE);
        registerIClientFluidExtension(event, "block/tremor_potion_still", TREMOR_POTION_TYPE);
        registerIClientFluidExtension(event, "block/magic_potion_still", MAGIC_POTION_TYPE);
        registerIClientFluidExtension(event, "block/flow_potion_still", FLOW_POTION_TYPE);
        registerIClientFluidExtension(event, "block/hollow_potion_still", HOLLOW_POTION_TYPE);
    }

    private static void registerIClientFluidExtension(RegisterClientExtensionsEvent event, String s, DeferredHolder<FluidType, FluidType> sulfuricAcidType) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private final ResourceLocation stillTexture = Magitech.id(s);

            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

        }, sulfuricAcidType);
    }

    public static void registerFluidRenderTypes() {
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
    }

}
