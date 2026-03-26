package net.stln.magitech.content.entity.mana.mana_parcel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.stln.magitech.content.entity.magicentity.SpellProjectileRenderer;

public class ManaParcelRenderer extends SpellProjectileRenderer<ManaParcelEntity> {

    public ManaParcelRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ManaParcelModel());
    }
}
