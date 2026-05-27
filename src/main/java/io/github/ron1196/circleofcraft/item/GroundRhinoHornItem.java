package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.entity.animal.GenderedAnimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * Right-click on an adult animal that has a nearby adult mate of the same type
 * to force instant breeding. 1/3 chance of failure (smoke particles).
 * Intercepted via {@code PlayerInteractEvent.EntityInteract} in ModForgeEvents.
 */
public class GroundRhinoHornItem extends Item {

    private static final int FAIL_CHANCE = 3;
    private static final int PARTICLE_COUNT = 7;
    private static final double MATE_SEARCH_RADIUS = 8.0;

    private enum PairDirection {
        /** Both types can initiate breeding with each other. */
        BIDIRECTIONAL,
        /** Only the first type can breed with the second, not the reverse. */
        ONE_WAY
    }

    private record BreedingPair(
            Supplier<EntityType<?>> first, Supplier<EntityType<?>> second, PairDirection direction) {}

    private static final BreedingPair[] BREEDING_PAIR_DEFS = {};

    /** Maps each type to its required mate type — built lazily on first use. */
    private static Map<EntityType<?>, EntityType<?>> breedingPartners;

    public GroundRhinoHornItem(Properties properties) {
        super(properties);
    }

    /**
     * Attempts to use the ground rhino horn on the given animal.
     * @return SUCCESS if breeding occurred or failed with smoke, CONSUME if conditions not met
     */
    public static InteractionResult tryBreed(Animal animal, Player player, ItemStack stack) {
        if (player.level().isClientSide) {
            return InteractionResult.CONSUME;
        }

        Animal mate = findNearbyMate(animal);
        if (mate == null) {
            return InteractionResult.CONSUME;
        }
        stack.shrink(1);

        if (player.getRandom().nextInt(FAIL_CHANCE) == 0) {
            spawnParticles((ServerLevel) player.level(), animal, ParticleTypes.SMOKE);
            return InteractionResult.SUCCESS;
        }

        // Set both animals in love — BreedGoal handles the walk + breeding
        animal.setInLove(player);
        mate.setInLove(player);

        if (player instanceof ServerPlayer serverPlayer) {
            ModCriteriaTriggers.USE_RHINO_HORN.trigger(serverPlayer);
        }

        return InteractionResult.SUCCESS;
    }

    /** Returns the required mate type for the given type, or the type itself if no cross-type pair exists. */
    public static EntityType<?> getPartnerType(EntityType<?> type) {
        return getBreedingPartners().getOrDefault(type, type);
    }

    private static Map<EntityType<?>, EntityType<?>> getBreedingPartners() {
        if (breedingPartners == null) {
            Map<EntityType<?>, EntityType<?>> map = new HashMap<>();
            for (BreedingPair pair : BREEDING_PAIR_DEFS) {
                EntityType<?> first = pair.first().get();
                EntityType<?> second = pair.second().get();
                map.put(first, second);
                if (pair.direction() == PairDirection.BIDIRECTIONAL) {
                    map.put(second, first);
                }
            }
            breedingPartners = Map.copyOf(map);
        }
        return breedingPartners;
    }

    private static Animal findNearbyMate(Animal animal) {
        AABB searchBox = animal.getBoundingBox().inflate(MATE_SEARCH_RADIUS);
        Level level = animal.level();
        var nearby = level.getEntitiesOfClass(Animal.class, searchBox, candidate -> isValidMate(animal, candidate));
        return nearby.isEmpty() ? null : nearby.get(0);
    }

    private static boolean isValidMate(Animal animal, Animal candidate) {
        EntityType<?> mateType = getBreedingPartners().getOrDefault(animal.getType(), animal.getType());
        if (candidate == animal || candidate.getType() != mateType || candidate.isBaby()) {
            return false;
        }
        if (animal instanceof GenderedAnimal a && candidate instanceof GenderedAnimal b) {
            return a.canBreedWith(b);
        }
        return true;
    }

    private static void spawnParticles(ServerLevel level, Animal animal, SimpleParticleType type) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double dx = animal.getRandom().nextGaussian() * 0.02;
            double dy = animal.getRandom().nextGaussian() * 0.02;
            double dz = animal.getRandom().nextGaussian() * 0.02;
            level.sendParticles(
                    type,
                    animal.getRandomX(1.0),
                    animal.getRandomY() + 0.5,
                    animal.getRandomZ(1.0),
                    1,
                    dx,
                    dy,
                    dz,
                    0.0);
        }
    }
}
