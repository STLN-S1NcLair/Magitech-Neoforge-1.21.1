package net.stln.magitech.item.tool.material;

import net.minecraft.world.item.Item;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.trait.Trait;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToolMaterial {
    protected ToolStats statsMap = ToolStats.DEFAULT;
    protected ToolStats spellCasterStatsMap = ToolStats.DEFAULT;
    protected String id;
    protected List<Item> materialItem = new ArrayList<>();
    protected Trait materialTrait;

    public ToolMaterial(@Nullable Trait materialTrait, String id) {
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

    public List<Item> getMaterialItem() {
        return this.materialItem;
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

    public void addMaterialItem(Item item) {
        materialItem.add(item);
    }

    public String getId() {
        return id;
    }
}
