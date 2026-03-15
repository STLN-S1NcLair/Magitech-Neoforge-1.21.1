package net.stln.magitech.content.entity.magicentity.frosblast;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class FrosblastRenderer extends SpellProjectileRenderer<FrosblastEntity> {

    public FrosblastRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FrosblastModel());
    }
}
