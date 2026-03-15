package net.stln.magitech.content.entity.magicentity.nihilflare;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class NihilflareRenderer extends SpellProjectileRenderer<NihilflareEntity> {

    public NihilflareRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NihilflareModel());
    }
}
