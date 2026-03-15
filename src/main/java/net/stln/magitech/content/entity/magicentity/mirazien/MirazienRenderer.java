package net.stln.magitech.content.entity.magicentity.mirazien;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class MirazienRenderer extends SpellProjectileRenderer<MirazienEntity> {

    public MirazienRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MirazienModel());
    }
}
