package net.stln.magitech;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE_DASH_SPELLS = BUILDER
            .translation("config.magitech.disable_dash_spells")
            .comment("If true, dash spells are disabled.")
            .define("disableDashSpells", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
