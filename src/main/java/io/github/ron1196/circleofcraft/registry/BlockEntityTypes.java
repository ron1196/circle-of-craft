package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CircleOfCraftMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrindingBowlBlockEntity>> GRINDING_BOWL =
            BLOCK_ENTITY_TYPES.register("grinding_bowl", () -> BlockEntityType.Builder.of(
                            GrindingBowlBlockEntity::new, ModBlocks.GRINDING_BOWL.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HyenaHeadBlockEntity>> HYENA_HEAD =
            BLOCK_ENTITY_TYPES.register("hyena_head", () -> BlockEntityType.Builder.of(
                            HyenaHeadBlockEntity::new, ModBlocks.HYENA_HEAD.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BugTrapBlockEntity>> BUG_TRAP =
            BLOCK_ENTITY_TYPES.register("bug_trap", () -> BlockEntityType.Builder.of(
                            BugTrapBlockEntity::new,
                            ModBlocks.ALL_BUG_TRAPS.stream()
                                    .map(net.neoforged.neoforge.registries.DeferredHolder::get)
                                    .toArray(net.minecraft.world.level.block.Block[]::new))
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BongoDrumBlockEntity>> BONGO_DRUM =
            BLOCK_ENTITY_TYPES.register("bongo_drum", () -> BlockEntityType.Builder.of(
                            BongoDrumBlockEntity::new, ModBlocks.BONGO_DRUM.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OutlandsPoolBlockEntity>> OUTLANDS_POOL =
            BLOCK_ENTITY_TYPES.register("outlands_pool", () -> BlockEntityType.Builder.of(
                            OutlandsPoolBlockEntity::new, ModBlocks.OUTLANDS_POOL.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrideBedBlockEntity>> PRIDE_BED =
            BLOCK_ENTITY_TYPES.register(
                    "pride_bed", () -> BlockEntityType.Builder.of(PrideBedBlockEntity::new, ModBlocks.PRIDE_BED.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MountedShooterBlockEntity>> MOUNTED_SHOOTER =
            BLOCK_ENTITY_TYPES.register("mounted_shooter", () -> BlockEntityType.Builder.of(
                            MountedShooterBlockEntity::new, ModBlocks.MOUNTED_SHOOTER.get())
                    .build(null));
}
