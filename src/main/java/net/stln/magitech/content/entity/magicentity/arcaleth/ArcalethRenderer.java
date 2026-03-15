package net.stln.magitech.content.entity.magicentity.arcaleth;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class ArcalethRenderer extends SpellProjectileRenderer<ArcalethEntity> {

    public ArcalethRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ArcalethModel());
    }
}
