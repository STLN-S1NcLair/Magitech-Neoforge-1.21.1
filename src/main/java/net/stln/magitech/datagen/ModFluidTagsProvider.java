package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.FluidTagsProvider;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.fluid.FluidInit;
import net.stln.magitech.content.fluid.FluidTagKeys;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, lookupProvider, Magitech.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FluidTagKeys.SULFURIC_ACID).add(FluidInit.SULFURIC_ACID.get());
    }
}
