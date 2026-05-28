package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.stln.magitech.content.damage.DamageTypeInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;

import java.awt.*;

public class QuartzPlantBushBlock extends SweetBerryBushBlock {
    public QuartzPlantBushBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ItemInit.QUARTZ_PLANT.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (i > 1) {
            int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ItemInit.QUARTZ_PLANT.get(), j + (flag ? 1 : 0)));
            level.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F
            );
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(0));
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PointVFX.burstColored(level, pos.getCenter().add(VectorHelper.randScaledRandom(level.random).scale(0.3F)),
                new Color(0xF5FFEE), new Color(0xD7E2FF), (lvl, vec, pri, sec) -> PresetHelper.modify(PresetHelper.smaller(SquareParticles.squareParticleColored(lvl, vec, pri, sec)),
                        worldParticleBuilder -> worldParticleBuilder.setGravity(0.01F)), 2, 0.05F);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    }
}
