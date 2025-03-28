package net.stln.magitech.item.renderer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.stln.magitech.Magitech;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModelGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new ToolModelProvider(packOutput, Magitech.MOD_ID, existingFileHelper));
    }
}
