package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.VoidGlowParticleEffect;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class Tenebport extends Spell {

    public Tenebport() {
        super(new SpellConfig.Builder(Element.HOLLOW, SpellShape.UTILITY, 6000, 80)
                .charge(100)
                .chargeSound(SoundInit.TENEBPORT_CHARGE)
                .endSound(SoundInit.TENEBPORT)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        // 参照渡し防止
        Vec3 pos = caster.position().add(0, 0, 0);
        BlockPos targetPos = caster.blockPosition();
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos.getX(), targetPos.getZ());
        targetPos = new BlockPos(targetPos.getX(), y, targetPos.getZ());

        if (!level.isClientSide) {
            SoundHelper.broadcastSound(level, caster, getConfig().endSound());


            TickScheduler.schedule(2, () -> {
                SoundHelper.broadcastSound(level, caster, getConfig().endSound());
                caster.fallDistance = 0;
            }, level.isClientSide);

            ServerPlayer serverPlayer = (ServerPlayer) caster;
            DimensionTransition respawnPos = serverPlayer.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING);
            serverPlayer.changeDimension(respawnPos);


            caster.teleportTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
        } else {
            addTeleportVFX(level, caster, pos);
        }
    }

    public void addTeleportVFX(Level level, LivingEntity caster, Vec3 from) {
        Vec3 above = from.add(0, 10, 0);

        EffectHelper.lineEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F), from, above, 4, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), above.toVector3f(), 5F, 1, 1, 5, 1), from.x, from.y - 0.5, from.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(1, 21), 1.0F),
                    from.x, from.y, from.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }

        TickScheduler.schedule(2, () -> {
            Level newLevel = caster.level();
            Vec3 newPos = caster.position();
            Vec3 newAbove = newPos.add(0, 10, 0);
            EffectHelper.lineEffect(newLevel, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, newLevel.random.nextInt(1, 21), 1.0F), newPos, newAbove, 4, false);
            newLevel.addParticle(new BeamParticleEffect(new Vector3f(0.3F, 0.0F, 1.0F), new Vector3f(0.5F, 0.0F, 1.0F), newAbove.toVector3f(), 5F, 1, 1, 5, 1), newPos.x, newPos.y - 0.5, newPos.z, 0, 0, 0);
            for (int i = 0; i < 20; i++) {
                newLevel.addParticle(new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, newLevel.random.nextInt(1, 21), 1.0F),
                        newPos.x, newPos.y, newPos.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
            }
        }, level.isClientSide);
    }
}
