package net.stln.magitech.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.damage.DamageTypeTagKeys;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, lookupProvider, Magitech.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(DamageTypeTagKeys.BYPASSES_SHIELD).add(key("mana_berry_bush"));
        tag(DamageTypeTagKeys.NO_KNOCKBACK).add(key("mana_berry_bush"));
        tag(DamageTypeTagKeys.IS_MAGIC).add(key("ember"), key("glace"), key("surge"), key("phantom"), key("tremor"), key("magic"), key("flow"), key("hollow"), key("mana"));
    }

    private static ResourceKey<DamageType> key(String path) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Magitech.id(path));
    }
}
