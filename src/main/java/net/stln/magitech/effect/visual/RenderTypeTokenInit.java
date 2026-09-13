package net.stln.magitech.effect.visual;

import net.stln.magitech.Magitech;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;

public class RenderTypeTokenInit {

    /**
     * 軌跡と境界辺に使用する発光テクスチャです。
     * Glowing texture used for trails and boundary edges.
     */
    public static final RenderTypeToken TRAIL = RenderTypeToken.createToken(Magitech.id("textures/vfx/trail.png"));

    /**
     * フィールド境界に使用する発光テクスチャです。
     * Glowing texture used for field boundaries.
     */
    public static final RenderTypeToken FIELD_BOUNDARY = RenderTypeToken.createToken(Magitech.id("textures/vfx/field_effect.png"));
}
