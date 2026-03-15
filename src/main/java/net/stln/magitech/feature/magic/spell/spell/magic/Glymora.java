package net.stln.magitech.feature.magic.spell.spell.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.SpraySpell;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;

public class Glymora extends SpraySpell {

    public Glymora() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.SPRAY, 20, 15)
                .continuous(2)
                .property(SpellPropertyInit.CONTINUOUS_DAMAGE, 4.0F)
                .tickSound(SoundInit.GLYMORA, 5)
                .castAnim("wand_spray")
        );
    }

    @Override
    protected void tickVFX(Level level, LivingEntity caster, int ticks, boolean charging) {
        if (!charging) {
            Element element = this.getConfig().element();
            Vec3 forward = Vec3.directionFromRotation(caster.getRotationVector());
            Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
            Vec3 offset = bodyPos.add(forward.scale(1));
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(SquareParticles.squareParticle(lvl, pos, elm)),
                    forward, 20, 0.5F, 0.4F);
            PointVFX.spray(level, offset, element,
                    (lvl, pos, elm) -> PresetHelper.bigger(ElementParticles.runeParticle(lvl, pos, elm)),
                    forward, 10, 0.5F, 0.4F);
        }
    }
}
