package net.stln.magitech.effect.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.handlers.LodestoneRenderHandler;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

/**
 * Lodestoneを使ってテクスチャ付きのワールド空間長方形を描画するユーティリティです。
 * Utilities for rendering textured world-space rectangles with Lodestone.
 *
 * <p>既定の描画形式は、Lodestoneの加算合成テクスチャ付きQUADです。
 * 通常のワールドVFX長方形と同じ位置・色・テクスチャ・ライトマップ形式を使い、
 * アルファブレンドではなくテクスチャを発光させます。</p>
 * <p>The default render type is Lodestone's additive textured quad.
 * It uses the same position, color, texture, and lightmap format as a usual
 * world VFX rectangle, while making the texture glow instead of alpha-blending it.</p>
 */
@OnlyIn(Dist.CLIENT)
public final class AdditiveRectangleRenderer {

    private static final Vec3 X_AXIS = new Vec3(1.0D, 0.0D, 0.0D);
    private static final Vec3 Y_AXIS = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 Z_AXIS = new Vec3(0.0D, 0.0D, 1.0D);

    private AdditiveRectangleRenderer() {
    }

    /**
     * ワールド座標にカメラ正面の長方形を描画します。
     * Renders a camera-facing rectangle at a world position.
     *
     * <p>一般的なパーティクル形式のビルボードです。指定する幅と高さは半径ではなく、
     * ワールド単位での全長です。</p>
     * <p>This is the usual particle-style billboard. The supplied width and
     * height are full world-space dimensions, not half-extents.</p>
     */
    public static void renderBillboard(
            PoseStack poseStack,
            Camera camera,
            Vec3 position,
            float width,
            float height,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(position, "position");

        poseStack.pushPose();
        Vec3 cameraPosition = camera.getPosition();
        poseStack.translate(
                position.x - cameraPosition.x,
                position.y - cameraPosition.y,
                position.z - cameraPosition.z
        );
        poseStack.mulPose(camera.rotation());
        renderLocal(poseStack, width, height, texture, color, alpha);
        poseStack.popPose();
    }

    /**
     * 指定した右方向軸と上方向軸を使って、ワールド空間に長方形を描画します。
     * Renders a rectangle in world space using the supplied right and up axes.
     *
     * <p>軸は内部で正規化されるため、軸ベクトルの長さは長方形のサイズに影響しません。</p>
     * <p>The axes are normalized internally, so their lengths do not affect the
     * requested rectangle size.</p>
     */
    public static void renderWorldPlane(
            PoseStack poseStack,
            Camera camera,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(center, "center");

        Vector3f[] vertices = createVertices(center.subtract(camera.getPosition()), right, up, width, height);
        renderLocal(poseStack, vertices, texture, color, alpha);
    }

    /**
     * ワールド空間の長方形を両面から描画します。
     * Renders a world-space rectangle from both sides.
     *
     * <p>カメラがフィールドの内側にも外側にも存在しうるため、フィールド境界に適しています。</p>
     * <p>This is useful for field boundaries because the camera may be either
     * inside or outside the field.</p>
     */
    public static void renderWorldPlaneDoubleSided(
            PoseStack poseStack,
            Camera camera,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(center, "center");

        Vector3f[] vertices = createVertices(center.subtract(camera.getPosition()), right, up, width, height);
        renderLocalDoubleSided(poseStack, vertices, texture, color, alpha);
    }

    /**
     * ワールド空間の長方形を両面から描画し、テクスチャをワールド単位で繰り返します。
     * Renders a world-space rectangle from both sides while repeating the texture per world unit.
     *
     * <p>幅と高さがブロック数に対応する場合、各ブロックへ1枚ずつテクスチャが貼られます。</p>
     * <p>When the width and height represent block counts, one texture tile is applied to each block.</p>
     */
    public static void renderWorldPlaneDoubleSidedTiledGradient(
            PoseStack poseStack,
            Camera camera,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            RenderTypeToken texture,
            Color primary,
            Color secondary,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(center, "center");

        Vector3f[] vertices = createVertices(center.subtract(camera.getPosition()), right, up, width, height);
        renderLocalDoubleSidedTiledGradient(poseStack, vertices, texture, primary, secondary, alpha, width, height);
    }

