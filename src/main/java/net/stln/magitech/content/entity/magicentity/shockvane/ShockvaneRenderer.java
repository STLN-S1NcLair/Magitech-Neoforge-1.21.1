package net.stln.magitech.content.entity.magicentity.shockvane;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class ShockvaneRenderer extends SpellProjectileRenderer<ShockvaneEntity> {

    public ShockvaneRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ShockvaneModel());
    }
}
