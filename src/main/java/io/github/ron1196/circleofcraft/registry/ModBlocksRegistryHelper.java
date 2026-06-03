package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.block.ModLeavesBlock;
import io.github.ron1196.circleofcraft.block.ModSaplingBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Shared property builders and registration shortcuts for {@link ModBlocks}.
 */
final class ModBlocksRegistryHelper {

    private ModBlocksRegistryHelper() {}

    // ========== Property Builders ==========

    static BlockBehaviour.Properties stoneProps() {
        return stoneProps(MapColor.STONE);
    }

    static BlockBehaviour.Properties stoneProps(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(1.5F, 10.0F)
                .requiresCorrectToolForDrops();
    }

    static BlockBehaviour.Properties pillarProps(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(1.2F, 8.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion();
    }

    static BlockBehaviour.Properties oreProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(3.0F, 5.0F)
                .requiresCorrectToolForDrops();
    }

    static BlockBehaviour.Properties metalProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .strength(5.0F, 10.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL);
    }

    static BlockBehaviour.Properties logProps() {
        return logProps(MapColor.WOOD);
    }

    static BlockBehaviour.Properties logProps(MapColor color) {
        return BlockBehaviour.Properties.of().mapColor(color).strength(2.0F).sound(SoundType.WOOD);
    }

    static BlockBehaviour.Properties planksProps() {
        return planksProps(MapColor.WOOD);
    }

    static BlockBehaviour.Properties planksProps(MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(2.0F, 5.0F)
                .sound(SoundType.WOOD);
    }

    static BlockBehaviour.Properties leavesProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((s, g, p, e) -> false)
                .isSuffocating((s, g, p) -> false)
                .isViewBlocking((s, g, p) -> false);
    }

    static BlockBehaviour.Properties saplingProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS);
    }

    static BlockBehaviour.Properties flowerProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS);
    }

    static BlockBehaviour.Properties lilyProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .instabreak()
                .sound(SoundType.LILY_PAD)
                .noOcclusion();
    }

    static BlockBehaviour.Properties cropProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .noOcclusion()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    // ========== Registration Shortcuts ==========

    static DeferredBlock<Block> stoneBlock(String name) {
        return ModBlocks.BLOCKS.register(name, () -> new Block(stoneProps()));
    }

    static DeferredBlock<Block> stoneBlock(String name, MapColor color) {
        return ModBlocks.BLOCKS.register(name, () -> new Block(stoneProps(color)));
    }

    static DeferredBlock<Block> log(String name) {
        return ModBlocks.BLOCKS.register(name, () -> new RotatedPillarBlock(logProps()));
    }

    static DeferredBlock<Block> log(String name, MapColor color) {
        return ModBlocks.BLOCKS.register(name, () -> new RotatedPillarBlock(logProps(color)));
    }

    static DeferredBlock<Block> planks(String name) {
        return ModBlocks.BLOCKS.register(name, () -> new Block(planksProps()));
    }

    static DeferredBlock<Block> planks(String name, MapColor color) {
        return ModBlocks.BLOCKS.register(name, () -> new Block(planksProps(color)));
    }

    static DeferredBlock<StairBlock> stairs(String name, DeferredBlock<? extends Block> base) {
        return ModBlocks.BLOCKS.register(
                name,
                () -> new StairBlock(() -> base.get().defaultBlockState(), BlockBehaviour.Properties.copy(base.get())));
    }

    static DeferredBlock<SlabBlock> slab(String name, DeferredBlock<? extends Block> base) {
        return ModBlocks.BLOCKS.register(name, () -> new SlabBlock(BlockBehaviour.Properties.copy(base.get())));
    }

    static DeferredBlock<WallBlock> wall(String name, DeferredBlock<? extends Block> base) {
        return ModBlocks.BLOCKS.register(name, () -> new WallBlock(BlockBehaviour.Properties.copy(base.get())));
    }

    static DeferredBlock<LeavesBlock> leaves(String name) {
        return ModBlocks.BLOCKS.register(name, () -> new ModLeavesBlock(leavesProps()));
    }

    static DeferredBlock<Block> sapling(String name, AbstractTreeGrower grower) {
        return ModBlocks.BLOCKS.register(name, () -> new ModSaplingBlock(grower, saplingProps()));
    }

    static DeferredBlock<Block> lily(String name) {
        return ModBlocks.BLOCKS.register(name, () -> new WaterlilyBlock(lilyProps()));
    }

    static BlockBehaviour.Properties carpetProps(MapColor color) {
        return BlockBehaviour.Properties.of().mapColor(color).strength(0.1F).sound(SoundType.WOOL);
    }

    static DeferredBlock<Block> carpet(String name, MapColor color) {
        return ModBlocks.BLOCKS.register(name, () -> new CarpetBlock(carpetProps(color)));
    }
}
