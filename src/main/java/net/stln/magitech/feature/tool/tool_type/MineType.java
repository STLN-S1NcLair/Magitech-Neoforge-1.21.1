package net.stln.magitech.feature.tool.tool_type;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;
import net.neoforged.fml.common.asm.enumextension.NetworkedEnum;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.stln.magitech.content.block.BlockTagKeys;
import net.stln.magitech.feature.element.Element;

import java.util.Set;

@net.neoforged.fml.common.asm.enumextension.NamedEnum()
@NetworkedEnum(NetworkedEnum.NetworkCheck.BIDIRECTIONAL)
public enum MineType implements IExtensibleEnum {
    SWORD("sword", Set.of(BlockTags.SWORD_EFFICIENT, BlockTagKeys.MINABLE_WITH_SWORD), ItemAbilities.DEFAULT_SWORD_ACTIONS),
    AXE("axe", Set.of(BlockTags.MINEABLE_WITH_AXE), ItemAbilities.DEFAULT_AXE_ACTIONS),
    PICKAXE("pickaxe", Set.of(BlockTags.MINEABLE_WITH_PICKAXE), ItemAbilities.DEFAULT_PICKAXE_ACTIONS),
    SHOVEL("shovel", Set.of(BlockTags.MINEABLE_WITH_SHOVEL), ItemAbilities.DEFAULT_SHOVEL_ACTIONS),
    HOE("hoe", Set.of(BlockTags.MINEABLE_WITH_HOE), ItemAbilities.DEFAULT_HOE_ACTIONS);

    private final String id;
    private final Set<TagKey<Block>> minable;
    private final Set<ItemAbility> abilities;

    MineType(String id, Set<TagKey<Block>> minable, Set<ItemAbility> abilities) {
        this.id = id;
        this.minable = minable;
        this.abilities = abilities;
    }

    public String get() {
        return this.id;
    }

    public Set<TagKey<Block>> getMinable() {
        return minable;
    }

    public Set<ItemAbility> getAbilities() {
        return abilities;
    }

    public static net.neoforged.fml.common.asm.enumextension.ExtensionInfo getExtensionInfo() {
        return net.neoforged.fml.common.asm.enumextension.ExtensionInfo.nonExtended(Element.class);
    }
}
