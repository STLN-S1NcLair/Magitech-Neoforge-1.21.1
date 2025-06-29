package net.stln.magitech.worldgen.tree;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.worldgen.WorldGenInit;

import java.util.Optional;

public class TreeGrowerInit {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Magitech.MOD_ID);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<RandomBranchingTrunkPlacer>> RANDOM_BRANCHING_TRUNK_PLACER = register("random_branching_trunk_placer", RandomBranchingTrunkPlacer.CODEC);

    public static final TreeGrower CELIFERN = new TreeGrower(Magitech.MOD_ID + ":celifern", Optional.empty(), Optional.of(WorldGenInit.CELIFERN_CONFIGURED_KEY), Optional.empty());


    private static <P extends TrunkPlacer> DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<P>> register(String name, MapCodec<P> codec) {
        return TRUNK_PLACER_TYPES.register(name, () -> new TrunkPlacerType<>(codec));
    }


    public static void registerTrunkPlacerTypes(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Trunk Placer Types for" + Magitech.MOD_ID);
        TRUNK_PLACER_TYPES.register(eventBus);
    }
}
