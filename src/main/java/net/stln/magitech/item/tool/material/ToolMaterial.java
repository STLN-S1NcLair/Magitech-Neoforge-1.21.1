package net.stln.magitech.item.tool.material;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.trait.Trait;
import org.jetbrains.annotations.NotNull;

public class ToolMaterial {
    protected ToolStats statsMap = ToolStats.DEFAULT;
    protected ToolStats spellCasterStatsMap = ToolStats.DEFAULT;
    protected final ResourceLocation id;
    protected final Trait materialTrait;

    public ToolMaterial(@NotNull Trait materialTrait, ResourceLocation id) {
        this.materialTrait = materialTrait;
        this.id = id;
    }

    public void addStats(@NotNull ToolStats stats) {
        statsMap = stats;
    }

    public void addSpellCasterStats(@NotNull ToolStats stats) {
        spellCasterStatsMap = stats;
    }

    public @NotNull ToolStats getStats() {
        return statsMap;
    }

    public @NotNull ToolStats getSpellCasterStats() {
        return spellCasterStatsMap;
    }

    public @NotNull Trait getTrait() {
        return this.materialTrait;
    }

    public @NotNull ResourceLocation getId() {
        return id;
    }
}
