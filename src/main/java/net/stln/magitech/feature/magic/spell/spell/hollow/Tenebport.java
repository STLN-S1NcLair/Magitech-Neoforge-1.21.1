package net.stln.magitech.feature.magic.spell.spell.hollow;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.preset.AreaVFX;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.RingParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;

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
        if (!level.isClientSide) {
            SoundHelper.broadcastSound(level, caster, getConfig().endSound());


            TickScheduler.schedule(1, () -> {
                ServerPlayer serverPlayer = (ServerPlayer) caster;
                DimensionTransition respawnPos = serverPlayer.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING);
                serverPlayer.changeDimension(respawnPos);
                caster.teleportTo(respawnPos.pos().x + 0.5, respawnPos.pos().y, respawnPos.pos().z + 0.5);
                caster.fallDistance = 0;
                SoundHelper.broadcastSound(level, caster, getConfig().endSound());
            }, level.isClientSide);
        }
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        Element element = getConfig().element();
        Vec3 pos = caster.position();
        Vec3 up = new Vec3(0, 1, 0);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 1);
        PointVFX.ringSquare(level, pos, element, up, 1, 0.15F, 0.5F, 0.0F);
        PointVFX.ring(level, pos, element, ElementParticles::riftParticle, up, 1, 0.15F, 0.5F, 0.0F);
    }

    @Override
    protected void endVFX(Level level, LivingEntity caster) {
        spawnTeleportVFX(level, caster, caster.position());
    }

    public void spawnTeleportVFX(Level level, LivingEntity caster, Vec3 from) {
        teleportVFX(level, caster, from);

        TickScheduler.schedule(2, () -> {
            Level newLevel = caster.level();
            teleportVFX(newLevel, caster, caster.position());
        }, level.isClientSide);
    }

    protected void teleportVFX(Level level, LivingEntity caster, Vec3 pos) {
        Element element = getConfig().element();
        Vec3 up = new Vec3(0, 1, 0);
        PointVFX.ringSquare(level, pos, element, up, 30, 0.2F, 1.2F, 0.05F);
        PointVFX.ring(level, pos, element, ElementParticles::riftParticle, up, 30, 0.2F, 1.2F, 0.05F);
        PointVFX.burst(level, pos.add(0, 0.1F, 0), element, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm)), 4.0F), 1, 0.0F);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 80);
        AreaVFX.areaLight(level, element, pos, 1.0F, 10.0F, 40);
    }
}
