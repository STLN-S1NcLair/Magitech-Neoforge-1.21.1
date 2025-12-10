package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import org.joml.Vector3f;

public class IlluminationTrait extends Trait {

    @Override
    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand, boolean isHost) {
        super.traitAction(player, level, target, lookingPos, stack, traitLevel, stats, hand, isHost);

        Vec3 max = player.getEyePosition().add(player.getLookAngle().scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue() * traitLevel));
        BlockHitResult result = level.clip(new ClipContext(player.getEyePosition(), max, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        BlockPos pos = result.getBlockPos();
        Direction direction = result.getDirection();
        BlockPos placePos = pos.relative(direction);
        if (result.getType() == BlockHitResult.Type.BLOCK && level.getBlockState(placePos).isAir()) {

            if (!level.isClientSide() && !isHost) {
                stack.hurtAndBreak(5, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            level.setBlock(placePos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15), 3);
            for (int i = 0; i < 20; i++) {
                level.addParticle(new PowerupParticleEffect(new Vector3f(1.0F, 0.8F, 0.5F), new Vector3f(1.0F, 0.6F, 0.3F), 1F, 1, 0, 15, 1.0F),
                        placePos.getX() + player.getRandom().nextFloat(), placePos.getY() + player.getRandom().nextFloat(), placePos.getZ() + player.getRandom().nextFloat(), 0, 0, 0);
            }

            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
        }
    }

    @Override
    public int getColor() {
        return 0xFFC080;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.illumination");
    }
}
