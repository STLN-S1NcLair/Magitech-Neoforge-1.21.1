package net.stln.magitech.core.api.field_effect.vfx;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.core.api.field_effect.FieldEffectHelper;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceInstance;
import net.stln.magitech.core.api.field_effect.ColoredFieldEffectType;
import net.stln.magitech.core.api.field_effect.data.FieldEffectClientCache;
import net.stln.magitech.core.api.field_effect.data.RangeEntry;
import net.stln.magitech.effect.visual.AdditiveRectangleRenderer;
import net.stln.magitech.effect.visual.RenderTypeTokenInit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * クライアント側でフィールド効果の連結した境界面を描画します。
 * Renders connected field-effect boundary surfaces on the client.
 *
 * <p>同じ効果種類に属する範囲を合成し、内部面を除去して外側の面だけを描画します。
 * そのため、隣接する範囲の境界が途切れず、内部の重複面も発光しません。</p>
 * <p>Ranges with the same effect type are unioned, internal surfaces are removed,
 * and only exposed surfaces are rendered. Adjacent ranges therefore connect
 * continuously without brighter overlapping internal faces.</p>
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = Magitech.MOD_ID)
public final class FieldEffectBoundaryRenderer {

    private static final Vec3 X_AXIS = new Vec3(1.0D, 0.0D, 0.0D);
    private static final Vec3 Y_AXIS = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 Z_AXIS = new Vec3(0.0D, 0.0D, 1.0D);

    private static final float BASE_ALPHA = 0.05F;
    private static final float PULSE_ALPHA = 0.10F;
    private static final long PULSE_TIME = 100L;
    private static final double MAX_RENDER_DISTANCE = 256.0D;
    private static final double SURFACE_INSET = 0.0025D;
    private static final float TRANSITION_DURATION_TICKS = 40.0F;
    private static final int FIELD_EFFECT_ATLAS_SIZE = 8;

    private static long cachedRevision = Long.MIN_VALUE;
    private static Level cachedLevel;
    private static List<BoundaryFace> cachedFaces = List.of();
    private static List<BoundaryFace> outgoingFaces = List.of();
    private static long transitionStartTick = Long.MIN_VALUE;

    private FieldEffectBoundaryRenderer() {
    }

