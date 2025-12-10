package net.stln.magitech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.damage.DamageTypeInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import org.joml.Vector3f;

public class ManaBerryBushBlock extends SweetBerryBushBlock {
    public ManaBerryBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ItemInit.MANA_BERRIES.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (i > 1) {
            int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ItemInit.MANA_BERRIES.get(), j + (flag ? 1 : 0)));
            level.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F
            );
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(1));
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if ((long) level.random.nextInt(200) <= level.getGameTime() % 200L) {
            Vec3 vec3 = Vec3.atCenterOf(pos);
            double d0 = vec3.x + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d1 = vec3.y + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d2 = vec3.z + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d3 = Mth.nextDouble(level.random, -0.05, 0.05);
            double d4 = Mth.nextDouble(level.random, 0, 0.1);
            double d5 = Mth.nextDouble(level.random, -0.05, 0.05);
            level.addParticle(new UnstableSquareParticleEffect(new Vector3f(0.7F, 1.0F, 0.2F), new Vector3f(0.5F, 1.0F, 1.0F), 0.5F, 1, 0, 15, 1.0F), d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
            if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    entity.hurt(level.damageSources().source(DamageTypeInit.MANA_BERRY_BUSH), 1.0F);
                }
            }
        }
    }
}
