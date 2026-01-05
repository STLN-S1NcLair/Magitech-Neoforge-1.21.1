package net.stln.magitech.util;

public class MathUtil {

    public static double round(double d, int i) {
        return Math.round(d * Math.pow(10.0, i)) / Math.pow(10.0, i);
    }

    public static double getGeneralAngle(double angle) {
        while (angle < 0) {
            angle += Math.PI * 2;
        }
        while (angle >= Math.PI * 2) {
            angle -= Math.PI * 2;
        }
        return angle;
    }

    public static long min(long... values) {
        long min = Long.MAX_VALUE;
        for (long val : values) {
            min = Math.min(min, val);
        }
        return min;
    }

    public static int min(int... values) {
        int min = Integer.MAX_VALUE;
        for (int val : values) {
            min = Math.min(min, val);
        }
        return min;
    }
}
