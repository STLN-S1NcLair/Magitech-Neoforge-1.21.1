package net.stln.magitech.effect.visual;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.rendeertype.ComplexRenderTypeToken;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.UUID;

public class RenderTypeTokenInit {

    public static final int FIELD_BOUNDARY_ATLAS_SIZE = 8;

    /**
     * フィールド境界アトラス画像の1辺のピクセル数です。
     * Pixel size of one side of the field-boundary atlas texture.
     */
    public static final int FIELD_BOUNDARY_TEXTURE_SIZE = 128;

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

    private static final ShaderHolder WORLD_DISTORTED_SHADER = new ShaderHolder(
            Magitech.id("world_distorted"),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
    );

    private static final ShaderHolder DISTORTED_ICON_SHADER = new ShaderHolder(
            Magitech.id("distorted_icon"),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
    );

    /**
     * GUIや他の画面描画で使える、テクスチャアイコン用の歪み描画形式です。
     * Distorted render type for textured icons reusable by GUIs and other screen renderers.
     */
    public static final RenderTypeProvider DISTORTED_ICON = new RenderTypeProvider(
            token -> createDistortedRenderType("magitech_distorted_icon", token, DISTORTED_ICON_SHADER)
    );

    /**
     * GUIや他の画面描画で使える、歪みなしのテクスチャ加算合成形式です。
     * Undistorted additive texture render type reusable by GUIs and other screen renderers.
     */
    public static final RenderTypeProvider ADDITIVE_ICON = new RenderTypeProvider(
            token -> LodestoneRenderTypes.ADDITIVE_TEXTURE.apply(token).getRenderType()
    );

    /**
     * アイコン歪み描画用のテクスチャトークンを作成します。
     * Creates a texture token for distorted icon rendering.
     *
     * @param texture 使用するテクスチャ / texture to use
     * @param timeOffset 各描画層の時間オフセット / time offset for the render layer
     * @return 歪みアイコン用トークン / token for distorted icon rendering
     */
    public static RenderTypeToken createDistortedIconToken(ResourceLocation texture, float timeOffset) {
        return RenderTypeToken.createToken(texture)
                .addUniformHandler(handler -> handler.modifyUniform("TimeOffset", timeOffset));
    }

    /**
     * フィールド境界を歪ませて描画する描画形式です。
     * Render type used to draw distorted field boundaries.
     */
    public static final RenderTypeProvider FIELD_BOUNDARY_DISTORTED = new RenderTypeProvider(
            token -> createDistortedRenderType("magitech_field_boundary_distorted", token, WORLD_DISTORTED_SHADER)
    );

    /**
     * フィールド境界用のワールド歪みテクスチャトークンです。
     * World-distortion texture token for field boundaries.
     */
    public static final RenderTypeToken FIELD_BOUNDARY_DISTORTED_TEXTURE = createWorldDistortedToken(
            FIELD_BOUNDARY.getTexture(),
            FIELD_BOUNDARY_ATLAS_SIZE,
            0.0F,
            0.0F
    );

    /**
     * ワールド上のフィールド効果アイコンに使用する歪み描画形式です。
     * Distorted render type used by field-effect icons in the world.
     */
    public static final RenderTypeProvider FIELD_EFFECT_DISTORTED = new RenderTypeProvider(
            token -> createDistortedRenderType("magitech_field_effect_distorted", token, WORLD_DISTORTED_SHADER)
    );

    /**
     * ワールド上の歪みテクスチャをシェーダーへ接続するトークンを作成します。
     * Creates a token that connects a world-distorted texture to the shader.
     *
     * @param texture 使用するテクスチャ / texture to use
     * @param atlasSize アトラスの1辺のタイル数 / number of tiles on one atlas edge
     * @param timeOffset 歪みの時間オフセット / distortion time offset
     * @param distortionMargin 歪みを受ける正規化余白 / normalized margin that receives distortion
     * @return ワールド歪み用トークン / token for world distortion
     */
    public static RenderTypeToken createWorldDistortedToken(
            ResourceLocation texture,
            int atlasSize,
            float timeOffset,
            float distortionMargin
    ) {
        return RenderTypeToken.createToken(texture)
                .addUniformHandler(handler -> {
                    handler.modifyUniform("AtlasSize", (float) atlasSize);
                    handler.modifyUniform("TimeOffset", timeOffset);
                    handler.modifyUniform("DistortionMargin", distortionMargin);
                });
    }

    private static LodestoneRenderType createDistortedRenderType(
            String name,
            RenderTypeToken token,
            ShaderHolder shaderHolder
    ) {
        ShaderUniformHandler uniformHandler = token instanceof ComplexRenderTypeToken complex
                ? complex.getUniformHandler()
                : null;
        RenderStateShard.ShaderStateShard shaderState = new RenderStateShard.ShaderStateShard(() -> {
            ShaderInstance shader = shaderHolder.getShaderInstance();
            if (shader != null && uniformHandler != null) {
                uniformHandler.updateShaderData(shader);
            }
            return shader;
        });
        return LodestoneRenderTypes.createGenericRenderType(
                token,
                name,
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                LodestoneRenderTypes.builder(
                        token,
                        StateShards.ADDITIVE_TRANSPARENCY,
                        shaderState,
                        LodestoneRenderTypes.CULL,
                        LodestoneRenderTypes.LIGHTMAP,
                        LodestoneRenderTypes.COLOR_WRITE
                )
        );
    }

    /**
     * フィールド境界のprimary層です。
     * Primary layer token for field boundaries.
     */
    public static final RenderTypeToken FIELD_BOUNDARY_PRIMARY = new RenderTypeToken(
            new UUID(0x4D41474954454348L, 1L),
            FIELD_BOUNDARY.getTexture()
    ).addUniformHandler(handler -> handler.modifyUniform("TimeOffset", 0.0F));

    /**
     * フィールド境界のsecondary層です。
     * Secondary layer token for field boundaries.
     */
    public static final RenderTypeToken FIELD_BOUNDARY_SECONDARY = new RenderTypeToken(
            new UUID(0x4D41474954454348L, 2L),
            FIELD_BOUNDARY.getTexture()
    ).addUniformHandler(handler -> handler.modifyUniform("TimeOffset", 5000.0F));

    /**
     * フィールド境界の歪みシェーダーを登録します。
     * Registers the field-boundary distortion shader.
     */
    public static void registerShaders(RegisterShadersEvent event) {
        WORLD_DISTORTED_SHADER.register(event);
        DISTORTED_ICON_SHADER.register(event);
    }
}
