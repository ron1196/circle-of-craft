package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.block.LionKingLeavesBlock;
import io.github.ron1196.thelionking.block.LionKingSaplingBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

/**
 * Shared property builders and registration shortcuts for {@link LionKingBlocks}.
 */
final class LionKingBlocksRegistryHelper {

    private LionKingBlocksRegistryHelper() {}

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
                .requiresCorrectToolForDrops();
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
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP);
    }

    // ========== Registration Shortcuts ==========

    static RegistryObject<Block> stoneBlock(String name) {
        return LionKingBlocks.BLOCKS.register(name, () -> new Block(stoneProps()));
    }

    static RegistryObject<Block> stoneBlock(String name, MapColor color) {
        return LionKingBlocks.BLOCKS.register(name, () -> new Block(stoneProps(color)));
    }

    static RegistryObject<Block> log(String name) {
        return LionKingBlocks.BLOCKS.register(name, () -> new RotatedPillarBlock(logProps()));
    }

    static RegistryObject<Block> log(String name, MapColor color) {
        return LionKingBlocks.BLOCKS.register(name, () -> new RotatedPillarBlock(logProps(color)));
    }

    static RegistryObject<Block> planks(String name) {
        return LionKingBlocks.BLOCKS.register(name, () -> new Block(planksProps()));
    }

    static RegistryObject<Block> planks(String name, MapColor color) {
        return LionKingBlocks.BLOCKS.register(name, () -> new Block(planksProps(color)));
    }

    static RegistryObject<StairBlock> stairs(String name, RegistryObject<? extends Block> base) {
        return LionKingBlocks.BLOCKS.register(
                name,
                () -> new StairBlock(() -> base.get().defaultBlockState(), BlockBehaviour.Properties.copy(base.get())));
    }

    static RegistryObject<SlabBlock> slab(String name, RegistryObject<? extends Block> base) {
        return LionKingBlocks.BLOCKS.register(name, () -> new SlabBlock(BlockBehaviour.Properties.copy(base.get())));
    }

    static RegistryObject<WallBlock> wall(String name, RegistryObject<? extends Block> base) {
        return LionKingBlocks.BLOCKS.register(name, () -> new WallBlock(BlockBehaviour.Properties.copy(base.get())));
    }

    static RegistryObject<LeavesBlock> leaves(String name) {
        return LionKingBlocks.BLOCKS.register(name, () -> new LionKingLeavesBlock(leavesProps()));
    }

    static RegistryObject<Block> sapling(String name, AbstractTreeGrower grower) {
        return LionKingBlocks.BLOCKS.register(name, () -> new LionKingSaplingBlock(grower, saplingProps()));
    }

    static RegistryObject<Block> lily(String name) {
        return LionKingBlocks.BLOCKS.register(name, () -> new WaterlilyBlock(lilyProps()));
    }
}
