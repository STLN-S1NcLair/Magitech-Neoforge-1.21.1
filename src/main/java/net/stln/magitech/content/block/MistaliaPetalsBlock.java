package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import org.joml.Vector3f;

public class MistaliaPetalsBlock extends PinkPetalsBlock {
    public MistaliaPetalsBlock(Properties p_273335_) {
        super(p_273335_);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PointVFX.burst(level, pos.getBottomCenter().add(0, 0.05, 0).add(VectorHelper.randScaledRandom(level.random).scale(0.4F).multiply(1, 0, 1)),
                Element.MANA, (lvl, vec, elm) -> PresetHelper.bigger(SquareParticles.squareGravityParticle(lvl, vec, elm, -0.01F), 0.2F), 2, 0.05F);
    }

}
