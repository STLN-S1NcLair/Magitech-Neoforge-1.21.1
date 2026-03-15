package net.stln.magitech.content.entity.magicentity.frigala;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class FrigalaRenderer extends SpellProjectileRenderer<FrigalaEntity> {

    public FrigalaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FrigalaModel());
    }
}
