package io.github.ron1196.thelionking.event;

import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.entity.animal.*;
import io.github.ron1196.thelionking.entity.hostile.*;
import io.github.ron1196.thelionking.entity.npc.*;
import io.github.ron1196.thelionking.registry.EntityTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheLionKingMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
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

        // Hostile entities
        event.put(EntityTypes.HYENA.get(), HyenaEntity.createAttributes().build());
        event.put(
                EntityTypes.SKELETAL_HYENA.get(),
                SkeletalHyenaEntity.createAttributes().build());
        event.put(
                EntityTypes.OUTLANDER.get(), OutlanderEntity.createAttributes().build());
        event.put(EntityTypes.VULTURE.get(), VultureEntity.createAttributes().build());
        event.put(
                EntityTypes.CROCODILE.get(), CrocodileEntity.createAttributes().build());
        event.put(EntityTypes.TERMITE.get(), TermiteEntity.createAttributes().build());
        event.put(
                EntityTypes.TERMITE_QUEEN.get(),
                TermiteQueenEntity.createAttributes().build());

        // Skeletal Hyena Head
        event.put(
                EntityTypes.SKELETAL_HYENA_HEAD.get(),
                io.github.ron1196.thelionking.entity.hostile.SkeletalHyenaHeadEntity.createAttributes()
                        .build());

        // Ticket Lion
        event.put(
                EntityTypes.TICKET_LION.get(),
                io.github.ron1196.thelionking.entity.npc.TicketLionEntity.createAttributes()
                        .build());

        // NPC entities
        event.put(EntityTypes.RAFIKI.get(), RafikiEntity.createAttributes().build());
        event.put(EntityTypes.SIMBA.get(), SimbaEntity.createAttributes().build());
        event.put(EntityTypes.TIMON.get(), TimonEntity.createAttributes().build());
        event.put(EntityTypes.PUMBAA.get(), PumbaaEntity.createAttributes().build());
        event.put(EntityTypes.SCAR.get(), ScarEntity.createAttributes().build());
        event.put(EntityTypes.ZIRA.get(), ZiraEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        // Passive animals — spawn on grass/dirt in light, like vanilla animals
        event.register(EntityTypes.LION.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.LIONESS.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.ZEBRA.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.GIRAFFE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.RHINO.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.GEMSBOK.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.FLAMINGO.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        // Hostile mobs — spawn on ground in dark, like vanilla monsters
        event.register(EntityTypes.HYENA.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityTypes.SKELETAL_HYENA.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.AND);

        // Crocodile — must be near water (custom check)
        event.register(EntityTypes.CROCODILE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) ->
                        Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)
                        && hasWaterNearby(level, pos, 6),
                SpawnPlacementRegisterEvent.Operation.AND);
    }

    private static boolean hasWaterNearby(
            net.minecraft.world.level.ServerLevelAccessor level,
            net.minecraft.core.BlockPos pos, int radius) {
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
}
