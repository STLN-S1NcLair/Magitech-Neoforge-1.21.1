package net.stln.magitech.datagen.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class HolderTagsProvider<T> extends TagsProvider<T> {
    protected HolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, modId, existingFileHelper);
    }

    protected HolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, parentProvider, modId, existingFileHelper);
    }

    @Override
    protected @NotNull DeferredTagAppender<T> tag(@NotNull TagKey<T> tag) {
        return new DeferredTagAppender<>(getOrCreateRawBuilder(tag));
    }
    
    protected static class DeferredTagAppender<T> extends TagsProvider.TagAppender<T> {
        protected DeferredTagAppender(@NotNull TagBuilder builder) {
            super(builder);
        }

        @Override
        public @NotNull DeferredTagAppender<T> addTag(@NotNull TagKey<T> tag) {
            super.addTag(tag);
            return this;
        }
        
        public @NotNull TagAppender<T> add(@NotNull Holder<T> holder) {
            holder.unwrapKey().ifPresent(this::add);
            return this;
        }

        @SafeVarargs
        public final @NotNull TagAppender<T> add(@NotNull Holder<T>... holders) {
            for (Holder<T> holder : holders) {
                this.add(holder);
            }
            return this;
        }
    }
}
