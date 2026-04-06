package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.SpellLike;
import net.stln.magitech.feature.tool.upgrade.Upgrade;
import net.stln.magitech.feature.tool.upgrade.UpgradeLike;
import org.jetbrains.annotations.NotNull;

public class DeferredUpgrade<T extends Upgrade> extends DeferredHolder<Upgrade, T> implements UpgradeLike {
    public DeferredUpgrade(ResourceKey<Upgrade> key) {
        super(key);
    }

    public DeferredUpgrade(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.UPGRADE, id));
    }

    @Override
    public @NotNull Upgrade asUpgrade() {
        return get();
    }
}
