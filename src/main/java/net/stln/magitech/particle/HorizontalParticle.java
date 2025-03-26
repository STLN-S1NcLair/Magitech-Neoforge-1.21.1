package net.stln.magitech.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import org.joml.Quaternionf;

public class HorizontalParticle extends AbstractCustomizableParticle {

    protected HorizontalParticle(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
    }

    protected HorizontalParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.set(camera.getPosition().y > this.y ? -0.25F : 0.25F, 0.0F, 0.0F, 0.25F);
        this.renderRotatedQuad(vertexConsumer, camera, quaternionf, tickDelta);
    }
}