    /**
     * ワールド空間の長方形を両面から描画し、アトラステクスチャの指定タイルを貼り付けます。
     * Renders a world-space rectangle from both sides using one tile from an atlas texture.
     *
     * <p>テクスチャは指定したアトラス分割数に従って正規化されます。
     * 例えば8分割のアトラスでタイル番号27を指定すると、4列目・4行目のタイルを使用します。</p>
     * <p>Texture coordinates are normalized according to the atlas grid.
     * For example, tile 27 in an 8-by-8 atlas selects column 4 and row 4.</p>
     *
     * @param tileIndex アトラス内のタイル番号 / tile index in the atlas
     * @param atlasSize アトラスの1辺あたりのタイル数 / number of tiles per atlas side
     */
    public static void renderWorldPlaneDoubleSidedTile(
            PoseStack poseStack,
            Camera camera,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            RenderTypeToken texture,
            int tileIndex,
            int atlasSize,
            Color primary,
            Color secondary,
            float alpha
    ) {
        renderWorldPlaneDoubleSidedTile(
                poseStack,
                camera,
                center,
                right,
                up,
                width,
                height,
                texture,
                tileIndex,
                atlasSize,
                primary,
                secondary,
                alpha,
                LodestoneRenderTypes.ADDITIVE_TEXTURE
        );
    }

    /**
     * ワールド空間の複数の長方形を一つの両面バッファへまとめて描画します。
     * Renders multiple world-space rectangles together in one double-sided buffer.
     *
     * <p>各長方形は異なるアトラスタイルを指定できますが、同じ描画形式を共有します。</p>
     * <p>Each rectangle may select a different atlas tile while sharing one render type.</p>
     *
     * @param planes 描画するワールド長方形 / world-space rectangles to render
     * @param atlasTextureSize アトラス画像の1辺のピクセル数 / pixel size of one atlas side
     * @param renderTypeProvider 使用する描画形式のプロバイダー / provider for the render type to use
     */
    public static void renderWorldPlanesDoubleSidedTileBatch(
            PoseStack poseStack,
            Camera camera,
            List<WorldPlane> planes,
            RenderTypeToken texture,
            Color color,
            float alpha,
            RenderTypeProvider renderTypeProvider,
            int atlasTextureSize
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(planes, "planes");
        Objects.requireNonNull(texture, "texture");
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(renderTypeProvider, "renderTypeProvider");
        if (planes.isEmpty() || alpha <= 0.0F) {
            return;
        }
        if (atlasTextureSize <= 0) {
            throw new IllegalArgumentException("Atlas texture size must be positive");
        }

        VFXBuilders.WorldVFXBuilder builder = additiveBuilder(renderTypeProvider, texture, color, alpha);
        var consumer = builder.getVertexConsumer();
        var supplier = builder.getSupplier();
        for (WorldPlane plane : planes) {
            Objects.requireNonNull(plane, "plane");
            if (plane.width() <= 0.0F || plane.height() <= 0.0F) {
                continue;
            }
            if (plane.atlasSize() <= 0 || plane.tileIndex() < 0
                    || plane.tileIndex() >= plane.atlasSize() * plane.atlasSize()) {
                throw new IllegalArgumentException("Plane tile is outside the atlas");
            }

            int tileX = plane.tileIndex() % plane.atlasSize();
            int tileY = plane.tileIndex() / plane.atlasSize();
            float tileWidth = 1.0F / plane.atlasSize();
            float texel = 1.0F / atlasTextureSize;
            float u0 = tileX * tileWidth + texel * 0.5F;
            float v0 = tileY * tileWidth + texel * 0.5F;
            float u1 = (tileX + 1) * tileWidth - texel * 0.5F;
            float v1 = (tileY + 1) * tileWidth - texel * 0.5F;
            Vector3f[] worldVertices = createVertices(
                    plane.center(),
                    plane.right(),
                    plane.up(),
                    plane.width(),
                    plane.height()
            );
            Vector3f[] vertices = new Vector3f[worldVertices.length];
            int[] distortionCoordinates = new int[worldVertices.length];
            Vec3 cameraPosition = camera.getPosition();
            for (int index = 0; index < worldVertices.length; index++) {
                Vector3f worldVertex = worldVertices[index];
                vertices[index] = new Vector3f(worldVertex).sub(
                        (float) cameraPosition.x,
                        (float) cameraPosition.y,
                        (float) cameraPosition.z
                );
                distortionCoordinates[index] = encodeDistortionCoordinate(worldVertex);
            }

            placeAtlasQuad(
                    poseStack,
                    supplier,
                    consumer,
                    builder,
                    vertices,
                    distortionCoordinates,
                    u0,
                    v0,
                    u1,
                    v1
            );
            placeAtlasQuad(
                    poseStack,
                    supplier,
                    consumer,
                    builder,
                    new Vector3f[]{vertices[3], vertices[2], vertices[1], vertices[0]},
                    new int[]{distortionCoordinates[3], distortionCoordinates[2], distortionCoordinates[1], distortionCoordinates[0]},
                    u0,
                    v1,
                    u1,
                    v0
            );
        }
    }

