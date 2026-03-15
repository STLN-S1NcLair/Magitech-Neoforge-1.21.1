package net.stln.magitech.content.entity.magicentity.illusflare;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class IllusflareRenderer extends SpellProjectileRenderer<IllusflareEntity> {

    public IllusflareRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new IllusflareModel());
    }
}
