package net.stln.magitech.effect.visual.preset;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;

public class YRotOnlyParticleBehavior implements LodestoneParticleBehavior {

    private static final VFXBuilders.WorldVFXBuilder BUILDER = VFXBuilders.createWorld().setFormat(DefaultVertexFormat.PARTICLE);

    private static final Vec3 UP = new Vec3(0, 1, 0);

    public YRotOnlyParticleBehavior() {
    }

    @Override
    public void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (Mth.lerp(partialTicks, particle.getXOld(), particle.getX()) - vec3.x());
        float y = (float) (Mth.lerp(partialTicks, particle.getYOld(), particle.getY()) - vec3.y());
        float z = (float) (Mth.lerp(partialTicks, particle.getZOld(), particle.getZ()) - vec3.z());
        Vec3 pos = new Vec3(x, y, z);
        float quadSize = particle.getQuadSize(partialTicks);
        BUILDER.setVertexConsumer(consumer)
                .setUV(particle.getU0(), particle.getV0(), particle.getU1(), particle.getV1())
                .setColor(particle.getRed(), particle.getGreen(), particle.getBlue())
                .setAlpha(particle.getAlpha())
                .renderBeam(null, getStartPos(pos, quadSize), getEndPos(pos, quadSize), quadSize, Vec3.ZERO);
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        return UP;
    }

    public Vec3 getStartPos(Vec3 pos, float scale) {
        return pos.subtract(UP.scale(0.5 * scale));
    }

    public Vec3 getEndPos(Vec3 pos, float scale) {
        return pos.add(UP.scale(0.5 * scale));
    }
}
