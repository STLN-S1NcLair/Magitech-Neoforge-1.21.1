package net.stln.magitech.item.tool.material;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.Trait;
import org.jetbrains.annotations.Nullable;

public class ToolMaterial {
    protected ToolStats statsMap = ToolStats.DEFAULT;
    protected ToolStats spellCasterStatsMap = ToolStats.DEFAULT;
    protected ResourceLocation id;
    protected Trait materialTrait;

    public ToolMaterial(@Nullable Trait materialTrait, ResourceLocation id) {
        this.materialTrait = materialTrait;
        this.id = id;
        ToolMaterialRegister.registerId(id, this);
    }

    public void addStats(ToolStats stats) {
        statsMap = stats;
    }

    public void addSpellCasterStats(ToolStats stats) {
        spellCasterStatsMap = stats;
    }

    public ToolStats getStats() {
        return statsMap;
    }

    public ToolStats getSpellCasterStats() {
        return spellCasterStatsMap;
    }

    public Trait getTrait() {
        return this.materialTrait;
    }

    public ResourceLocation getId() {
        return id;
    }
}
