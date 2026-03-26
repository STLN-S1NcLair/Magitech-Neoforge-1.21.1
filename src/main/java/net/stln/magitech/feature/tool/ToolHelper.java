package net.stln.magitech.feature.tool;

public class ToolHelper {

    public static int getDefaultMaxProgression(int tier) {
        return (int) Math.round(100 * Math.exp(Math.log(3) * Math.sqrt(tier) / Math.sqrt(5)));
    }
}
