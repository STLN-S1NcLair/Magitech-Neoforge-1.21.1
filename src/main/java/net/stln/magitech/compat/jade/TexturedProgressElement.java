package net.stln.magitech.compat.jade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.WailaTickHandler;
import snownee.jade.track.ProgressTrackInfo;

import javax.annotation.Nullable;

public class TexturedProgressElement extends Element {
    private final float ratio;
    private final Component text;
    private final ResourceLocation textureLocation;
    private final Vec2 size;
    private @Nullable ProgressTrackInfo track;

    /**
     * @param ratio           進行度 (0.0F - 1.0F)
     * @param text            表示テキスト
     * @param textureLocation テクスチャのパス (例: "minecraft:block/water_still")
     */
    public TexturedProgressElement(float ratio, Component text, ResourceLocation textureLocation) {
        this.ratio = Math.max(0, Math.min(1, ratio));
        this.text = text;
        this.textureLocation = textureLocation;
        // エネルギーバーなどの標準的なサイズ (幅は可変、高さ14px)
        this.size = new Vec2(100, 14);
    }

    @Override
    public Vec2 getSize() {
        return size;
    }

    @Override
    public void render(GuiGraphics gui, float x, float y, float maxX, float maxY) {

        float width = maxX - x;
        float height = maxY - y;

        if (track == null && getTag() != null) {
            track = WailaTickHandler.instance().progressTracker.getOrCreate(
                    getTag(), ProgressTrackInfo.class, () -> new ProgressTrackInfo(true, this.ratio, width));
        }
        float progress = this.ratio;
        if (track != null) {
            track.setProgress(progress);
            track.update(Minecraft.getInstance().getTimer().getRealtimeDeltaTicks());
            progress = track.getSmoothProgress();
        }

        // --- 1. 枠（背景）の描画 ---
        // Jadeのエネルギーバーと同様の配色（暗い背景 + 枠線）を再現します
        int borderColor = 0x80FFFFFF; // 枠の色
        int bgColor = 0x30303030;     // バーの背景（空の部分）の色

//        // 全体を枠色で塗りつぶし
        gui.fill((int) x, (int) y + 1, (int) x + 1, (int) maxY - 1, borderColor);
        gui.fill((int) x, (int) y, (int) maxX, (int) y + 1, borderColor);
        gui.fill((int) maxX - 1, (int) y + 1, (int) maxX, (int) maxY - 1, borderColor);
        gui.fill((int) x, (int) maxY - 1, (int) maxX, (int) maxY, borderColor);
//        // 1ピクセル内側を背景色で塗りつぶす（これで枠ができる）
        gui.fill((int) x + 1, (int) y + 1, (int) maxX - 1, (int) maxY - 1, bgColor);

        // --- 2. 中身（テクスチャ）の描画 ---
        if (progress > 0) {

            // バーの可動域（枠線1pxを除く）
            int barX = (int) x + 1;
            int barY = (int) y + 1;
            int maxBarWidth = (int) width - 2;
            int barHeight = (int) height - 2;

            // 現在の進行度に基づく幅
            int currentWidth = Math.round(maxBarWidth * progress);

            if (currentWidth > 0) {

                int tileW = 16;
                int tileH = 16;

                // タイリング描画ループ
                for (int dx = 0; dx < currentWidth; dx += tileW) {
                    for (int dy = 0; dy < barHeight; dy += tileH) {
                        int drawX = barX + dx;
                        int drawY = barY + dy;

                        // 描画サイズ計算（端数処理）
                        int w = Math.min(tileW, currentWidth - dx);
                        int h = Math.min(tileH, barHeight - dy);

                        // Spriteを使用して描画
                        gui.blitSprite(textureLocation, 16, 16, 0, (16 - h) / 2, drawX, drawY, w, h);
                    }
                }
            }
        }

        // --- 3. テキストの描画 ---
        // Jadeの標準的なテキスト描画（中央揃え、影付き）
        if (text != null) {
            Minecraft mc = Minecraft.getInstance();
            int textWidth = mc.font.width(text);

            // 座標計算
            float textX = x + 4;
            float textY = y + (height - mc.font.lineHeight) / 2.0f + 2;

            // Zオーダーを少し上げて手前に表示
            gui.pose().pushPose();
            gui.pose().translate(0, 0, 100);
            gui.drawString(mc.font, text, (int) textX, (int) textY, 0xFFFFFFFF, true);
            gui.pose().popPose();
        }
    }
}