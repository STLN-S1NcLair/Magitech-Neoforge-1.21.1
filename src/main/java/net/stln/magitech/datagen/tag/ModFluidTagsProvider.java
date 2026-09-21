package net.stln.magitech.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.fluid.FluidContent;
import net.stln.magitech.content.fluid.FluidInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends HolderTagsProvider<Fluid> {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.FLUID, lookupProvider, Magitech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // FluidContentRegister から自動でタグを生成
        for (FluidContent content : FluidInit.REGISTER.getContents()) {
            TagKey<Fluid> fluidTag = content.fluidTag();
            DeferredTagAppender<Fluid> appender = tag(fluidTag);
            appender.add(content.sourceHolder());
            // 流体流がある場合，それもタグに含める
            if (content instanceof FluidContent.Flowing flowing) {
                appender.add(flowing.flowingHolder());
            }
            // 流体源が空気よりも軽い = 気体の場合，ガス状タグもつける
            if (content.fluidType().isLighterThanAir()) {
                tag(Tags.Fluids.GASEOUS).addTag(fluidTag);
            }
        }
    }
}
