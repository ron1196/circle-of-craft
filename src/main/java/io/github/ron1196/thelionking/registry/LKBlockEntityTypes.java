package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LKBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TheLionKingMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<GrindingBowlBlockEntity>> GRINDING_BOWL =
            BLOCK_ENTITY_TYPES.register("grinding_bowl",
                    () -> BlockEntityType.Builder.of(GrindingBowlBlockEntity::new,
                            LKBlocks.GRINDING_BOWL.get()).build(null));

    public static final RegistryObject<BlockEntityType<HyenaHeadBlockEntity>> HYENA_HEAD =
            BLOCK_ENTITY_TYPES.register("hyena_head",
                    () -> BlockEntityType.Builder.of(HyenaHeadBlockEntity::new,
                            LKBlocks.HYENA_HEAD.get()).build(null));

    public static final RegistryObject<BlockEntityType<FurRugBlockEntity>> FUR_RUG =
            BLOCK_ENTITY_TYPES.register("fur_rug",
                    () -> BlockEntityType.Builder.of(FurRugBlockEntity::new,
                            LKBlocks.FUR_RUG.get()).build(null));

    public static final RegistryObject<BlockEntityType<BugTrapBlockEntity>> BUG_TRAP =
            BLOCK_ENTITY_TYPES.register("bug_trap",
                    () -> BlockEntityType.Builder.of(BugTrapBlockEntity::new,
                            LKBlocks.BUG_TRAP.get()).build(null));

    public static final RegistryObject<BlockEntityType<BongoDrumBlockEntity>> BONGO_DRUM =
            BLOCK_ENTITY_TYPES.register("bongo_drum",
                    () -> BlockEntityType.Builder.of(BongoDrumBlockEntity::new,
                            LKBlocks.BONGO_DRUM.get()).build(null));

    public static final RegistryObject<BlockEntityType<OutlandsPoolBlockEntity>> OUTLANDS_POOL =
            BLOCK_ENTITY_TYPES.register("outlands_pool",
                    () -> BlockEntityType.Builder.of(OutlandsPoolBlockEntity::new,
                            LKBlocks.OUTLANDS_POOL.get()).build(null));

    public static final RegistryObject<BlockEntityType<LKSpawnerBlockEntity>> LK_SPAWNER =
            BLOCK_ENTITY_TYPES.register("lk_spawner",
                    () -> BlockEntityType.Builder.of(LKSpawnerBlockEntity::new,
                            LKBlocks.LK_SPAWNER.get()).build(null));

    public static final RegistryObject<BlockEntityType<PrideBedBlockEntity>> PRIDE_BED =
            BLOCK_ENTITY_TYPES.register("pride_bed",
                    () -> BlockEntityType.Builder.of(PrideBedBlockEntity::new,
                            LKBlocks.PRIDE_BED.get()).build(null));
}
