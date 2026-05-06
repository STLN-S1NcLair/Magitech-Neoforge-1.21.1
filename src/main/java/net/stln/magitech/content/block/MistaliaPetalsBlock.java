package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import org.joml.Vector3f;

public class MistaliaPetalsBlock extends PinkPetalsBlock {
    public MistaliaPetalsBlock(Properties p_273335_) {
        super(p_273335_);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PointVFX.burst(level, pos.getCenter(), Element.MANA, SquareParticles::squareParticle, 2, 0.1F);
    }

}
