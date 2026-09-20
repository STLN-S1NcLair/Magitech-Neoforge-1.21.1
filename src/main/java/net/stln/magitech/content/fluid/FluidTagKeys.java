package net.stln.magitech.content.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public final class FluidTagKeys {
    public static final TagKey<Fluid> SULFURIC_ACID = TagKey.create(
            Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath("c", "sulfuric_acid")
    );

    private FluidTagKeys() {
    }
}
