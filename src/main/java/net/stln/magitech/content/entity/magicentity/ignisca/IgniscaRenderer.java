package net.stln.magitech.content.entity.magicentity.ignisca;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class IgniscaRenderer extends SpellProjectileRenderer<IgniscaEntity> {

    public IgniscaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new IgniscaModel());
    }
}
