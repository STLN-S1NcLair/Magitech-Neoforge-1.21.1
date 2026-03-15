package net.stln.magitech.content.entity.magicentity.hydrelux;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class HydreluxRenderer extends SpellProjectileRenderer<HydreluxEntity> {

    public HydreluxRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HydreluxModel());
    }
}
