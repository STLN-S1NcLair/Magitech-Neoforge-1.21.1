package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.ItemTagsProvider;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;

import java.util.concurrent.CompletableFuture;

public class ModCuriosItemTagsProvider extends ItemTagsProvider {
    public ModCuriosItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                     CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags,
                                     ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, "curios", helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTagKeys.CURIOS_BELT).add(ItemInit.TOOL_BELT.get());
        tag(ItemTagKeys.CURIOS_RING).add(ItemInit.FLUXIUM_RING.get(), ItemInit.MANA_RING.get(), ItemInit.ARDOR_RING.get(),
                ItemInit.QUENCH_RING.get(), ItemInit.CHARGEBIND_RING.get(), ItemInit.CELERITAS_RING.get(),
                ItemInit.CRACK_RING.get(), ItemInit.PROTECTION_RING.get(), ItemInit.UPDRAFT_RING.get(),
                ItemInit.DISTORTION_RING.get(), ItemInit.UMBRAL_RING.get(), ItemInit.DAWN_RING.get(),
                ItemInit.FLUXBOUND_RING.get());
    }
}
