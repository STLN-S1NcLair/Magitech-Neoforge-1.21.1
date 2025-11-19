package net.stln.magitech.util;

import java.awt.*;

public class ColorHelper {
    public static int channelFromFloat(float value) {
        return MathHelper.floor(value * 255.0F);
    }

    public static int getTierColor(int tier) {
        if (tier <= 4) {
            double t = (double) (tier) / Math.max(1, 4);
            double l = interpolate(40, 55, t);
            Color c = hslToRgb(0, 0, l);
            return rgbToInt(c);
        }

        // Tier 5〜
        int tiersPerSegment = 5;
        int baseH = 70;
        int relativeTier = tier - 5;
        int segmentIndex = relativeTier / tiersPerSegment;
        int segmentOffset = relativeTier % tiersPerSegment;

        // 巻き戻し回数を数える
        int rollCount = 0;
        int prevH = baseH % 360;
        for (int i = 1; i <= segmentIndex; i++) {
            int currentH = (baseH + i * 110) % 360;
            if (currentH < prevH) {
                rollCount++;
            }
            prevH = currentH;
        }

        // 現在のセグメント色相範囲
        int h1 = (baseH + segmentIndex * 110) % 360;
        int h2 = (h1 + 45) % 360;

        // 明度補正（上限90/95に制限）
        double l1 = Math.min(40 + rollCount * 10, 90);
        double l2 = Math.min(65 + rollCount * 10, 95);

        // 色補間
        double t = (double) segmentOffset / (tiersPerSegment - 1);
        double h = interpolateHue(h1, h2, t);
        double s = interpolate(30, 75, t);
        double l = interpolate(l1, l2, t);

        Color c = hslToRgb(h, s, l);
        return rgbToInt(c);
    }

    // 線形補間
    private static double interpolate(double start, double end, double t) {
        return start + (end - start) * t;
    }

    private static double interpolateHue(double h1, double h2, double t) {
        double delta = ((h2 - h1 + 540) % 360) - 180;  // 最短経路を計算
        return (h1 + delta * t + 360) % 360;
    }

    // HSL → RGB
    private static Color hslToRgb(double h, double s, double l) {
        s /= 100;
        l /= 100;

        double c = (1 - Math.abs(2 * l - 1)) * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = l - c / 2;

        double r = 0, g = 0, b = 0;

        if (0 <= h && h < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (h < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (h < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (h < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (h < 300) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        int R = (int) Math.round((r + m) * 255);
        int G = (int) Math.round((g + m) * 255);
        int B = (int) Math.round((b + m) * 255);

        return new Color(clamp(R), clamp(G), clamp(B));
    }

    // RGB → int
    private static int rgbToInt(Color c) {
        return (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
    }

    private static int clamp(int val) {
        return Math.max(0, Math.min(255, val));
    }

    public static class Abgr {
        public static int getAlpha(int abgr) {
            return abgr >>> 24;
        }

        public static int getRed(int abgr) {
            return abgr & 0xFF;
        }

        public static int getGreen(int abgr) {
            return abgr >> 8 & 0xFF;
        }

        public static int getBlue(int abgr) {
            return abgr >> 16 & 0xFF;
        }

        public static int getBgr(int abgr) {
            return abgr & 16777215;
        }

        public static int toOpaque(int abgr) {
            return abgr | 0xFF000000;
        }

        public static int getAbgr(int a, int b, int g, int r) {
            return a << 24 | b << 16 | g << 8 | r;
        }

        public static int withAlpha(int alpha, int bgr) {
            return alpha << 24 | bgr & 16777215;
        }

        public static int toAbgr(int argb) {
            return argb & -16711936 | (argb & 0xFF0000) >> 16 | (argb & 0xFF) << 16;
        }

        public static int mul(int color, int mulColor) {
            int alpha = Math.clamp((long) getAlpha(color) * getAlpha(mulColor), 0, 255);
            int red = Math.clamp((long) getRed(color) * getRed(mulColor), 0, 255);
            int green = Math.clamp((long) getGreen(color) * getGreen(mulColor), 0, 255);
            int blue = Math.clamp((long) getBlue(color) * getBlue(mulColor), 0, 255);
            return getAbgr(alpha, blue, green, red);
        }
    }

    /**
     * Contains color-related helper methods that use ARGB colors represented
     * as {@code 0xAARRGGBB}.
     */
    public static class Argb {
        /**
         * {@return the alpha value of {@code argb}}
         *
         * <p>The returned value is between {@code 0} and {@code 255} (both inclusive).
         */
        public static int getAlpha(int argb) {
            return argb >>> 24;
        }

        /**
         * {@return the red value of {@code argb}}
         *
         * <p>The returned value is between {@code 0} and {@code 255} (both inclusive).
         */
        public static int getRed(int argb) {
            return argb >> 16 & 0xFF;
        }

        /**
         * {@return the green value of {@code argb}}
         *
         * <p>The returned value is between {@code 0} and {@code 255} (both inclusive).
         */
        public static int getGreen(int argb) {
            return argb >> 8 & 0xFF;
        }

        /**
         * {@return the blue value of {@code argb}}
         *
         * <p>The returned value is between {@code 0} and {@code 255} (both inclusive).
         */
        public static int getBlue(int argb) {
            return argb & 0xFF;
        }

        /**
         * {@return the ARGB color value from its components}
         */
        public static int getArgb(int alpha, int red, int green, int blue) {
            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        public static int getArgb(int red, int green, int blue) {
            return getArgb(255, red, green, blue);
        }

        public static int mixColor(int first, int second) {
            return getArgb(
                    getAlpha(first) * getAlpha(second) / 255,
                    getRed(first) * getRed(second) / 255,
                    getGreen(first) * getGreen(second) / 255,
                    getBlue(first) * getBlue(second) / 255
            );
        }

        public static int lerp(float delta, int start, int end) {
            int i = MathHelper.lerp(delta, getAlpha(start), getAlpha(end));
            int j = MathHelper.lerp(delta, getRed(start), getRed(end));
            int k = MathHelper.lerp(delta, getGreen(start), getGreen(end));
            int l = MathHelper.lerp(delta, getBlue(start), getBlue(end));
            return getArgb(i, j, k, l);
        }

        public static int fullAlpha(int argb) {
            return argb | 0xFF000000;
        }

        public static int withAlpha(int alpha, int rgb) {
            return alpha << 24 | rgb & 16777215;
        }

        public static int fromFloats(float a, float r, float g, float b) {
            return getArgb(ColorHelper.channelFromFloat(a), ColorHelper.channelFromFloat(r), ColorHelper.channelFromFloat(g), ColorHelper.channelFromFloat(b));
        }

        public static int averageArgb(int a, int b) {
            return getArgb((getAlpha(a) + getAlpha(b)) / 2, (getRed(a) + getRed(b)) / 2, (getGreen(a) + getGreen(b)) / 2, (getBlue(a) + getBlue(b)) / 2);
        }

        public static int mul(int color, int mulColor) {
            int alpha = Math.clamp((long) getAlpha(color) * getAlpha(mulColor) / 255, 0, 255);
            int red = Math.clamp((long) getRed(color) * getRed(mulColor) / 255, 0, 255);
            int green = Math.clamp((long) getGreen(color) * getGreen(mulColor) / 255, 0, 255);
            int blue = Math.clamp((long) getBlue(color) * getBlue(mulColor) / 255, 0, 255);
            return getArgb(alpha, red, green, blue);
        }
    }
}

