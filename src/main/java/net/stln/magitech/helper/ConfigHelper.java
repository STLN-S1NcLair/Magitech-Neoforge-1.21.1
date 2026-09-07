package net.stln.magitech.helper;

public class ConfigHelper {

    public static boolean isDashSpellsDisabled() {
        return net.stln.magitech.Config.DISABLE_DASH_SPELLS.get();
    }
}
