package net.stln.magitech.helper;

public class TextUtil {

    public static String toSignedPercent(double value) {
        return (value >= 0 ? "+" : "") + MathHelper.round((value * 100), 1) + "%";
    }

    public static String toSignedIntPercent(double value) {
        return (value >= 0 ? "+" : "") + Math.round(value * 100) + "%";
    }
}
