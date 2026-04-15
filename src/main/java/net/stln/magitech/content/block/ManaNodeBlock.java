package net.stln.magitech.content.block;

import com.mojang.datafixers.util.Function3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaNode;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

import java.util.Set;

public class ManaNodeBlock extends NodeBlock implements IManaNode {

    protected final int range;

    public ManaNodeBlock(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of(state.getValue(FACING).getOpposite());
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3i normal = state.getValue(FACING).getNormal();
        Vec3 dir = new Vec3(normal.getX(), normal.getY(), normal.getZ());
        Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier = (lvl, vec, elm) -> PresetHelper.longer(SquareParticles.squareShrinkParticle(lvl, vec, elm));
        PointVFX.spray(level, pos.getCenter(), Element.MANA, supplier, dir, 1, 0.03F, 0.02F);
        PointVFX.ring(level, pos.getCenter().add(dir.scale(-0.5)), Element.MANA, supplier, dir.reverse(), 1, 0.05F, 0.05F, 0.0F);
    }
}
