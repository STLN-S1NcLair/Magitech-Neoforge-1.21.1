package net.stln.magitech.util;

public class MathUtil {

    public static double round(double d, int i) {
        return Math.round(d * Math.pow(10.0, i)) / Math.pow(10.0, i);
    }
}
