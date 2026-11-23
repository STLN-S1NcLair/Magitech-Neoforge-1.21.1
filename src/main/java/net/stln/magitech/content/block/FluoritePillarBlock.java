package net.stln.magitech.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.block.block_entity.FluoritePillarBlockEntity;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

public class FluoritePillarBlock extends RotatedPillarBlock implements EntityBlock {
    public static final MapCodec<FluoritePillarBlock> CODEC = simpleCodec(FluoritePillarBlock::new);
    public static final int PARTICLE_ORBIT_PERIOD = 200;
    private static final double ORBIT_RADIUS = 0.28;
    private static final double ORBIT_LAYER_OFFSET = 0.15;

    public FluoritePillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<FluoritePillarBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluoritePillarBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide || !blockEntityType.equals(BlockInit.FLUORITE_PILLAR_ENTITY.get())) {
            return null;
        }
        return (tickLevel, tickPos, tickState, blockEntity) -> {
            if (blockEntity instanceof FluoritePillarBlockEntity fluoritePillar) {
                fluoritePillar.clientTick(tickLevel, tickPos, tickState);
            }
        };
    }

    public static void spawnOrbitingParticles(Level level, BlockPos pos, Axis axis, int tickCounter) {
        float theta = Mth.lerp((float) (tickCounter % PARTICLE_ORBIT_PERIOD) / PARTICLE_ORBIT_PERIOD, 0, Mth.TWO_PI);
        theta += Mth.TWO_PI / 3 * (tickCounter % 3);
        Vec3 center = pos.getCenter();
        Vec3 axisOffset = axisDirection(axis).scale(ORBIT_LAYER_OFFSET);
        spawnOrbitingParticle(level, center.add(axisOffset), axis, theta);
        spawnOrbitingParticle(level, center.subtract(axisOffset), axis, -theta);
    }

    private static void spawnOrbitingParticle(Level level, Vec3 center, Axis axis, double angle) {
        Vec3 direction = orbitDirection(axis, angle);
        Vec3 particlePosition = center.add(direction.scale(ORBIT_RADIUS));
        PointVFX.spray(level, particlePosition, Element.MANA, (particleLevel, particlePos, element) -> PresetHelper.modifyBloomTransparency(
                PresetHelper.longer(SquareParticles.squareParticle(particleLevel, particlePos, element), 2.0F), 0.5F),
                direction, 1, 0.0F, 0.0F);
    }

    private static Vec3 axisDirection(Axis axis) {
        return switch (axis) {
            case X -> new Vec3(1.0, 0.0, 0.0);
            case Y -> new Vec3(0.0, 1.0, 0.0);
            case Z -> new Vec3(0.0, 0.0, 1.0);
        };
    }

    private static Vec3 orbitDirection(Axis axis, double angle) {
        double firstOffset = Math.cos(angle);
        double secondOffset = Math.sin(angle);
        return switch (axis) {
            case X -> new Vec3(0.0, firstOffset, secondOffset);
            case Y -> new Vec3(firstOffset, 0.0, secondOffset);
            case Z -> new Vec3(firstOffset, secondOffset, 0.0);
        };
    }
}
