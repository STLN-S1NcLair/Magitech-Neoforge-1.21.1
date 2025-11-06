package net.stln.magitech.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Magitech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        sideBottomTopBlockWithItem(BlockInit.ENGINEERING_WORKBENCH.get());
        sideBottomTopBlockWithItem(BlockInit.ASSEMBLY_WORKBENCH.get());
        sideBottomTopBlockWithItem(BlockInit.REPAIRING_WORKBENCH.get());
        sideBottomTopBlockWithItem(BlockInit.UPGRADE_WORKBENCH.get());
        horizontalHandModeledBlockWithItem(BlockInit.TOOL_HANGER.get());
        handModeledBlockWithItem(BlockInit.ALCHEMETRIC_PYLON.get());
        handModeledBlockWithItem(BlockInit.ATHANOR_PILLAR.get());
        handModeledBlockWithItem(BlockInit.ZARDIUS_CRUCIBLE.get());
        directionalHandModeledBlockWithItem(BlockInit.MANA_NODE.get());
        handModeledBlockWithInventoryModelItem(BlockInit.MANA_VESSEL.get());
        blockWithItem(BlockInit.FLUORITE_ORE.get());
        blockWithItem(BlockInit.DEEPSLATE_FLUORITE_ORE.get());
        blockWithItem(BlockInit.TOURMALINE_ORE.get());
        blockWithItem(BlockInit.DEEPSLATE_TOURMALINE_ORE.get());
        blockWithItem(BlockInit.ZINC_ORE.get());
        blockWithItem(BlockInit.DEEPSLATE_ZINC_ORE.get());
        blockWithItem(BlockInit.RAW_ZINC_BLOCK.get());
        blockWithItem(BlockInit.SULFUR_BLOCK.get());
        blockWithItem(BlockInit.ALCHECRYSITE.get());
        stairsBlockWithItem(BlockInit.ALCHECRYSITE_STAIRS.get(), BlockInit.ALCHECRYSITE.get());
        slabBlockWithItem(BlockInit.ALCHECRYSITE_SLAB.get(), BlockInit.ALCHECRYSITE.get());
        wallBlockWithItem(BlockInit.ALCHECRYSITE_WALL.get(), BlockInit.ALCHECRYSITE.get());
        blockWithItem(BlockInit.POLISHED_ALCHECRYSITE.get());
        stairsBlockWithItem(BlockInit.POLISHED_ALCHECRYSITE_STAIRS.get(), BlockInit.POLISHED_ALCHECRYSITE.get());
        slabBlockWithItem(BlockInit.POLISHED_ALCHECRYSITE_SLAB.get(), BlockInit.POLISHED_ALCHECRYSITE.get());
        wallBlockWithItem(BlockInit.POLISHED_ALCHECRYSITE_WALL.get(), BlockInit.POLISHED_ALCHECRYSITE.get());
        blockWithItem(BlockInit.ALCHECRYSITE_BRICKS.get());
        stairsBlockWithItem(BlockInit.ALCHECRYSITE_BRICK_STAIRS.get(), BlockInit.ALCHECRYSITE_BRICKS.get());
        slabBlockWithItem(BlockInit.ALCHECRYSITE_BRICK_SLAB.get(), BlockInit.ALCHECRYSITE_BRICKS.get());
        wallBlockWithItem(BlockInit.ALCHECRYSITE_BRICK_WALL.get(), BlockInit.ALCHECRYSITE_BRICKS.get());
        blockWithItem(BlockInit.ALCHECRYSITE_TILES.get());
        blockWithItem(BlockInit.FLUORITE_BLOCK.get());
        blockWithItem(BlockInit.FLUORITE_BRICKS.get());
        stairsBlockWithItem(BlockInit.FLUORITE_BRICK_STAIRS.get(), BlockInit.FLUORITE_BRICKS.get());
        slabBlockWithItem(BlockInit.FLUORITE_BRICK_SLAB.get(), BlockInit.FLUORITE_BRICKS.get());
        wallBlockWithItem(BlockInit.FLUORITE_BRICK_WALL.get(), BlockInit.FLUORITE_BRICKS.get());
        logBlockWithItem(BlockInit.CELIFERN_LOG.get());
        woodBlockWithItem(BlockInit.CELIFERN_WOOD.get());
        logBlockWithItem(BlockInit.STRIPPED_CELIFERN_LOG.get());
        woodBlockWithItem(BlockInit.STRIPPED_CELIFERN_WOOD.get());
        blockWithItem(BlockInit.CELIFERN_PLANKS.get());
        stairsBlockWithItem(BlockInit.CELIFERN_STAIRS.get(), BlockInit.CELIFERN_PLANKS.get());
        slabBlockWithItem(BlockInit.CELIFERN_SLAB.get(), BlockInit.CELIFERN_PLANKS.get());
        fenceBlockWithItem(BlockInit.CELIFERN_FENCE.get(), BlockInit.CELIFERN_PLANKS.get());
        fenceGateBlockWithItem(BlockInit.CELIFERN_FENCE_GATE.get(), BlockInit.CELIFERN_PLANKS.get());
        doorBlock(BlockInit.CELIFERN_DOOR.get());
        trapdoorBlockWithItem(BlockInit.CELIFERN_TRAPDOOR.get(), true);
        pressurePlateBlockWithItem(BlockInit.CELIFERN_PRESSURE_PLATE.get(), BlockInit.CELIFERN_PLANKS.get());
        buttonBlockWithItem(BlockInit.CELIFERN_BUTTON.get(), BlockInit.CELIFERN_PLANKS.get());
        leavesBlockWithItem(BlockInit.CELIFERN_LEAVES.get());
        saplingBlock(BlockInit.CELIFERN_SAPLING.get());
        signBlock(BlockInit.CELIFERN_SIGN.get(), BlockInit.CELIFERN_WALL_SIGN.get(), BlockInit.CELIFERN_PLANKS.get());
        hangingSignBlock(BlockInit.CELIFERN_HANGING_SIGN.get(), BlockInit.CELIFERN_WALL_HANGING_SIGN.get(), BlockInit.STRIPPED_CELIFERN_LOG.get());
        logBlockWithItem(BlockInit.CHARCOAL_BIRCH_LOG.get());
        woodBlockWithItem(BlockInit.CHARCOAL_BIRCH_WOOD.get());
        logBlockWithItem(BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get());
        woodBlockWithItem(BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get());
        blockWithItem(BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        stairsBlockWithItem(BlockInit.CHARCOAL_BIRCH_STAIRS.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        slabBlockWithItem(BlockInit.CHARCOAL_BIRCH_SLAB.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        fenceBlockWithItem(BlockInit.CHARCOAL_BIRCH_FENCE.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        fenceGateBlockWithItem(BlockInit.CHARCOAL_BIRCH_FENCE_GATE.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        doorBlock(BlockInit.CHARCOAL_BIRCH_DOOR.get());
        trapdoorBlockWithItem(BlockInit.CHARCOAL_BIRCH_TRAPDOOR.get(), true);
        pressurePlateBlockWithItem(BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        buttonBlockWithItem(BlockInit.CHARCOAL_BIRCH_BUTTON.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        leavesBlockWithItem(BlockInit.CHARCOAL_BIRCH_LEAVES.get());
        saplingBlock(BlockInit.CHARCOAL_BIRCH_SAPLING.get());
        signBlock(BlockInit.CHARCOAL_BIRCH_SIGN.get(), BlockInit.CHARCOAL_BIRCH_WALL_SIGN.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        hangingSignBlock(BlockInit.CHARCOAL_BIRCH_HANGING_SIGN.get(), BlockInit.CHARCOAL_BIRCH_WALL_HANGING_SIGN.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get());
        grassBlockWithItem(BlockInit.SCORCHED_GRASS_SOIL.get(), BlockInit.SCORCHED_SOIL.get());
        blockWithItem(BlockInit.SCORCHED_SOIL.get());
    }

    private String getName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(blockTexture(block)));
    }

    private void handModeledBlockWithItem(Block block) {
        simpleBlock(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(blockTexture(block)));
    }

    private void handModeledBlockWithInventoryModelItem(Block block) {
        simpleBlock(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(blockTexture(block).withSuffix("_inventory")));
    }

    private void sideBottomTopBlockWithItem(Block block) {
        simpleBlockWithItem(block, models().cubeBottomTop(getName(block), blockTexture(block).withSuffix("_side"), blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top")));
        blockItem(block);
    }

    private void grassBlockWithItem(Block block, Block soil) {
        simpleBlockWithItem(block, models().cubeBottomTop(getName(block), blockTexture(block).withSuffix("_side"), blockTexture(soil), blockTexture(block).withSuffix("_top")));
        blockItem(block);
    }

    private void directionalHandModeledBlockWithItem(Block block) {
        directionalBlock(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
        blockItem(block);
    }

    private void horizontalHandModeledBlockWithItem(Block block) {
        horizontalBlock(block, new ModelFile.ExistingModelFile(blockTexture(block), this.models().existingFileHelper));
        blockItem(block);
    }

    private void logBlockWithItem(Block block) {
        logBlock((RotatedPillarBlock) block);
        blockItem(block);
    }

    private void woodBlockWithItem(Block block) {
        ResourceLocation resourceLocation = ResourceLocation.parse(blockTexture(block).toString().replace("_wood", "_log"));
        axisBlock((RotatedPillarBlock) block, resourceLocation, resourceLocation);
        blockItem(block);
    }

    private void stairsBlockWithItem(Block block, Block fullTextureBlock) {
        stairsBlock((StairBlock) block, blockTexture(fullTextureBlock));
        blockItem(block);
    }

    private void slabBlockWithItem(Block block, Block fullTextureBlock) {
        slabBlock((SlabBlock) block, blockTexture(fullTextureBlock), blockTexture(fullTextureBlock));
        blockItem(block);
    }

    private void fenceBlockWithItem(Block block, Block fullTextureBlock) {
        fenceBlock((FenceBlock) block, blockTexture(fullTextureBlock));
        simpleBlockItem(block, models().fenceInventory(getName(block) + "_inventory", blockTexture(fullTextureBlock)));
    }

    private void wallBlockWithItem(Block block, Block fullTextureBlock) {
        wallBlock((WallBlock) block, blockTexture(fullTextureBlock));
        simpleBlockItem(block, models().wallInventory(getName(block) + "_inventory", blockTexture(fullTextureBlock)));
    }

    private void fenceGateBlockWithItem(Block block, Block fullTextureBlock) {
        fenceGateBlock((FenceGateBlock) block, blockTexture(fullTextureBlock));
        blockItem(block);
    }

    private void doorBlock(Block block) {
        doorBlockWithRenderType((DoorBlock) block, blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"), "cutout");
    }

    private void trapdoorBlockWithItem(Block block, boolean orientable) {
        trapdoorBlockWithRenderType((TrapDoorBlock) block, blockTexture(block), orientable, "cutout");
        simpleBlockItem(block, new ModelFile.UncheckedModelFile(blockTexture(block).withSuffix("_bottom")));
    }

    private void pressurePlateBlockWithItem(Block block, Block fullTextureBlock) {
        pressurePlateBlock((PressurePlateBlock) block, blockTexture(fullTextureBlock));
        blockItem(block);
    }

    private void buttonBlockWithItem(Block block, Block fullTextureBlock) {
        buttonBlock((ButtonBlock) block, blockTexture(fullTextureBlock));
        simpleBlockItem(block, models().buttonInventory(getName(block) + "_inventory", blockTexture(fullTextureBlock)));
    }

    private void leavesBlockWithItem(Block block) {
        simpleBlockWithItem(block,
                models().singleTexture(getName(block), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(block)).renderType("cutout"));
        blockItem(block);
    }

    private void saplingBlock(Block block) {
        simpleBlock(block,
                models().cross(getName(block), blockTexture(block)).renderType("cutout"));
    }

    private void signBlock(Block sign, Block wallSign, Block fullTextureBlock) {
        signBlock((StandingSignBlock) sign, (WallSignBlock) wallSign, blockTexture(fullTextureBlock));
    }

    private void hangingSignBlock(Block sign, Block wallSign, Block fullTextureBlock) {
        hangingSignBlock((CeilingHangingSignBlock) sign, (WallHangingSignBlock) wallSign, blockTexture(fullTextureBlock));
    }

    private void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
}
