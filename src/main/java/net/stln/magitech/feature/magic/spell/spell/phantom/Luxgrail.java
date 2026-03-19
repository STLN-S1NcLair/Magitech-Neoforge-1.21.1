package net.stln.magitech.feature.magic.spell.spell.phantom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.MembraneParticleEffect;
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
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class Luxgrail extends Spell {

    public Luxgrail() {
        super(new SpellConfig.Builder(Element.PHANTOM, SpellShape.UTILITY, 4000, 70)
                .charge(80)
                .chargeSound(SoundInit.LUXGRAIL_CHARGE)
                .endSound(SoundInit.LUXGRAIL)
                .castAnim("wand_chant")
                .endAnim("wand_shoot")
        );
    }

    @Override
    public void endSpell(Level level, LivingEntity caster, @Nullable ItemStack wand, @Nullable InteractionHand hand) {
        BlockPos targetPos = caster.blockPosition();
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos.getX(), targetPos.getZ());
        BlockPos to = new BlockPos(targetPos.getX(), y, targetPos.getZ());

        if (!level.isClientSide) {
            SoundHelper.broadcastSound(level, caster, getConfig().endSound());


            TickScheduler.schedule(1, () -> {
                caster.teleportTo(to.getX() + 0.5, to.getY(), to.getZ() + 0.5);
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
        PointVFX.ring(level, pos, element, ElementParticles::glintParticle, up, 1, 0.1F, 0.5F, 0.0F);
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
        PointVFX.ring(level, pos, element, ElementParticles::glintParticle, up, 30, 0.2F, 1.2F, 0.05F);
        PointVFX.burst(level, pos.add(0, 0.1F, 0), element, (lvl, p, elm) -> PresetHelper.bigger(PresetHelper.longer(RingParticles.ringReversedParticle(lvl, p, up, elm)), 4.0F), 1, 0.0F);
        EntityVFX.powerupAura(level, element, caster, Section.cover(), 80);
        AreaVFX.areaLight(level, element, pos, 1.0F, 10.0F, 40);
    }
}
