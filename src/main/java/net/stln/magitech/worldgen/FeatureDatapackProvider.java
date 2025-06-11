package net.stln.magitech.worldgen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FeatureDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, WorldGenInit::Configured)
            .add(Registries.PLACED_FEATURE, WorldGenInit::bootstrapPlaced)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, WorldGenInit::bootstrapBiomeModifier);

    public FeatureDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Magitech.MOD_ID));
    }
}
