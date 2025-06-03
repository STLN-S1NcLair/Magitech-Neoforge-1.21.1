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
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ZapParticle extends GlowingParticle {

    private final SpriteSet spriteProvider;
    private final Vector3f startColor;
    private final Vector3f endColor;
    private final Vector3f endPos;
    private List<Vec3> segmentList = new ArrayList<>();

    public ZapParticle(ClientLevel clientWorld, double x, double y, double z, double vx, double vy, double vz,
                       ZapParticleEffect parameters, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.lifetime = 2 + clientWorld.random.nextInt(0, 3);
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

        if (this.age >= this.lifetime * 0.8F) {
            this.alpha = (this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F;
        }
        if (this.twinkle > 1) {
            float multiplier = Math.max(((float) this.age % this.twinkle) / (this.twinkle - 1), 0.1F);
            this.rCol *= multiplier;
            this.gCol *= multiplier;
            this.bCol *= multiplier;
        }
        for (int i = 0; i < segmentList.size() - 1; i++) {
            Vec3 nextFrom = segmentList.get(i);
            Vec3 nextTo = segmentList.get(i + 1);
            drawBeam(vertexConsumer, camera, nextFrom, nextTo, 0.2F, 1, 1, 1, 1);
        }
    }

    public void drawBeam(VertexConsumer vc, Camera camera, Vec3 start, Vec3 end, float width,
                         int r, int g, int b, int a) {
        Vec3 cameraPos = camera.getPosition();
        Vec3 from = start.subtract(cameraPos);
        Vec3 to = end.subtract(cameraPos);

        // ビームの方向ベクトル（normalized）
        Vector3f dir = to.subtract(from).normalize().toVector3f();

        // カメラの向きベクトル（ビュー方向）
        Vector3f view = start.subtract(camera.getPosition()).toVector3f(); // Forge/NeoForge 1.20+ の標準API
        // ↑もし getLookVector が無い場合は：
        // Vec3 view = new Vec3(camera.getLookVector().x, camera.getLookVector().y, camera.getLookVector().z);

        // dir × view = 「線に垂直でカメラに対して水平な方向（横方向）」
        Vec3 right = new Vec3(dir.x, dir.y, dir.z).cross(new Vec3(view.x, view.y, view.z)).normalize().scale((float) (width / 2.0));

        // 四角形の4点（左右）
        Vector3f v1 = from.add(right).toVector3f();
        Vector3f v2 = from.subtract(right).toVector3f();
        Vector3f v3 = to.subtract(right).toVector3f();
        Vector3f v4 = to.add(right).toVector3f();

        // 明るさ最大
        int light = 240;
        float f = this.getQuadSize(0);
        float f1 = this.getU0();
        float f2 = this.getU1();
        float f3 = this.getV0();
        float f4 = this.getV1();
        int i = this.getLightColor(0);
        this.renderVertex(vc, v1.x, v1.y, v1.z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(vc, v2.x, v2.y, v2.z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(vc, v3.x, v3.y, v3.z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(vc, v4.x, v4.y, v4.z, -1.0F, -1.0F, f, f1, f4, i);
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

        segmentList.clear();
        Vec3 toPos = new Vec3(this.endPos);
        double distance = this.getPos().distanceTo(toPos);
        int segment = (int) Math.max(distance / Math.sqrt(distance), 3);
        segmentList.add(this.getPos());
        for (int i = 0; i < segment; i++) {
            Vec3 nextTo = this.getPos().scale((double) (segment - (i + 1)) / segment);
            nextTo = nextTo.add(toPos.scale((double) (i + 1) / segment));
            if (i + 1 < segment) {
                nextTo = nextTo.add((random.nextFloat() - 0.5) / 6 * distance, (random.nextFloat() - 0.5) / 6 * distance, (random.nextFloat() - 0.5) / 6 * distance);
            }
            segmentList.add(nextTo);
        }

        rotate();

        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    protected int getLightColor(float tint) {
        return 240;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<ZapParticleEffect> {
        private final SpriteSet spriteProvider;

        public Provider(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(ZapParticleEffect parameters, ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
            return new ZapParticle(world, x, y, z, xd, yd, zd, parameters, this.spriteProvider);
        }
    }
}
