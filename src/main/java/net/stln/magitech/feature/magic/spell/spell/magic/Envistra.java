package net.stln.magitech.feature.magic.spell.spell.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.BlinkSpell;
import net.stln.magitech.feature.magic.spell.SpellConfig;
import net.stln.magitech.feature.magic.spell.SpellShape;
import net.stln.magitech.feature.magic.spell.property.SpellPropertyInit;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.RuneParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SquareParticleEffect;
import org.joml.Vector3f;

public class Envistra extends BlinkSpell {

    public Envistra() {
        super(new SpellConfig.Builder(Element.MAGIC, SpellShape.DASH, 40, 50)
                .charge(5)
                .property(SpellPropertyInit.DAMAGE, 6.0F)
                .property(SpellPropertyInit.MAX_RANGE, 10F)
                .endSound(SoundInit.ENVISTRA)
                .castAnim("charge_wand")
                .endAnim("wand_blink")
        );
    }

    @Override
    protected void addBlinkVFX(Level level, LivingEntity caster, Vec3 start, Vec3 end) {
        Vec3 back = Vec3.directionFromRotation(caster.getRotationVector()).scale(-1);
        Vec3 bodyPos = caster.position().add(0, caster.getBbHeight() * 0.7, 0);
        Vec3 offset = bodyPos.add(back.scale(1));
        EffectHelper.lineEffect(level, new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 20), 0.9F), start, end, 2, false);
        level.addParticle(new BeamParticleEffect(new Vector3f(1.0F, 0.0F, 0.7F), new Vector3f(1.0F, 0.0F, 0.3F), end.toVector3f(), 0.7F, 1, 1, 5, 1), start.x, start.y, start.z, 0, 0, 0);
        for (int i = 0; i < 20; i++) {
            level.addParticle(new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 1.0F, 1, 0, level.random.nextInt(5, 20), 0.9F),
                    end.x, end.y, end.z, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3, (caster.getRandom().nextFloat() - 0.5) / 3);
        }
        for (int j = 0; j < 10; j++) {
            level.addParticle(new RuneParticleEffect(new Vector3f(1), new Vector3f(1),
                            5F, 1, 0.3F, level.random.nextInt(5, 20), 0.9F), offset.x + (caster.getRandom().nextFloat() - 0.5) / 4, offset.y + (caster.getRandom().nextFloat() - 0.5) / 4, offset.z + (caster.getRandom().nextFloat() - 0.5) / 4,
                    back.x * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.y * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2, back.z * 0.75 + (caster.getRandom().nextFloat() - 0.5) / 2);
        }
        for (int i = 0; i < 60; i++) {
            Vec3 off = new Vec3(3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5));
            Vec3 randomBody = caster.position().add(0, caster.getBbHeight() / 2, 0).add(off);
            level.addParticle(new RuneParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, level.random.nextInt(5, 20), 0.9F),
                    randomBody.x, randomBody.y, randomBody.z, off.x / 10, off.y / 10, off.z / 10);
        }
        for (int i = 0; i < 60; i++) {
            Vec3 off = new Vec3(3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5), 3 * (caster.getRandom().nextFloat() - 0.5));
            Vec3 randomBody = caster.position().add(0, caster.getBbHeight() / 2, 0).add(off);
            level.addParticle(new SquareParticleEffect(new Vector3f(1.0F, 0.0F, 0.7F), new Vector3f(1.0F, 0.0F, 0.3F), 1F, 1, 0, 15, 1.0F),
                    randomBody.x, randomBody.y, randomBody.z, off.x / 10, off.y / 10, off.z / 10);
        }
    }
}
