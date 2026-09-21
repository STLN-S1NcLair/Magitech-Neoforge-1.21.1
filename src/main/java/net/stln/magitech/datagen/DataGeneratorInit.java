package net.stln.magitech.datagen;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.datagen.tag.*;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class DataGeneratorInit {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        event.createProvider((output, future) -> new ModBlockTagsProvider(output, future, existingFileHelper));
        event.createProvider((output, future) -> new ModItemTagsProvider(output, future, existingFileHelper));
        event.createProvider((output, future) -> new ModCuriosItemTagsProvider(output, future, existingFileHelper));
        event.createProvider((output, future) -> new ModFluidTagsProvider(output, future, existingFileHelper));
        event.createProvider((output, future) -> new ModBiomeTagsProvider(output, future, existingFileHelper));
        event.createProvider((output, future) -> new ModDamageTypeTagsProvider(output, future, existingFileHelper));
        event.createProvider(ModDataMapProvider::new);

        event.createProvider(ModRecipeProvider::new);
        event.createProvider(ModComponentRecipeProvider::new);

        event.createProvider((output, future) -> new LootTableProvider(
                output,
                Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModEntityLootTableProvider::new, LootContextParamSets.ENTITY)
                ),
                future
        ));

        event.createProvider(output -> new ModItemModelProvider(output, existingFileHelper));
        event.createProvider(output -> new ModBlockStateProvider(output, existingFileHelper));
        event.createProvider(output -> new ModSoundDefinitionsProvider(output, existingFileHelper));
    }
}
