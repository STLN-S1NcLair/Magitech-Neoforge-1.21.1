package net.stln.magitech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.api.mana.flow.network.connectable.IManaNode;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import net.stln.magitech.util.VoxelShapeUtil;
import org.joml.Vector3f;

import java.util.Set;

public class ManaNodeBlock extends NodeBlock implements IManaNode {

    public ManaNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of(state.getValue(FACING).getOpposite());
    }

    @Override
    public int getRange() {
        return 3;
    }
}
