package net.stln.magitech.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.stln.magitech.Magitech;

public class FluidTagKeys {

    public static TagKey<Fluid> ALCHEMICAL_FLASK_CONTAINABLE = TagKey.create(Registries.FLUID, Magitech.id("alchemical_flask_containable"));
}
