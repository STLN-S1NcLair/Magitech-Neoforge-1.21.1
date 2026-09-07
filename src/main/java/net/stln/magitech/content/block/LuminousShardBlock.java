package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

public class LuminousShardBlock extends Block {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 4, 4, 12, 12, 12)
    );

    public LuminousShardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PointVFX.spray(level, pos.getCenter(), Element.PHANTOM, ((level1, vec3, element) -> PresetHelper.bigger(PresetHelper.longer(PresetHelper.friction(SquareParticles.squareParticle(level1, vec3, element), 0.98F)))), new Vec3(0, 1, 0), 1, 0.02F, 0F);
    }
}
