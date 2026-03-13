package net.stln.magitech.effect.visual.spawner;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;

public class BeamParticles {

    public static void beamParticle(Level level, Vec3 start, Vec3 end, Element element, float scale) {
        BeamParticleEffect effect = new BeamParticleEffect(ColorHelper.getVectorColor(element.getPrimary()), ColorHelper.getVectorColor(element.getSecondary()).mul(0.3F), end.toVector3f(),
                scale, 1, level.getRandom().nextFloat(), level.getRandom().nextInt(5, 9), 0);
        BeamParticleEffect bloom = new BeamParticleEffect(ColorHelper.getVectorColor(element.getPrimary()).mul(0.3F), ColorHelper.getVectorColor(element.getSecondary()).mul(0.1F), end.toVector3f(),
                scale * 2F, 1, level.getRandom().nextFloat(), level.getRandom().nextInt(9, 13), 0);
        BeamParticleEffect bloom2 = new BeamParticleEffect(ColorHelper.getVectorColor(element.getSecondary()).mul(0.2F), ColorHelper.getVectorColor(element.getDark()).mul(0.1F), end.toVector3f(),
                scale * 3F, 1, level.getRandom().nextFloat(), level.getRandom().nextInt(13, 17), 0);
        level.addParticle(effect, start.x, start.y, start.z, 0, 0, 0);
        level.addParticle(bloom, start.x, start.y, start.z, 0, 0, 0);
        level.addParticle(bloom2, start.x, start.y, start.z, 0, 0, 0);
    }

}
