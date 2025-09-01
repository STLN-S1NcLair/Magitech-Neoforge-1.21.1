package net.stln.magitech.worldgen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.stln.magitech.Magitech;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class FeatureDataGenerator {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new FeatureDatapackProvider(packOutput, lookupProvider));
    }
}
