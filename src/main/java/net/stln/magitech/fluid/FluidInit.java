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

    public static final DeferredHolder<FluidType, FluidType> MANA_POTION_TYPE = registerFluidType("mana_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> HEALING_POTION_TYPE = registerFluidType("healing_potion", new FluidType(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<Fluid, Fluid> SULFURIC_ACID = registerFluid("sulfuric_acid", SulfuricAcidFluid::new);

    public static final DeferredHolder<Fluid, Fluid> MANA_POTION = registerFluid("mana_potion", ManaPotionFluid::new);

    public static final DeferredHolder<Fluid, Fluid> HEALING_POTION = registerFluid("healing_potion", HealingPotionFluid::new);

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
        registerIClientFluidExtension(event, "block/mana_potion_still", MANA_POTION_TYPE);
        registerIClientFluidExtension(event, "block/healing_potion_still", HEALING_POTION_TYPE);
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
        setRenderLayerTranslucent(MANA_POTION);
        setRenderLayerTranslucent(HEALING_POTION);
    }

    private static void setRenderLayerTranslucent(DeferredHolder<Fluid, Fluid> fluid) {
        ItemBlockRenderTypes.setRenderLayer(fluid.get(), RenderType.translucent());
    }

}