    /**
     * 同期済みフィールド範囲をワールド描画ステージで描画します。
     * Renders synchronized field ranges during the world render stage.
     *
     * @param event ワールド描画ステージイベント / world render stage event
     */
    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) {
            return;
        }

        FieldEffectClientCache cache = FieldEffectClientCache.getInstance();
        if (cachedLevel != level || cachedRevision != cache.getRevision()) {
            outgoingFaces = cachedLevel == level ? cachedFaces : List.of();
            cachedFaces = buildBoundaryFaces(level, cache.getEntriesSnapshot());
            cachedLevel = level;
            cachedRevision = cache.getRevision();
            transitionStartTick = level.getGameTime();
        }

        Camera camera = minecraft.gameRenderer.getMainCamera();
        PoseStack poseStack = event.getPoseStack();
        float partialTicks = event.getPartialTick().getRealtimeDeltaTicks();
        float pulse = 0.5F + 0.5F * Mth.sin(((level.getGameTime() % PULSE_TIME) + partialTicks) * Mth.TWO_PI / (float) PULSE_TIME);
        float alpha = BASE_ALPHA + PULSE_ALPHA * pulse;
        float transition = transitionStartTick == Long.MIN_VALUE
                ? 1.0F
                : Mth.clamp((level.getGameTime() - transitionStartTick + partialTicks) / TRANSITION_DURATION_TICKS, 0.0F, 1.0F);

        renderFaces(poseStack, camera, outgoingFaces, alpha * (1.0F - transition));
        renderFaces(poseStack, camera, cachedFaces, alpha * transition);
    }

    /**
     * 指定された面集合を距離判定付きで描画します。
     * Renders a set of faces with distance culling.
     *
     * @param alpha 描画全体のアルファ値 / overall render alpha
     */
    private static void renderFaces(
            PoseStack poseStack,
            Camera camera,
            List<BoundaryFace> faces,
            float alpha
    ) {
        if (alpha <= 0.0F) {
            return;
        }

        for (BoundaryFace face : faces) {
            if (!face.bounds().inflate(MAX_RENDER_DISTANCE).contains(camera.getPosition())) {
                continue;
            }

            AdditiveRectangleRenderer.renderWorldPlaneDoubleSidedTile(
                    poseStack,
                    camera,
                    face.center(),
                    face.right(),
                    face.up(),
                    face.width(),
                    face.height(),
                    RenderTypeTokenInit.FIELD_BOUNDARY,
                    face.textureIndex(),
                    FIELD_EFFECT_ATLAS_SIZE,
                    face.primary(),
                    face.secondary(),
                    alpha
            );
        }
    }

    /**
     * 重複範囲を実効影響ごとのセルへ分解し、露出面を長方形へ圧縮します。
     * Splits overlapping ranges into cells by their effective influences and compresses exposed surfaces into rectangles.
     *
     * <p>先に影響を合算することで、単一影響の範囲と複数影響の重複範囲を別領域として扱います。
     * これにより、加熱同士の重複は Scorching、冷却同士の重複は Freezing、
     * 加熱と冷却の重複は ThermalShock として独立して描画されます。</p>
     * <p>Influences are merged before rendering so single-influence areas and overlap areas remain separate.
     * Heated overlaps become Scorching, cold overlaps become Freezing, and heated/cold overlaps become
     * ThermalShock regions that are rendered independently.</p>
     */
    private static List<BoundaryFace> buildBoundaryFaces(Level level, List<RangeEntry> entries) {
        List<Cuboid> sourceCuboids = new ArrayList<>();
        TreeSet<Integer> xCoordinates = new TreeSet<>();
        for (RangeEntry entry : entries) {
            if (entry == null || entry.instance() == null || entry.instance().fieldInfluences() == null
                    || entry.instance().fieldInfluences().isEmpty()) {
                continue;
            }

            Cuboid cuboid = Cuboid.from(entry);
            sourceCuboids.add(cuboid);
            xCoordinates.add(cuboid.minX());
            xCoordinates.add(cuboid.maxX());
        }

        Map<BoundaryEffectKey, List<Cuboid>> groups = new LinkedHashMap<>();
        int[] xValues = xCoordinates.stream().mapToInt(Integer::intValue).toArray();
        for (int xIndex = 0; xIndex + 1 < xValues.length; xIndex++) {
            int minX = xValues[xIndex];
            int maxX = xValues[xIndex + 1];
            List<Cuboid> activeCuboids = new ArrayList<>();
            TreeSet<Integer> yCoordinates = new TreeSet<>();
            TreeSet<Integer> zCoordinates = new TreeSet<>();

            for (Cuboid cuboid : sourceCuboids) {
                if (cuboid.minX() >= maxX || cuboid.maxX() <= minX) {
                    continue;
                }

                activeCuboids.add(cuboid);
                yCoordinates.add(cuboid.minY());
                yCoordinates.add(cuboid.maxY());
                zCoordinates.add(cuboid.minZ());
                zCoordinates.add(cuboid.maxZ());
            }

            int[] yValues = yCoordinates.stream().mapToInt(Integer::intValue).toArray();
            int[] zValues = zCoordinates.stream().mapToInt(Integer::intValue).toArray();
            for (int yIndex = 0; yIndex + 1 < yValues.length; yIndex++) {
                int minY = yValues[yIndex];
                int maxY = yValues[yIndex + 1];
                for (int zIndex = 0; zIndex + 1 < zValues.length; zIndex++) {
                    int minZ = zValues[zIndex];
                    int maxZ = zValues[zIndex + 1];
                    FieldInfluenceInstance effectiveInstance = mergeInfluences(
                            activeCuboids,
                            minY,
                            minZ
                    );
                    if (effectiveInstance.fieldInfluences().isEmpty()) {
                        continue;
                    }

                    BoundaryEffectKey effectKey = resolveEffectKey(level, effectiveInstance);
                    groups.computeIfAbsent(effectKey, ignored -> new ArrayList<>()).add(
                            new Cuboid(minX, minY, minZ, maxX, maxY, maxZ, effectiveInstance)
                    );
                }
            }
        }

        List<BoundaryFace> faces = new ArrayList<>();
        for (Map.Entry<BoundaryEffectKey, List<Cuboid>> group : groups.entrySet()) {
            BoundaryColors colors = resolveBoundaryColors(group.getKey());
            addAxisSurfaces(group.getValue(), Axis.X, colors.primary(), colors.secondary(), faces);
            addAxisSurfaces(group.getValue(), Axis.Y, colors.primary(), colors.secondary(), faces);
            addAxisSurfaces(group.getValue(), Axis.Z, colors.primary(), colors.secondary(), faces);
        }
        return List.copyOf(faces);
    }

    /**
     * 指定セルを覆う範囲の影響を種類ごとに合算します。
     * Sums the influences of all ranges covering the specified compressed cell.
     *
     * @param cuboids X方向の区間ですでに絞り込まれた直方体 / cuboids already filtered by the X interval
     * @param minY セルの最小Y座標 / minimum Y coordinate of the cell
     * @param minZ セルの最小Z座標 / minimum Z coordinate of the cell
     * @return セルに実際に適用される影響 / effective influences applied to the cell
     */
    private static FieldInfluenceInstance mergeInfluences(List<Cuboid> cuboids, int minY, int minZ) {
        Map<FieldInfluenceType, Integer> merged = new HashMap<>();
        for (Cuboid cuboid : cuboids) {
            if (cuboid.minY() > minY || cuboid.maxY() <= minY
                    || cuboid.minZ() > minZ || cuboid.maxZ() <= minZ) {
                continue;
            }

            for (FieldInfluence influence : cuboid.instance().fieldInfluences()) {
                merged.merge(influence.type(), influence.intensity(), Integer::sum);
            }
        }

        if (merged.isEmpty()) {
            return new FieldInfluenceInstance(Set.of());
        }

        Set<FieldInfluence> influences = new java.util.HashSet<>();
        for (Map.Entry<FieldInfluenceType, Integer> entry : merged.entrySet()) {
            influences.add(new FieldInfluence(entry.getKey(), entry.getValue()));
        }
        return new FieldInfluenceInstance(influences);
    }

    /**
     * 色付き効果の色を境界面用に取得します。
     * Resolves the colors of a colored effect for its boundary surfaces.
     */
    private static BoundaryColors resolveBoundaryColors(BoundaryEffectKey key) {
        if (key.type() instanceof ColoredFieldEffectType colored) {
            return new BoundaryColors(colored.getPrimary(), colored.getSecondary());
        }
        return new BoundaryColors(Color.BLACK, Color.BLACK);
    }

    /**
     * 指定軸の各平面について、片側だけに存在する投影領域を抽出します。
     * Extracts projected regions that exist on only one side of each plane on an axis.
     */
    private static void addAxisSurfaces(
            List<Cuboid> cuboids,
            Axis axis,
            Color primary,
            Color secondary,
            List<BoundaryFace> output
    ) {
        TreeSet<Integer> planes = new TreeSet<>();
        for (Cuboid cuboid : cuboids) {
            planes.add(axis.min(cuboid));
            planes.add(axis.max(cuboid));
        }

        for (int plane : planes) {
            List<Projection> negativeSide = new ArrayList<>();
            List<Projection> positiveSide = new ArrayList<>();

            for (Cuboid cuboid : cuboids) {
                int min = axis.min(cuboid);
                int max = axis.max(cuboid);
                Projection projection = axis.project(cuboid);

                if (min < plane && max >= plane) {
                    negativeSide.add(projection);
                }
                if (min <= plane && max > plane) {
                    positiveSide.add(projection);
                }
            }

            addSurfaceDifference(axis, plane, negativeSide, positiveSide, false, primary, secondary, output);
            addSurfaceDifference(axis, plane, positiveSide, negativeSide, true, primary, secondary, output);
        }
    }

    /**
     * 2次元投影の差分を座標圧縮し、連続した長方形へまとめます。
     * Coordinate-compresses a two-dimensional projection difference into connected rectangles.
     *
     * <p>面は範囲の内側へわずかに移動し、ブロック面との深度競合を避けます。</p>
     * <p>Each face is moved slightly toward the covered range to avoid depth fighting with block faces.</p>
     */
    private static void addSurfaceDifference(
            Axis axis,
            int plane,
            List<Projection> source,
            List<Projection> covered,
            boolean sourceIsPositiveSide,
            Color primary,
            Color secondary,
            List<BoundaryFace> output
    ) {
        if (source.isEmpty()) {
            return;
        }

        TreeSet<Integer> uCoordinates = new TreeSet<>();
        TreeSet<Integer> vCoordinates = new TreeSet<>();
        addCoordinates(source, uCoordinates, vCoordinates);
        addCoordinates(covered, uCoordinates, vCoordinates);
        if (uCoordinates.size() < 2 || vCoordinates.size() < 2) {
            return;
        }

        int[] uValues = uCoordinates.stream().mapToInt(Integer::intValue).toArray();
        int[] vValues = vCoordinates.stream().mapToInt(Integer::intValue).toArray();
        Map<Integer, Integer> uIndices = indexCoordinates(uValues);
        Map<Integer, Integer> vIndices = indexCoordinates(vValues);
        boolean[][] sourceMask = new boolean[vValues.length - 1][uValues.length - 1];
        boolean[][] coveredMask = new boolean[vValues.length - 1][uValues.length - 1];

        markProjections(sourceMask, source, uIndices, vIndices);
        markProjections(coveredMask, covered, uIndices, vIndices);

        boolean[][] surfaceMask = new boolean[vValues.length - 1][uValues.length - 1];
        for (int v = 0; v < surfaceMask.length; v++) {
            for (int u = 0; u < surfaceMask[v].length; u++) {
                surfaceMask[v][u] = sourceMask[v][u] && !coveredMask[v][u];
            }
        }
        mergeSurfaceMask(axis, plane, surfaceMask, uValues, vValues, sourceIsPositiveSide, primary, secondary, output);
    }

    /**
     * 投影長方形の座標をマスクへ登録します。
     * Marks projected rectangles in a compressed occupancy mask.
     */
    private static void markProjections(
            boolean[][] mask,
            List<Projection> projections,
            Map<Integer, Integer> uIndices,
            Map<Integer, Integer> vIndices
    ) {
        for (Projection projection : projections) {
            int u0 = uIndices.get(projection.u0());
            int u1 = uIndices.get(projection.u1());
            int v0 = vIndices.get(projection.v0());
            int v1 = vIndices.get(projection.v1());
            for (int v = v0; v < v1; v++) {
                Arrays.fill(mask[v], u0, u1, true);
            }
        }
    }

    /**
     * マスクを走査し、各ブロックへ接続状態に対応するテクスチャタイルを割り当てます。
     * Scans a mask and assigns a connected-texture tile to each block.
     *
     * <p>アトラスがCreateのOMNIDIRECTIONAL形式なので、面全体を1枚へ引き伸ばさず、
     * 1ブロックごとに上下左右と四隅の接続状態を判定します。</p>
     * <p>The atlas follows Create's OMNIDIRECTIONAL layout, so the surface is not stretched into one texture;
     * each block evaluates its four sides and four corners independently.</p>
     */
    private static void mergeSurfaceMask(
            Axis axis,
            int plane,
            boolean[][] mask,
            int[] uValues,
            int[] vValues,
            boolean sourceIsPositiveSide,
            Color primary,
            Color secondary,
            List<BoundaryFace> output
    ) {
        int height = mask.length;
        int width = mask[0].length;

        for (int v = 0; v < height; v++) {
            for (int u = 0; u < width; u++) {
                if (!mask[v][u]) {
                    continue;
                }

                for (int worldV = vValues[v]; worldV < vValues[v + 1]; worldV++) {
                    for (int worldU = uValues[u]; worldU < uValues[u + 1]; worldU++) {
                        int textureIndex = connectedTextureIndex(
                                isSurfaceCell(mask, uValues, vValues, worldU, worldV + 1),
                                isSurfaceCell(mask, uValues, vValues, worldU, worldV - 1),
                                isSurfaceCell(mask, uValues, vValues, worldU - 1, worldV),
                                isSurfaceCell(mask, uValues, vValues, worldU + 1, worldV),
                                isSurfaceCell(mask, uValues, vValues, worldU - 1, worldV + 1),
                                isSurfaceCell(mask, uValues, vValues, worldU + 1, worldV + 1),
                                isSurfaceCell(mask, uValues, vValues, worldU - 1, worldV - 1),
                                isSurfaceCell(mask, uValues, vValues, worldU + 1, worldV - 1)
                        );
                        output.add(createBoundaryFace(
                                axis,
                                plane,
                                worldU,
                                worldV,
                                worldU + 1,
                                worldV + 1,
                                sourceIsPositiveSide,
                                textureIndex,
                                primary,
                                secondary
                        ));
                    }
                }
            }
        }
    }

    /**
     * ワールド座標の隣接セルが接続面に含まれるか判定します。
     * Determines whether a world-space neighboring cell belongs to the connected surface.
     */
    private static boolean isSurfaceCell(boolean[][] mask, int[] uValues, int[] vValues, int worldU, int worldV) {
        int uIndex = Arrays.binarySearch(uValues, worldU);
        if (uIndex < 0) {
            uIndex = -uIndex - 2;
        }

        int vIndex = Arrays.binarySearch(vValues, worldV);
        if (vIndex < 0) {
            vIndex = -vIndex - 2;
        }

        return uIndex >= 0 && uIndex < uValues.length - 1
                && vIndex >= 0 && vIndex < vValues.length - 1
                && mask[vIndex][uIndex];
    }

    /**
     * CreateのOMNIDIRECTIONALテクスチャ配置に対応するタイル番号を計算します。
     * Calculates the tile index used by Create's OMNIDIRECTIONAL texture layout.
     */
    private static int connectedTextureIndex(
            boolean up,
            boolean down,
            boolean left,
            boolean right,
            boolean topLeft,
            boolean topRight,
            boolean bottomLeft,
            boolean bottomRight
    ) {
        int verticalIndex = 0;
        int horizontalIndex = 0;
        int disconnectedSides = 0;

        if (up) {
            verticalIndex += 1;
        } else {
            disconnectedSides++;
        }
        if (down) {
            verticalIndex += 2;
        } else {
            disconnectedSides++;
        }
        if (left) {
            horizontalIndex += 1;
        } else {
            disconnectedSides++;
        }
        if (right) {
            horizontalIndex += 2;
        } else {
            disconnectedSides++;
        }

        if (disconnectedSides == 0) {
            if (topRight) {
                verticalIndex += 1;
            }
            if (topLeft) {
                verticalIndex += 2;
            }
            if (bottomRight) {
                horizontalIndex += 2;
            }
            if (bottomLeft) {
                horizontalIndex += 1;
            }
        } else if (disconnectedSides == 1) {
            if (!right && (topLeft || bottomLeft)) {
                horizontalIndex = 4;
                verticalIndex = -1 + (bottomLeft ? 1 : 0) + (topLeft ? 2 : 0);
            }
            if (!left && (topRight || bottomRight)) {
                horizontalIndex = 5;
                verticalIndex = -1 + (bottomRight ? 1 : 0) + (topRight ? 2 : 0);
            }
            if (!down && (topLeft || topRight)) {
                horizontalIndex = 6;
                verticalIndex = -1 + (topLeft ? 1 : 0) + (topRight ? 2 : 0);
            }
            if (!up && (bottomLeft || bottomRight)) {
                horizontalIndex = 7;
                verticalIndex = -1 + (bottomLeft ? 1 : 0) + (bottomRight ? 2 : 0);
            }
        } else if (disconnectedSides == 2
                && ((up && left && topLeft)
                || (down && left && bottomLeft)
                || (up && right && topRight)
                || (down && right && bottomRight))) {
            verticalIndex += 3;
        }

        return verticalIndex + FIELD_EFFECT_ATLAS_SIZE * horizontalIndex;
    }

    private static void addCoordinates(List<Projection> projections, TreeSet<Integer> uCoordinates, TreeSet<Integer> vCoordinates) {
        for (Projection projection : projections) {
            uCoordinates.add(projection.u0());
            uCoordinates.add(projection.u1());
            vCoordinates.add(projection.v0());
            vCoordinates.add(projection.v1());
        }
    }

    private static Map<Integer, Integer> indexCoordinates(int[] coordinates) {
        Map<Integer, Integer> indices = new HashMap<>();
        for (int index = 0; index < coordinates.length; index++) {
            indices.put(coordinates[index], index);
        }
        return indices;
    }

    private static BoundaryEffectKey resolveEffectKey(Level level, FieldInfluenceInstance instance) {
        FieldEffectType type = FieldEffectHelper.getFieldEffect(level, instance);
        return type == null ? new BoundaryEffectKey(null, instance) : new BoundaryEffectKey(type, null);
    }

    private static BoundaryFace createBoundaryFace(
            Axis axis,
            int plane,
            int u0,
            int v0,
            int u1,
            int v1,
            boolean sourceIsPositiveSide,
            int textureIndex,
            Color primary,
            Color secondary
    ) {
        double centerU = ((double) u0 + u1) * 0.5D;
        double centerV = ((double) v0 + v1) * 0.5D;
        double renderPlane = plane + (sourceIsPositiveSide ? SURFACE_INSET : -SURFACE_INSET);
        float width = u1 - u0;
        float height = v1 - v0;

        return switch (axis) {
            case X -> new BoundaryFace(
                    new Vec3(renderPlane, centerV, centerU),
                    Z_AXIS,
                    Y_AXIS,
                    width,
                    height,
                    new AABB(renderPlane, v0, u0, renderPlane, v1, u1),
                    textureIndex,
                    primary,
                    secondary
            );
            case Y -> new BoundaryFace(
                    new Vec3(centerU, renderPlane, centerV),
                    X_AXIS,
                    Z_AXIS,
                    width,
                    height,
                    new AABB(u0, renderPlane, v0, u1, renderPlane, v1),
                    textureIndex,
                    primary,
                    secondary
            );
            case Z -> new BoundaryFace(
                    new Vec3(centerU, centerV, renderPlane),
                    X_AXIS,
                    Y_AXIS,
                    width,
                    height,
                    new AABB(u0, v0, renderPlane, u1, v1, renderPlane),
                    textureIndex,
                    primary,
                    secondary
            );
        };
    }

    private enum Axis {
        X {
            @Override
            int min(Cuboid cuboid) {
                return cuboid.minX();
            }

            @Override
            int max(Cuboid cuboid) {
                return cuboid.maxX();
            }

            @Override
            Projection project(Cuboid cuboid) {
                return new Projection(cuboid.minZ(), cuboid.minY(), cuboid.maxZ(), cuboid.maxY());
            }
        },
        Y {
            @Override
            int min(Cuboid cuboid) {
                return cuboid.minY();
            }

            @Override
            int max(Cuboid cuboid) {
                return cuboid.maxY();
            }

            @Override
            Projection project(Cuboid cuboid) {
                return new Projection(cuboid.minX(), cuboid.minZ(), cuboid.maxX(), cuboid.maxZ());
            }
        },
        Z {
            @Override
            int min(Cuboid cuboid) {
                return cuboid.minZ();
            }

            @Override
            int max(Cuboid cuboid) {
                return cuboid.maxZ();
            }

            @Override
            Projection project(Cuboid cuboid) {
                return new Projection(cuboid.minX(), cuboid.minY(), cuboid.maxX(), cuboid.maxY());
            }
        };

        abstract int min(Cuboid cuboid);

        abstract int max(Cuboid cuboid);

        abstract Projection project(Cuboid cuboid);
    }

    private record BoundaryEffectKey(FieldEffectType type, FieldInfluenceInstance fallbackInstance) {
    }

    private record BoundaryColors(Color primary, Color secondary) {
    }

    private record Cuboid(
            int minX,
            int minY,
            int minZ,
            int maxX,
            int maxY,
            int maxZ,
            FieldInfluenceInstance instance
    ) {
        private static Cuboid from(RangeEntry entry) {
            BlockPos from = entry.from();
            BlockPos to = entry.to();
            int minX = Math.min(from.getX(), to.getX());
            int minY = Math.min(from.getY(), to.getY());
            int minZ = Math.min(from.getZ(), to.getZ());
            int maxX = Math.max(from.getX(), to.getX()) + 1;
            int maxY = Math.max(from.getY(), to.getY()) + 1;
            int maxZ = Math.max(from.getZ(), to.getZ()) + 1;
            return new Cuboid(minX, minY, minZ, maxX, maxY, maxZ, entry.instance());
        }
    }

    private record Projection(int u0, int v0, int u1, int v1) {
    }

    private record BoundaryFace(
            Vec3 center,
            Vec3 right,
            Vec3 up,
            float width,
            float height,
            AABB bounds,
            int textureIndex,
            Color primary,
            Color secondary
    ) {
    }
}