    /**
     * ワールド空間の長方形を両面から描画し、指定した描画形式でアトラステクスチャのタイルを貼り付けます。
     * Renders a world-space rectangle from both sides using one atlas tile and the supplied render type.
     *
     * @param renderTypeProvider 使用する描画形式のプロバイダー / provider for the render type to use
     */
    public static void renderWorldPlaneDoubleSidedTile(
            PoseStack poseStack,
            Camera camera,
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            RenderTypeToken texture,
            int tileIndex,
            int atlasSize,
            Color primary,
            Color secondary,
            float alpha,
            RenderTypeProvider renderTypeProvider
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(center, "center");
        Objects.requireNonNull(renderTypeProvider, "renderTypeProvider");
        if (atlasSize <= 0) {
            throw new IllegalArgumentException("Atlas size must be positive");
        }
        if (tileIndex < 0 || tileIndex >= atlasSize * atlasSize) {
            throw new IllegalArgumentException("Tile index is outside the atlas");
        }

        int tileX = tileIndex % atlasSize;
        int tileY = tileIndex / atlasSize;
        float tileWidth = 1.0F / atlasSize;
        float u0 = tileX * tileWidth;
        float v0 = tileY * tileWidth;
        float u1 = u0 + tileWidth;
        float v1 = v0 + tileWidth;

        Vector3f[] vertices = createVertices(center.subtract(camera.getPosition()), right, up, width, height);
        renderLocalDoubleSidedAtlasTile(
                poseStack,
                vertices,
                renderTypeProvider,
                texture,
                primary,
                secondary,
                alpha,
                u0,
                v0,
                u1,
                v1
        );
    }

