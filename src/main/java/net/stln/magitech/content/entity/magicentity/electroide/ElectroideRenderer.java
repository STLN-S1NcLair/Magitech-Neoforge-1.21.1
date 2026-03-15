package net.stln.magitech.content.entity.magicentity.electroide;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class ElectroideRenderer extends SpellProjectileRenderer<ElectroideEntity> {

    public ElectroideRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ElectroideModel());
    }
}
