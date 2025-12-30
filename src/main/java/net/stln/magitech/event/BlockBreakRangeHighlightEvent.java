package net.stln.magitech.event;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.item.tool.trait.BlockBreakEvent;
import net.stln.magitech.item.tool.trait.Trait;
import net.stln.magitech.util.ComponentHelper;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

public class BlockBreakRangeHighlightEvent {

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            Level level = mc.level;
            if (player == null || level == null) return;

            HitResult hr = mc.hitResult;
            if (!(hr instanceof BlockHitResult)) return;
            BlockPos target = ((BlockHitResult) hr).getBlockPos();

            if (!shouldShowOverlay(player, target)) return;

            Collection<BlockPos> predicted = calculatePredictedPositions(player, level, target);
            if (predicted.size() < 2) return;

            renderPredicted(context.matrixStack(), context.consumers(), mc.gameRenderer.getMainCamera(), predicted);
        });
    }

    private static boolean shouldShowOverlay(LocalPlayer player, BlockPos target) {
        return player.getMainHandItem().getItem() instanceof PartToolItem && !player.level().getBlockState(target).getShape(player.level(), target, CollisionContext.of(player)).isEmpty()
                && !ComponentHelper.isBroken(player.getMainHandItem());
    }

    // サーバ側ロジックを参照: BlockBreakEvent の public メソッドを利用する
    private static Collection<BlockPos> calculatePredictedPositions(LocalPlayer player, Level level, BlockPos pos) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof PartToolItem) || ComponentHelper.isBroken(stack)) {
            return Collections.emptyList();
        }
        PartToolItem pti = (PartToolItem) stack.getItem();
        Map<Trait, Integer> traitMap = PartToolItem.getTraitLevel(PartToolItem.getTraits(stack));

        Set<BlockPos> blockList = new HashSet<>();
        Direction breakDir;
        try {
            breakDir = PartToolItem.getBreakDirection(player.blockInteractionRange(), pos, player);
        } catch (Throwable t) {
            Vec3 look = player.getLookAngle();
            if (Math.abs(look.y) > Math.abs(look.x) && Math.abs(look.y) > Math.abs(look.z)) {
                breakDir = look.y > 0 ? Direction.UP : Direction.DOWN;
            } else if (Math.abs(look.x) > Math.abs(look.z)) {
                breakDir = look.x > 0 ? Direction.EAST : Direction.WEST;
            } else {
                breakDir = look.z > 0 ? Direction.SOUTH : Direction.NORTH;
            }
        }

        if (pti.getToolType().equals(net.stln.magitech.item.tool.ToolType.HAMMER)) {
            BlockBreakEvent.addHammerMine(player, stack, pos, blockList, breakDir);
        } else if (pti.getToolType().equals(net.stln.magitech.item.tool.ToolType.SCYTHE)) {
            boolean noCollision = level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
            boolean instant = level.getBlockState(pos).getBlock().defaultDestroyTime() == 0.0f;
            if (noCollision || instant) {
                BlockBreakEvent.addScytheMine(player, stack, pos, blockList, level.getBlockState(pos).getBlock());
            } else {
                blockList.add(pos);
            }
        }
        blockList.add(pos);

        Set<BlockPos> blockList2 = new HashSet<>();
        for (BlockPos p : blockList) {
            for (Map.Entry<Trait, Integer> e : traitMap.entrySet()) {
                blockList2.addAll(e.getKey().addAdditionalBlockBreakFirst(player, level, stack, e.getValue(),
                        pti.getSumStats(player, level, stack), level.getBlockState(p), p, 1, breakDir));
            }
        }

        Set<BlockPos> finalList = new HashSet<>();
        for (BlockPos p : blockList2) {
            if (!level.getBlockState(p).getBlock().equals(net.minecraft.world.level.block.Blocks.AIR)) {
                for (Map.Entry<Trait, Integer> e : traitMap.entrySet()) {
                    finalList.addAll(e.getKey().addAdditionalBlockBreakSecond(player, level, stack, e.getValue(),
                            pti.getSumStats(player, level, stack), level.getBlockState(p), p, 1, breakDir));
                }
            }
        }

        finalList.addAll(blockList); // 中心は必ず含める
        return finalList;
    }

    private static RenderType NO_DEPTH_LINE = RenderType.create("magitech:no_depth_line",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(5.0)))
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderType.NO_DEPTH_TEST)
                    .setOutputState(RenderType.MAIN_TARGET)
                    .setCullState(RenderType.NO_CULL)
                    .createCompositeState(false));

    private static void renderPredicted(PoseStack poseStack, MultiBufferSource buffers, Camera camera, Collection<BlockPos> positions) {
        Vec3 camPos = camera.getPosition();
        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f mat = poseStack.last().pose();
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);

        int tickDelta = Math.abs(Minecraft.getInstance().levelRenderer.getTicks() % 40 - 20);
        float r = 1.0f, g = 1.0f, b = 1.0f, a = 0.1f + 0.5f * tickDelta / 20.0f;
        VertexConsumer vc = buffers.getBuffer(NO_DEPTH_LINE);

        if (isFlatFullRectangle(positions)) {
            int y = positions.iterator().next().getY();
            int minX = positions.stream().mapToInt(BlockPos::getX).min().orElse(0);
            int maxX = positions.stream().mapToInt(BlockPos::getX).max().orElse(0);
            int minZ = positions.stream().mapToInt(BlockPos::getZ).min().orElse(0);
            int maxZ = positions.stream().mapToInt(BlockPos::getZ).max().orElse(0);
            AABB bb = new AABB(minX, y, minZ, maxX + 1.0, y + 1.0, maxZ + 1.0);
            // 矩形全体は常に描画（隣接判定を無視）
            putLineBox(vc, mat, bb, Collections.emptySet(), r, g, b, a);
        } else {
            // 各ブロックごとに隣接チェックを行いながら描画
            Set<BlockPos> posSet = new HashSet<>(positions);
            for (BlockPos p : positions) {
                AABB bb = new AABB(p);
                putLineBox(vc, mat, bb, posSet, r, g, b, a);
            }
        }

        poseStack.popPose();
    }

    private static boolean isFlatFullRectangle(Collection<BlockPos> positions) {
        if (positions.isEmpty()) return false;
        Iterator<BlockPos> it = positions.iterator();
        BlockPos first = it.next();
        int y = first.getY();
        int minX = first.getX(), maxX = first.getX();
        int minZ = first.getZ(), maxZ = first.getZ();
        Set<Long> coords = new HashSet<>();
        coords.add(pack(first.getX(), first.getZ()));
        for (BlockPos p : positions) {
            if (p.getY() != y) return false;
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minZ = Math.min(minZ, p.getZ());
            maxZ = Math.max(maxZ, p.getZ());
            coords.add(pack(p.getX(), p.getZ()));
        }
        long expected = (long) (maxX - minX + 1) * (maxZ - minZ + 1);
        return coords.size() == expected;
    }

    private static long pack(int x, int z) {
        return (((long) x) << 32) ^ (z & 0xffffffffL);
    }

    // positions が空なら常に描画する
    private static void putLineBox(VertexConsumer buf, Matrix4f mat, AABB bb, Collection<BlockPos> positions, float r, float g, float b, float a) {
        double x1 = bb.minX, y1 = bb.minY, z1 = bb.minZ;
        double x2 = bb.maxX, y2 = bb.maxY, z2 = bb.maxZ;
        BlockPos p = new BlockPos(
                (int) Math.floor((x1 + x2) * 0.5),
                (int) Math.floor((y1 + y2) * 0.5),
                (int) Math.floor((z1 + z2) * 0.5)
        );

        addLine(buf, mat, x1, y1, z1, x2, y1, z1, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y1, z1, x2, y1, z2, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y1, z2, x1, y1, z2, p, positions, r, g, b, a);
        addLine(buf, mat, x1, y1, z2, x1, y1, z1, p, positions, r, g, b, a);

        addLine(buf, mat, x1, y2, z1, x2, y2, z1, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y2, z1, x2, y2, z2, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y2, z2, x1, y2, z2, p, positions, r, g, b, a);
        addLine(buf, mat, x1, y2, z2, x1, y2, z1, p, positions, r, g, b, a);

        addLine(buf, mat, x1, y1, z1, x1, y2, z1, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y1, z1, x2, y2, z1, p, positions, r, g, b, a);
        addLine(buf, mat, x2, y1, z2, x2, y2, z2, p, positions, r, g, b, a);
        addLine(buf, mat, x1, y1, z2, x1, y2, z2, p, positions, r, g, b, a);
    }

    private static void addLine(
            VertexConsumer buf, Matrix4f mat,
            double x1, double y1, double z1,
            double x2, double y2, double z2,
            BlockPos currentPos,
            Collection<BlockPos> positions,
            float r, float g, float b, float a
    ) {
        if (!positions.isEmpty()) {
            if (shouldSkipEdge(positions, currentPos, x1, y1, z1, x2, y2, z2)) {
                return;
            }
        }

        buf.addVertex(mat, (float) x1, (float) y1, (float) z1)
                .setColor(r, g, b, a)
                .setNormal((float) (x1 - x2), (float) (y1 - y2), (float) (z1 - z2));
        buf.addVertex(mat, (float) x2, (float) y2, (float) z2)
                .setColor(r, g, b, a)
                .setNormal((float) (x1 - x2), (float) (y1 - y2), (float) (z1 - z2));
    }


    private static boolean shouldSkipEdge(
            Collection<BlockPos> positions,
            BlockPos currentPos,
            double x1, double y1, double z1,
            double x2, double y2, double z2
    ) {
        final double EPS = 1e-6;

        boolean alongX = Math.abs(x1 - x2) > 0.5;
        boolean alongY = Math.abs(y1 - y2) > 0.5;
        boolean alongZ = Math.abs(z1 - z2) > 0.5;

        if (alongX) {
            int x = (int) Math.floor(Math.min(x1, x2));
            int y = (int) Math.floor(y1 + EPS);
            int z = (int) Math.floor(z1 + EPS);

            return checkAllSides(
                    positions, currentPos, Axis.X,
                    new BlockPos[]{
                            new BlockPos(x, y,     z),
                            new BlockPos(x, y,     z - 1),
                            new BlockPos(x, y - 1, z - 1),
                            new BlockPos(x, y - 1, z)
                    }
            );
        }

        if (alongZ) {
            int x = (int) Math.floor(x1 + EPS);
            int y = (int) Math.floor(y1 + EPS);
            int z = (int) Math.floor(Math.min(z1, z2));

            return checkAllSides(
                    positions, currentPos, Axis.Z,
                    new BlockPos[]{
                            new BlockPos(x,     y, z),
                            new BlockPos(x - 1, y, z),
                            new BlockPos(x - 1, y - 1, z),
                            new BlockPos(x,     y - 1, z)
                    }
            );
        }

        if (alongY) {
            int x = (int) Math.floor(x1 + EPS);
            int y = (int) Math.floor(Math.min(y1, y2));
            int z = (int) Math.floor(z1 + EPS);

            return checkAllSides(
                    positions, currentPos, Axis.Y,
                    new BlockPos[]{
                            new BlockPos(x,     y, z),
                            new BlockPos(x - 1, y, z),
                            new BlockPos(x - 1, y, z - 1),
                            new BlockPos(x,     y, z - 1)
                    }
            );
        }

        return false;
    }


    @FunctionalInterface
    private interface QuadProvider {
        BlockPos[] get(int a, int b);
    }

    private static boolean checkAllSides(
            Collection<BlockPos> positions,
            BlockPos currentPos,
            Axis axis,
            BlockPos[] q
    ) {
        boolean a = positions.contains(q[0]);
        boolean b = positions.contains(q[1]);
        boolean c = positions.contains(q[2]);
        boolean d = positions.contains(q[3]);

        int count = 0;
        if (a && b) count++;
        if (b && c) count++;
        if (c && d) count++;
        if (d && a) count++;

        // 完全に内部 or 平坦
        if (count == 1 || count == 4) return true;

        // 凹み（2ブロック直線）
        if (count == 2) {
            List<BlockPos> filled = new ArrayList<>(2);
            if (a) filled.add(q[0]);
            if (b) filled.add(q[1]);
            if (c) filled.add(q[2]);
            if (d) filled.add(q[3]);

            // currentPos が責任ブロックでなければスキップ
            return !isEdgeOwner(currentPos, filled.get(0), filled.get(1), axis);
        }

        return false;
    }


    private enum Axis {
        X, Y, Z
    }

    private static boolean isEdgeOwner(
            BlockPos self,
            BlockPos p1,
            BlockPos p2,
            Axis axis
    ) {
        // self が該当しないブロックなら即除外
        if (!self.equals(p1) && !self.equals(p2)) return false;

        BlockPos other = self.equals(p1) ? p2 : p1;

        return switch (axis) {
            case X -> self.getZ() < other.getZ()
                    || (self.getZ() == other.getZ() && self.getY() < other.getY());
            case Z -> self.getX() < other.getX()
                    || (self.getX() == other.getX() && self.getY() < other.getY());
            case Y -> self.getX() < other.getX()
                    || (self.getX() == other.getX() && self.getZ() < other.getZ());
        };
    }


}