    /**
     * 直方体の12辺を発光する細い長方形として描画します。
     * Renders the twelve edges of a cuboid as glowing thin rectangles.
     *
     * <p>min/max はブロック境界などのワールド座標で指定します。辺の太さはワールド単位です。</p>
     * <p>The min/max values are world-space bounds such as block boundaries.
     * The edge thickness is measured in world units.</p>
     */
    public static void renderCuboidBoundary(
            PoseStack poseStack,
            Camera camera,
            Vec3 min,
            Vec3 max,
            float edgeThickness,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(camera, "camera");
        Objects.requireNonNull(min, "min");
        Objects.requireNonNull(max, "max");

        Vec3 lower = new Vec3(
                Math.min(min.x, max.x),
                Math.min(min.y, max.y),
                Math.min(min.z, max.z)
        );
        Vec3 upper = new Vec3(
                Math.max(min.x, max.x),
                Math.max(min.y, max.y),
                Math.max(min.z, max.z)
        );
        float thickness = Math.max(edgeThickness, 0.001F);

        // X-axis edges / X軸方向の辺
        for (double y : new double[]{lower.y, upper.y}) {
            for (double z : new double[]{lower.z, upper.z}) {
                renderCuboidEdge(poseStack, camera,
                        new Vec3(lower.x, y, z), new Vec3(upper.x, y, z),
                        X_AXIS, Y_AXIS, thickness, texture, color, alpha);
            }
        }

        // Y-axis edges / Y軸方向の辺
        for (double x : new double[]{lower.x, upper.x}) {
            for (double z : new double[]{lower.z, upper.z}) {
                renderCuboidEdge(poseStack, camera,
                        new Vec3(x, lower.y, z), new Vec3(x, upper.y, z),
                        Y_AXIS, X_AXIS, thickness, texture, color, alpha);
            }
        }

        // Z-axis edges / Z軸方向の辺
        for (double x : new double[]{lower.x, upper.x}) {
            for (double y : new double[]{lower.y, upper.y}) {
                renderCuboidEdge(poseStack, camera,
                        new Vec3(x, y, lower.z), new Vec3(x, y, upper.z),
                        Z_AXIS, X_AXIS, thickness, texture, color, alpha);
            }
        }
    }

    /**
     * ローカル空間の頂点から長方形を描画します。
     * Renders a rectangle from local-space vertices.
     *
     * <p>頂点は外周順、つまり左下・右下・右上・左上の順で指定してください。</p>
     * <p>The vertices must be supplied in perimeter order:
     * bottom-left, bottom-right, top-right, top-left.</p>
     */
    public static void renderLocal(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(poseStack, "poseStack");
        Objects.requireNonNull(vertices, "vertices");
        if (vertices.length != 4) {
            throw new IllegalArgumentException("A rectangle requires exactly four vertices");
        }

        additiveBuilder(texture, color, alpha).renderQuad(poseStack, vertices);
    }

    private static void renderLocalTiledGradient(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeToken texture,
            Color primary,
            Color secondary,
            float alpha,
            float textureWidth,
            float textureHeight
    ) {
        VFXBuilders.WorldVFXBuilder builder = additiveBuilder(texture, primary, alpha)
                .setUV(0.0F, 0.0F, textureWidth, textureHeight);
        var consumer = builder.getVertexConsumer();
        var supplier = builder.getSupplier();

        builder.setColor(primary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[0].x(), vertices[0].y(), vertices[0].z(), 0.0F, textureHeight);
        builder.setColor(secondary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[1].x(), vertices[1].y(), vertices[1].z(), textureWidth, textureHeight);
        builder.setColor(secondary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[2].x(), vertices[2].y(), vertices[2].z(), textureWidth, 0.0F);
        builder.setColor(primary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[3].x(), vertices[3].y(), vertices[3].z(), 0.0F, 0.0F);
    }

    private static void renderLocalDoubleSidedTiledGradient(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeToken texture,
            Color primary,
            Color secondary,
            float alpha,
            float textureWidth,
            float textureHeight
    ) {
        Objects.requireNonNull(vertices, "vertices");
        Objects.requireNonNull(primary, "primary");
        Objects.requireNonNull(secondary, "secondary");

        renderLocalTiledGradient(poseStack, vertices, texture, primary, secondary, alpha, textureWidth, textureHeight);
        renderLocalTiledGradient(
                poseStack,
                new Vector3f[]{vertices[3], vertices[2], vertices[1], vertices[0]},
                texture,
                primary,
                secondary,
                alpha,
                textureWidth,
                textureHeight
        );
    }

