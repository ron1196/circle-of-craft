package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.*;
import io.github.ron1196.thelionking.entity.hostile.*;
import io.github.ron1196.thelionking.entity.npc.*;
import io.github.ron1196.thelionking.registry.EntityTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {

    private static final int WATER_SEARCH_RADIUS = 6;
    private static final int FLAMINGO_WATER_SEARCH_RADIUS = 8;
    private static final int DIKDIK_MIN_LIGHT = 8;
    private static final int VULTURE_MIN_Y = 60;

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        registerAnimalAttributes(event);
        registerHostileAttributes(event);
        registerNpcAttributes(event);
    }

    private static void registerAnimalAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.LION.get(), LionEntity.createAttributes().build());
        event.put(EntityTypes.LIONESS.get(), LionessEntity.createAttributes().build());
        event.put(EntityTypes.ZEBRA.get(), ZebraEntity.createAttributes().build());
        event.put(EntityTypes.GIRAFFE.get(), GiraffeEntity.createAttributes().build());
        event.put(EntityTypes.RHINO.get(), RhinoEntity.createAttributes().build());
        event.put(EntityTypes.GEMSBOK.get(), GemsbokEntity.createAttributes().build());
        event.put(EntityTypes.DIKDIK.get(), DikDikEntity.createAttributes().build());
        event.put(EntityTypes.FLAMINGO.get(), FlamingoEntity.createAttributes().build());
        event.put(EntityTypes.ZAZU.get(), ZazuEntity.createAttributes().build());
        event.put(EntityTypes.BUG.get(), BugEntity.createAttributes().build());
    }

    private static void registerHostileAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.HYENA.get(), HyenaEntity.createAttributes().build());
        event.put(
                EntityTypes.SKELETAL_HYENA.get(),
                SkeletalHyenaEntity.createAttributes().build());
        event.put(
                EntityTypes.SKELETAL_HYENA_HEAD.get(),
                SkeletalHyenaHeadEntity.createAttributes().build());
        event.put(
                EntityTypes.OUTLANDER.get(), OutlanderEntity.createAttributes().build());
        event.put(EntityTypes.VULTURE.get(), VultureEntity.createAttributes().build());
        event.put(
                EntityTypes.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(EntityTypes.TERMITE.get(), TermiteEntity.createAttributes().build());
        event.put(
                EntityTypes.TERMITE_QUEEN.get(),
                TermiteQueenEntity.createAttributes().build());
    }

    private static void registerNpcAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.RAFIKI.get(), RafikiEntity.createAttributes().build());
        event.put(EntityTypes.SIMBA.get(), SimbaEntity.createAttributes().build());
        event.put(EntityTypes.TIMON.get(), TimonEntity.createAttributes().build());
        event.put(EntityTypes.PUMBAA.get(), PumbaaEntity.createAttributes().build());
        event.put(EntityTypes.SCAR.get(), ScarEntity.createAttributes().build());
        event.put(EntityTypes.ZIRA.get(), ZiraEntity.createAttributes().build());
        event.put(
                EntityTypes.TICKET_LION.get(),
                TicketLionEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        registerPassiveSpawns(event);
        registerHostileSpawns(event);
    }

    private static void registerPassiveSpawns(SpawnPlacementRegisterEvent event) {
        // Standard ground animals
        var groundAnimals = List.of(
                EntityTypes.LION,
                EntityTypes.LIONESS,
                EntityTypes.ZEBRA,
                EntityTypes.GIRAFFE,
                EntityTypes.RHINO,
                EntityTypes.GEMSBOK,
                EntityTypes.ZAZU);

        for (var type : groundAnimals) {
            event.register(
                    type.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules,
                    SpawnPlacementRegisterEvent.Operation.AND);
        }

        // DikDik — grass/sand only, light > 8 (old mod: cave creature list)
        event.register(
                EntityTypes.DIKDIK.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        Animal.checkAnimalSpawnRules(type, level, spawnType, pos, random)
                                && level.getRawBrightness(pos, 0) > DIKDIK_MIN_LIGHT
                                && isGrassOrSand(level, pos.below()),
                SpawnPlacementRegisterEvent.Operation.AND);

        // Flamingo — Upendi only, needs water nearby (old mod: water check in 17x17x17)
        event.register(
                EntityTypes.FLAMINGO.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        Animal.checkAnimalSpawnRules(type, level, spawnType, pos, random)
                                && hasWaterNearby(level, pos, FLAMINGO_WATER_SEARCH_RADIUS),
                SpawnPlacementRegisterEvent.Operation.AND);

        // Bug is not a natural spawn (only from BugTrap)
    }

    private static void registerHostileSpawns(SpawnPlacementRegisterEvent event) {
        var prideLandsHostiles = List.of(EntityTypes.HYENA, EntityTypes.SKELETAL_HYENA);

        for (var type : prideLandsHostiles) {
            event.register(
                    type.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Monster::checkMonsterSpawnRules,
                    SpawnPlacementRegisterEvent.Operation.AND);
        }

        // Crocodile — must be near water
        event.register(
                EntityTypes.CROCODILE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)
                                && hasWaterNearby(level, pos, WATER_SEARCH_RADIUS),
                SpawnPlacementRegisterEvent.Operation.AND);

        // Outlands ground hostiles
        var outlandsHostiles = List.of(EntityTypes.OUTLANDER);

        for (var type : outlandsHostiles) {
            event.register(
                    type.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Monster::checkMonsterSpawnRules,
                    SpawnPlacementRegisterEvent.Operation.AND);
        }

        // Vulture — Outlands, prefers high altitude (Y >= 60)
        event.register(
                EntityTypes.VULTURE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)
                                && (pos.getY() >= VULTURE_MIN_Y || random.nextInt(3) == 0),
                SpawnPlacementRegisterEvent.Operation.AND);
    }

    private static boolean hasWaterNearby(ServerLevelAccessor level, BlockPos pos, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (level.getFluidState(pos.offset(dx, dy, dz)).is(net.minecraft.tags.FluidTags.WATER)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isGrassOrSand(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.DIRT)
                || level.getBlockState(pos).is(BlockTags.SAND);
    }
}
