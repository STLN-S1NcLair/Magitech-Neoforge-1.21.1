package net.stln.magitech.content.entity.magicentity.hexflare;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class HexflareRenderer extends SpellProjectileRenderer<HexflareEntity> {

    public HexflareRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HexflareModel());
    }
}