    /**
     * ローカル空間の長方形へアトラスの指定タイルを両面から描画します。
     * Renders one atlas tile on a local-space rectangle from both sides.
     */
    private static void renderLocalDoubleSidedAtlasTile(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeProvider renderTypeProvider,
            RenderTypeToken texture,
            Color primary,
            Color secondary,
            float alpha,
            float u0,
            float v0,
            float u1,
            float v1
    ) {
        renderLocalAtlasTile(
                poseStack,
                vertices,
                renderTypeProvider,
                texture,
                primary,
                secondary,
                alpha,
                u0,
                v0,
                u1,
                v1
        );
        // 裏面は頂点順だけでなくVの割り当ても反転し、ワールド上方向の見た目を維持します。
        // The back face reverses the V assignment as well as the winding, preserving the world-space up direction.
        renderLocalAtlasTile(
                poseStack,
                new Vector3f[]{vertices[3], vertices[2], vertices[1], vertices[0]},
                renderTypeProvider,
                texture,
                primary,
                secondary,
                alpha,
                u0,
                v1,
                u1,
                v0
        );
    }

    /**
     * ローカル空間の長方形へアトラスの指定タイルを描画します。
     * Renders one atlas tile on a local-space rectangle.
     */
    private static void renderLocalAtlasTile(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeProvider renderTypeProvider,
            RenderTypeToken texture,
            Color primary,
            Color secondary,
            float alpha,
            float u0,
            float v0,
            float u1,
            float v1
    ) {
        VFXBuilders.WorldVFXBuilder builder = additiveBuilder(renderTypeProvider, texture, primary, alpha)
                .setUV(u0, v0, u1, v1);
        var consumer = builder.getVertexConsumer();
        var supplier = builder.getSupplier();

        builder.setColor(primary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[0].x(), vertices[0].y(), vertices[0].z(), u0, v1);
        builder.setColor(secondary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[1].x(), vertices[1].y(), vertices[1].z(), u1, v1);
        builder.setColor(secondary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[2].x(), vertices[2].y(), vertices[2].z(), u1, v0);
        builder.setColor(primary);
        supplier.placeVertex(consumer, poseStack, builder, vertices[3].x(), vertices[3].y(), vertices[3].z(), u0, v0);
    }

    private static void placeAtlasQuad(
            PoseStack poseStack,
            VFXBuilders.VertexConsumerActor supplier,
            VertexConsumer consumer,
            VFXBuilders.WorldVFXBuilder builder,
            Vector3f[] vertices,
            int[] distortionCoordinates,
            float u0,
            float v0,
            float u1,
            float v1
    ) {
        placeAtlasVertex(poseStack, supplier, consumer, builder, vertices[0], distortionCoordinates[0], u0, v1);
        placeAtlasVertex(poseStack, supplier, consumer, builder, vertices[1], distortionCoordinates[1], u1, v1);
        placeAtlasVertex(poseStack, supplier, consumer, builder, vertices[2], distortionCoordinates[2], u1, v0);
        placeAtlasVertex(poseStack, supplier, consumer, builder, vertices[3], distortionCoordinates[3], u0, v0);
    }

    private static void placeAtlasVertex(
            PoseStack poseStack,
            VFXBuilders.VertexConsumerActor supplier,
            VertexConsumer consumer,
            VFXBuilders.WorldVFXBuilder builder,
            Vector3f vertex,
            int distortionCoordinate,
            float u,
            float v
    ) {
        // UV2はライト値の代わりにカメラに依存しない歪み座標を格納します。
        // UV2 stores camera-independent distortion coordinates instead of light values.
        builder.setLight(distortionCoordinate);
        supplier.placeVertex(consumer, poseStack, builder, vertex.x(), vertex.y(), vertex.z(), u, v);
    }

    private static int encodeDistortionCoordinate(Vector3f position) {
        int x = encodeDistortionAxis(position.x() + position.y() * 0.37F + position.z() * 0.19F);
        int y = encodeDistortionAxis(position.x() * 0.13F + position.y() * 0.53F + position.z() * 0.71F);
        return (y << 16) | x;
    }

    private static int encodeDistortionAxis(float coordinate) {
        return Math.floorMod(Math.round(coordinate * 8.0F), 1 << 15);
    }

