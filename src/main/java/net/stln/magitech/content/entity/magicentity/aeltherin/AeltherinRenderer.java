package net.stln.magitech.content.entity.magicentity.aeltherin;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class AeltherinRenderer extends SpellProjectileRenderer<AeltherinEntity> {
    public AeltherinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new AeltherinModel());
    }
}
