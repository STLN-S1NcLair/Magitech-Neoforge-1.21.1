package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import org.joml.Vector3f;

public class FluoriteCrystalClusterBlock extends CrystalClusterBlock {

    public FluoriteCrystalClusterBlock(IntProvider xpRange, Properties properties) {
        super(xpRange, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vec3 vec3 = Vec3.atCenterOf(pos);
        PointVFX.burst(level, vec3, Element.MANA, SquareParticles::squareParticle, 2, 0.5F);
        PointVFX.zap(level, vec3, Element.MANA, 1, 1.0F, 2.0F, 2.0F, 0.5F, 5);
    }
}
