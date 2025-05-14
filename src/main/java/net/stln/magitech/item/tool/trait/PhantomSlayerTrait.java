package net.stln.magitech.item.tool.trait;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.item.tool.toolitem.SpellCasterItem;
import net.stln.magitech.particle.particle_option.MembraneParticleEffect;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.particle.particle_option.SquareFieldParticleEffect;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.EntityUtil;
import org.joml.Vector3f;

public class PhantomSlayerTrait extends Trait {

    @Override
    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand, boolean isHost) {
        super.traitAction(player, level, target, lookingPos, stack, traitLevel, stats, hand, isHost);

        Vec3 playerEyePos = player.getEyePosition();
        Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
        if (lookingPos != null && player.position().distanceTo(lookingPos) < player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * Math.sqrt(traitLevel) && player.getAttackStrengthScale(0.5F) > 0.7F && target != null) {

            if (stack.getItem() instanceof SpellCasterItem) {
                player.addDeltaMovement(forward.scale(-1.2).add(0, 1.0, 0));
                if (traitLevel > 1 && !level.isClientSide) {
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, traitLevel * 20, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, traitLevel * 5, 0));
                }
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, traitLevel * 10, 0));
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.PHANTOM_SLAYER_DASH.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
                EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), player, 60);
                stack.hurtAndBreak(3, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            } else {
                Vec3 playerPos = new Vec3(player.getX(), player.getY(0.5), player.getZ());
                Vec3 distance = lookingPos.subtract(playerEyePos).normalize().multiply(2, 2, 2);
                Vec3 teleportPos = lookingPos.add(distance);
                BlockHitResult blockResult = level.clip(new ClipContext(lookingPos, teleportPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                boolean flag = blockResult.getType() != HitResult.Type.BLOCK;
                if (flag) {

                    if (traitLevel > 1 && !level.isClientSide) {

                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, traitLevel * 10, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, traitLevel * 5, 0));
                    }
                    ((PartToolItem) stack.getItem()).applyElementDamage(player, target, stack);
                    player.attack(target);
                    EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), player, 20);

                    Vec3 delta = player.getDeltaMovement();

                    player.setPos(lookingPos.add(distance));
                    if (delta.length() < 0.5) {
                        delta.normalize().multiply(0.5, 0.5, 0.5);
                    }
                    player.setDeltaMovement(delta);
                    EffectUtil.lineEffect(level, new MembraneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2.0F, 1, 0), playerPos, teleportPos, 7, false);
                    stack.hurtAndBreak(3, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.PHANTOM_SLAYER_DASH.get(), SoundSource.PLAYERS, 1.0F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
                    EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), player, 60);
                }
            }
        }
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.tick(player, level, stack, traitLevel, stats);
        if (level.isClientSide && Minecraft.getInstance().isLocalPlayer(player.getUUID()) && player.tickCount % 10 == 0) {
            Vec3 playerEyePos = player.getEyePosition();
            Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
            double mul = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) * Math.sqrt(traitLevel);
            Vec3 maxReachPos = playerEyePos.add(forward.multiply(mul, mul, mul));

            EntityHitResult result = EntityUtil.getEntityHitResult(player, playerEyePos, maxReachPos, level);

            if (result != null && player.getAttackStrengthScale(0.5F) > 0.7F) {

                Entity target = result.getEntity();
                if (target instanceof LivingEntity) {
                    Vec3 position = new Vec3(target.getX(), target.getY(0.5), target.getZ());
                    Vec3 distance = position.subtract(playerEyePos).normalize().multiply(2, 2, 2);
                    Vec3 teleportPos = position.add(distance);
                    BlockHitResult blockResult = level.clip(new ClipContext(position, teleportPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                    boolean flag = blockResult.getType() == HitResult.Type.MISS;

                    if (flag) {
                        level.addParticle(new SquareFieldParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), target.getX(), target.getY() + 0.1, target.getZ(), 0, 0, 0);
                        EffectUtil.entityEffect(level, new UnstableSquareParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), target, 5);
                    }
                }
            }
        }
    }

    @Override
    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onAttackEntity(player, level, stack, traitLevel, stats, target);
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, traitLevel * 10, 0));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, traitLevel * 5, 0));
        EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), player, 20);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.PHANTOM_BUFF.get(), SoundSource.PLAYERS, 0.5F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (!level.isClientSide) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, traitLevel * 10, 0));
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, traitLevel * 5, 0));
        }
        EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 1.0F, 0.5F), new Vector3f(1.0F, 1.0F, 0.5F), 1F, 1, 0), player, 20);
        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.PHANTOM_BUFF.get(), SoundSource.PLAYERS, 0.5F, 0.7F + (player.getRandom().nextFloat() * 0.6F));
    }

    @Override
    public int getColor() {
        return 0xFFFF80;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.phantom_slayer");
    }
}
