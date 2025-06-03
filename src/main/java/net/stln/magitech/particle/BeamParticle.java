package net.stln.magitech.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BeamParticle extends GlowingParticle {

    private final SpriteSet spriteProvider;
    private final Vector3f startColor;
    private final Vector3f endColor;
    private final Vector3f endPos;

    public BeamParticle(ClientLevel clientWorld, double x, double y, double z, double vx, double vy, double vz,
                        BeamParticleEffect parameters, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = 5;
        this.alpha = 1.0F;
        this.scale = 1F * parameters.getScale();
        this.gravity = 0.0F;
        this.friction = 1.0F;
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
        this.startColor = parameters.getFromColor();
        this.endColor = parameters.getToColor();
        this.endPos = parameters.getToPos();
        this.twinkle = parameters.getTwinkle();
        this.rotSpeed = parameters.getRotSpeed();
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.updateColor(tickDelta);

        RandomSource random = Minecraft.getInstance().level.getRandom();
        float width = 0.2F * this.scale;
        if (this.age >= this.lifetime * 0.8F) {
            this.alpha = (this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F;
            width = ((this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F) * 0.2F * this.scale;
        }
        if (this.twinkle > 1) {
            float multiplier = Math.max(((float) this.age % this.twinkle) / (this.twinkle - 1), 0.1F);
            this.rCol *= multiplier;
            this.gCol *= multiplier;
            this.bCol *= multiplier;
        }
        drawBeam(vertexConsumer, camera, this.getPos(), new Vec3(this.endPos), width, 1, 1, 1, 1);
    }

    public void drawBeam(VertexConsumer vc, Camera camera, Vec3 start, Vec3 end, float width,
                         int r, int g, int b, int a) {
        Vec3 cameraPos = camera.getPosition();
        Vec3 from = start.subtract(cameraPos);
        Vec3 to = end.subtract(cameraPos);

        // ビームの方向ベクトル（normalized）
        Vector3f dir = to.subtract(from).normalize().toVector3f();

        // カメラの向きベクトル（ビュー方向）
        Vector3f view = new Vector3f(0, 1, 0).rotate(new Quaternionf(dir.x, dir.y, dir.z, this.roll)); // Forge/NeoForge 1.20+ の標準API
        // ↑もし getLookVector が無い場合は：
        // Vec3 view = new Vec3(camera.getLookVector().x, camera.getLookVector().y, camera.getLookVector().z);

        // dir × view = 「線に垂直でカメラに対して水平な方向（横方向）」
        Vec3 right = new Vec3(dir.x, dir.y, dir.z).cross(new Vec3(view.x, view.y, view.z)).normalize().scale((float) (width / 2.0));
        if (right.lengthSqr() < 1e-6) {
            right = new Vec3(dir.x, dir.y, dir.z).cross(new Vec3(0, 0, 1)).normalize().scale((float) (width / 2.0));
        }
        Vec3 up = new Vec3(dir.x, dir.y, dir.z).cross(right).normalize().scale((float) (width / 2.0));

        // 明るさ最大
        int light = 240;
        float f = this.getQuadSize(0);
        float f1 = this.getU0();
        float f2 = this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(0);

        // 四角形の4点（左右）
        Vector3f v1 = from.add(right).add(up).toVector3f();
        Vector3f v2 = from.subtract(right).add(up).toVector3f();
        Vector3f v3 = to.subtract(right).add(up).toVector3f();
        Vector3f v4 = to.add(right).add(up).toVector3f();

        this.renderVertex(vc, v1.x, v1.y, v1.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v2.x, v2.y, v2.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v3.x, v3.y, v3.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v4.x, v4.y, v4.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v5 = from.subtract(right).subtract(up).toVector3f();
        Vector3f v6 = from.add(right).subtract(up).toVector3f();
        Vector3f v7 = to.add(right).subtract(up).toVector3f();
        Vector3f v8 = to.subtract(right).subtract(up).toVector3f();

        this.renderVertex(vc, v5.x, v5.y, v5.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v6.x, v6.y, v6.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v7.x, v7.y, v7.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v8.x, v8.y, v8.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v9 = from.add(right).subtract(up).toVector3f();
        Vector3f v10 = from.add(right).add(up).toVector3f();
        Vector3f v11 = to.add(right).add(up).toVector3f();
        Vector3f v12 = to.add(right).subtract(up).toVector3f();

        this.renderVertex(vc, v9.x, v9.y, v9.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v10.x, v10.y, v10.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v11.x, v11.y, v11.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v12.x, v12.y, v12.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v13 = from.subtract(right).add(up).toVector3f();
        Vector3f v14 = from.subtract(right).subtract(up).toVector3f();
        Vector3f v15 = to.subtract(right).subtract(up).toVector3f();
        Vector3f v16 = to.subtract(right).add(up).toVector3f();

        this.renderVertex(vc, v13.x, v13.y, v13.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v14.x, v14.y, v14.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v15.x, v15.y, v15.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v16.x, v16.y, v16.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v17 = from.subtract(right).add(up).toVector3f();
        Vector3f v18 = from.add(right).add(up).toVector3f();
        Vector3f v19 = to.add(right).add(up).toVector3f();
        Vector3f v20 = to.subtract(right).add(up).toVector3f();

        this.renderVertex(vc, v17.x, v17.y, v17.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v18.x, v18.y, v18.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v19.x, v19.y, v19.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v20.x, v20.y, v20.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v21 = from.add(right).subtract(up).toVector3f();
        Vector3f v22 = from.subtract(right).subtract(up).toVector3f();
        Vector3f v23 = to.subtract(right).subtract(up).toVector3f();
        Vector3f v24 = to.add(right).subtract(up).toVector3f();

        this.renderVertex(vc, v21.x, v21.y, v21.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v22.x, v22.y, v22.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v23.x, v23.y, v23.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v24.x, v24.y, v24.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v25 = from.subtract(right).subtract(up).toVector3f();
        Vector3f v26 = from.subtract(right).add(up).toVector3f();
        Vector3f v27 = to.subtract(right).add(up).toVector3f();
        Vector3f v28 = to.subtract(right).subtract(up).toVector3f();

        this.renderVertex(vc, v25.x, v25.y, v25.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v26.x, v26.y, v26.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v27.x, v27.y, v27.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v28.x, v28.y, v28.z, -1.0F, -1.0F, f, f1, f4, i);

        // 四角形の4点（左右）
        Vector3f v29 = from.add(right).add(up).toVector3f();
        Vector3f v30 = from.add(right).subtract(up).toVector3f();
        Vector3f v31 = to.add(right).subtract(up).toVector3f();
        Vector3f v32 = to.add(right).add(up).toVector3f();

        this.renderVertex(vc, v29.x, v29.y, v29.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v30.x, v30.y, v30.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v31.x, v31.y, v31.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v32.x, v32.y, v32.z, -1.0F, -1.0F, f, f1, f4, i);
    }

    private void renderVertex(
            VertexConsumer buffer,
            float x,
            float y,
            float z,
            float xOffset,
            float yOffset,
            float quadSize,
            float u,
            float v,
            int packedLight
    ) {
        Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
                .setUv(u, v)
                .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
                .setLight(packedLight);
    }

    @Override
    public FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    private void updateColor(float tickDelta) {
        float f = ((float) this.age + tickDelta) / ((float) this.lifetime + 1.0F);
        Vector3f vector3f = new Vector3f(this.startColor).lerp(this.endColor, f);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.xd, this.yd, this.zd);
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd = this.yd - 0.04 * (double) this.gravity;
        }

        this.xd = this.xd * (double) this.friction;
        this.yd = this.yd * (double) this.friction;
        this.zd = this.zd * (double) this.friction;

        rotate();

        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    protected int getLightColor(float tint) {
        return 240;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<BeamParticleEffect> {
        private final SpriteSet spriteProvider;

        public Provider(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(BeamParticleEffect parameters, ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
            return new BeamParticle(world, x, y, z, xd, yd, zd, parameters, this.spriteProvider);
        }
    }
}