    /**
     * ローカル空間の長方形を、両方の頂点巻き方向で描画します。
     * Renders a local-space rectangle from both winding directions.
     */
    public static void renderLocalDoubleSided(
            PoseStack poseStack,
            Vector3f[] vertices,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        Objects.requireNonNull(vertices, "vertices");
        renderLocal(poseStack, vertices, texture, color, alpha);
        renderLocal(poseStack, new Vector3f[]{vertices[3], vertices[2], vertices[1], vertices[0]}, texture, color, alpha);
    }

    /**
     * 長方形の中心へ移動済みのPoseStackを使い、カメラ正面の長方形を描画します。
     * Renders a camera-facing rectangle using a pose stack that is already
     * positioned at the rectangle center.
     */
    public static void renderLocal(
            PoseStack poseStack,
            float width,
            float height,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        additiveBuilder(texture, color, alpha).renderQuad(poseStack, width * 0.5F, height * 0.5F);
    }

    private static VFXBuilders.WorldVFXBuilder additiveBuilder(RenderTypeToken texture, Color color, float alpha) {
        return additiveBuilder(LodestoneRenderTypes.ADDITIVE_TEXTURE, texture, color, alpha);
    }

    private static VFXBuilders.WorldVFXBuilder additiveBuilder(
            RenderTypeProvider renderTypeProvider,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        ensureLodestoneFogShape();
        return new VFXBuilders.WorldVFXBuilder()
                .setRenderType(renderTypeProvider.apply(Objects.requireNonNull(texture, "texture")))
                .setColor(Objects.requireNonNull(color, "color"))
                .setAlpha(alpha);
    }

    /**
     * Lodestoneのフォグ形状キャッシュが未設定の場合に安全な既定値を設定します。
     * Supplies a safe default when Lodestone's cached fog shape has not been set.
     *
     * <p>Sableなどがフォグイベントを変更すると、Lodestone 1.8.2の描画直前に
     * nullが再適用され、シェーダーのデフォルトuniform更新でクラッシュすることがあります。</p>
     * <p>When mods such as Sable alter fog events, Lodestone 1.8.2 can reapply a null
     * value immediately before drawing, which crashes shader default-uniform updates.</p>
     */
    private static void ensureLodestoneFogShape() {
        if (LodestoneRenderHandler.FOG_SHAPE == null) {
            LodestoneRenderHandler.FOG_SHAPE = FogShape.SPHERE;
        }
    }

    private static void renderCuboidEdge(
            PoseStack poseStack,
            Camera camera,
            Vec3 start,
            Vec3 end,
            Vec3 right,
            Vec3 up,
            float thickness,
            RenderTypeToken texture,
            Color color,
            float alpha
    ) {
        float length = (float) start.distanceTo(end);
        if (length <= 0.0F) {
            return;
        }

        renderWorldPlaneDoubleSided(
                poseStack,
                camera,
                start.add(end).scale(0.5),
                right,
                up,
                length,
                thickness,
                texture,
                color,
                alpha
        );
    }

    /**
     * 一括描画するワールド空間長方形の定義です。
     * Defines one world-space rectangle for batched rendering.
     */
    public record WorldPlane(
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            int tileIndex,
            int atlasSize
    ) {
    }

    private static Vector3f[] createVertices(Vec3 center, Vec3 right, Vec3 up, float width, float height) {
        Objects.requireNonNull(right, "right");
        Objects.requireNonNull(up, "up");

        Vec3 halfRight = right.normalize().scale(width * 0.5F);
        Vec3 halfUp = up.normalize().scale(height * 0.5F);

        return new Vector3f[]{
                center.subtract(halfRight).subtract(halfUp).toVector3f(),
                center.add(halfRight).subtract(halfUp).toVector3f(),
                center.add(halfRight).add(halfUp).toVector3f(),
                center.subtract(halfRight).add(halfUp).toVector3f()
        };
    }
}
