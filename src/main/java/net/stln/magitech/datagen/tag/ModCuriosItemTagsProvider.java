package net.stln.magitech.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModCuriosItemTagsProvider extends HolderTagsProvider<Item> {
    public ModCuriosItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, Registries.ITEM, lookupProvider, "curios", helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ItemTagKeys.CURIOS_BELT).add(ItemInit.TOOL_BELT);
        tag(ItemTagKeys.CURIOS_HEAD).add(ItemInit.SPECTACLES_OF_INSPECTION);
        tag(ItemTagKeys.CURIOS_RING).add(ItemInit.FLUXIUM_RING, ItemInit.MANA_RING, ItemInit.ARDOR_RING,
                ItemInit.QUENCH_RING, ItemInit.CHARGEBIND_RING, ItemInit.CELERITAS_RING,
                ItemInit.CRACK_RING, ItemInit.PROTECTION_RING, ItemInit.UPDRAFT_RING,
                ItemInit.DISTORTION_RING, ItemInit.UMBRAL_RING, ItemInit.DAWN_RING,
                ItemInit.FLUXBOUND_RING);
    }
}
