package net.stln.magitech.content.entity.magicentity.tremivox;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class TremivoxRenderer extends SpellProjectileRenderer<TremivoxEntity> {

    public TremivoxRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new TremivoxModel());
    }
}
