package net.stln.magitech.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ManaVesselBlock extends BaseEntityBlock {

    public static final MapCodec<AlchemetricPylonBlock> CODEC = simpleCodec(AlchemetricPylonBlock::new);

    protected ManaVesselBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaVesselBlockEntity(blockPos, blockState);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        for (int i = 0; i < 2; i++) {
            double x = center.x + Mth.nextDouble(random, -0.6, 0.6);
            double y = center.y + Mth.nextDouble(random, -0.6, 0.6);
            double z = center.z + Mth.nextDouble(random, -0.6, 0.6);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, 0, 15, 1.0F), x, y, z, 0, 0, 0);
        }
        for (int i = 0; i < 2; i++) {
            int direction = i == 0 ? -1 : 1;
            double x = center.x + Mth.nextDouble(random, -0.2, 0.2);
            double y = center.y + 0.5 * direction;
            double z = center.z + Mth.nextDouble(random, -0.2, 0.2);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 1.0F, 3, Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.0F), x, y, z, 0, 0.03 * direction, 0);
        }
        for (int i = 0; i < 4; i++) {
            double x2 = center.x + Mth.nextDouble(random, -0.5, 0.5);
            double y2 = center.y + Mth.nextDouble(random, -0.5, 0.5);
            double z2 = center.z + Mth.nextDouble(random, -0.5, 0.5);
            level.addParticle(new SquareParticleEffect(new Vector3f(0.8F, 1.0F, 0.7F), new Vector3f(0.0F, 1.0F, 0.9F), 0.5F, 1, Mth.nextFloat(random, -0.1F, 0.1F), 15, 1.0F), x2, y2, z2, 0, 0.03, 0);
        }
    }
}
