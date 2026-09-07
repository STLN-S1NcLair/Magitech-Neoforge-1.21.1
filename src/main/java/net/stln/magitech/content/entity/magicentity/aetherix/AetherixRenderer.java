package net.stln.magitech.content.entity.magicentity.aetherix;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class AetherixRenderer extends SpellProjectileRenderer<AetherixEntity> {
    public AetherixRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new AetherixModel());
    }
}
