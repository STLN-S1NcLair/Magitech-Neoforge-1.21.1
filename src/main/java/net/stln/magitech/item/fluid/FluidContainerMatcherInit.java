package net.stln.magitech.item.fluid;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.stln.magitech.Magitech;
import net.stln.magitech.fluid.FluidInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.registry.DeferredFluidContainerMatcher;
import net.stln.magitech.registry.DeferredFluidContainerMatcherRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FluidContainerMatcherInit {
    public static final DeferredFluidContainerMatcherRegister REGISTER = new DeferredFluidContainerMatcherRegister(Magitech.MOD_ID);

     public static final DeferredFluidContainerMatcher<FluidContainerMatcher> WATER_FLASK = register("water_flask", makeSimpleMatcher(ItemInit.ALCHEMICAL_FLASK, () -> Fluids.WATER, ItemInit.WATER_FLASK));

     public static final DeferredFluidContainerMatcher<FluidContainerMatcher> SULFURIC_ACID_FLASK = register("sulfuric_acid_flask", makeSimpleMatcher(ItemInit.ALCHEMICAL_FLASK, FluidInit.SULFURIC_ACID, ItemInit.SULFURIC_ACID_FLASK));

    public static FluidContainerMatcher makeSimpleMatcher(ItemLike emptyContainer, Supplier<Fluid> fluid, ItemLike filledContainer) {
        return new FluidContainerMatcher(emptyContainer, () -> Ingredient.of(emptyContainer), fluid, () -> FluidIngredient.of(fluid.get()), filledContainer);
    }

    private static @NotNull DeferredFluidContainerMatcher<FluidContainerMatcher> register(@NotNull String path, @NotNull FluidContainerMatcher fluidContainerMatcher) {
        return REGISTER.register(path, () -> fluidContainerMatcher);
    }
    
    public static void registerFluidContainerMatchers(IEventBus bus) {
        Magitech.LOGGER.info("Registering Fluid Container Matcher for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
