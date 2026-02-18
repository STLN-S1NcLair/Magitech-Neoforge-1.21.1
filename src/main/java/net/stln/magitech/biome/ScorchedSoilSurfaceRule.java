package net.stln.magitech.biome;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.stln.magitech.block.BlockInit;

public class ScorchedSoilSurfaceRule {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource SCORCHED_GRASS_SOIL = makeStateRule(BlockInit.SCORCHED_GRASS_SOIL.get());
    private static final SurfaceRules.RuleSource SCORCHED_SOIL = makeStateRule(BlockInit.SCORCHED_SOIL.get());

    protected static SurfaceRules.RuleSource makeRule() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource scorchedSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(isAtOrAboveWaterLevel, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SCORCHED_GRASS_SOIL), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SCORCHED_SOIL))),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SCORCHED_SOIL));

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(BiomeInit.SCORCHED_PLAINS), scorchedSurface),

                // Default child a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
