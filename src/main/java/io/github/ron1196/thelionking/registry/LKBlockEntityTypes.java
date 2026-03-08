package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.block.entity.FurRugBlockEntity;
import io.github.ron1196.thelionking.block.entity.GrindingBowlBlockEntity;
import io.github.ron1196.thelionking.block.entity.HyenaHeadBlockEntity;
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
}
