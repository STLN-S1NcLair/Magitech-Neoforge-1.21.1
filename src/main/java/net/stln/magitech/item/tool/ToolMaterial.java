package net.stln.magitech.item.tool;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolMaterial {
    protected Map<ToolPart, ToolStats> statsMap = new HashMap<>();
    protected String id;
    protected List<Item> materialItem = new ArrayList<>();
    protected Trait materialTrait;

    public ToolMaterial(@Nullable Trait materialTrait, String id) {
        this.materialTrait = materialTrait;
        this.id = id;
    }

    public void addStats(ToolPart part, ToolStats stats) {
        statsMap.put(part, stats);
    }

    public List<Item> getMaterialItem() {
        return this.materialItem;
    }

    public ToolStats getStats(ToolPart part) {
        return statsMap.get(part);
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
