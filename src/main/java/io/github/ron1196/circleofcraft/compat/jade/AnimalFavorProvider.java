package io.github.ron1196.circleofcraft.compat.jade;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.resources.ResourceLocation;

/**
 * TODO(jade compat): port to the Jade 15.x (1.21.1) API — see {@link ModJadePlugin} for why this is
 * stubbed (no compile-usable Jade API artifact for 1.21.1). Tracked as a GitHub issue.
 *
 * <p>Intended behaviour (server data + entity tooltip): for a {@code ModAnimal}, read the favor entry
 * for the looking player ({@code animal.getFavorEntryFor(player.getUUID())}); if present, write the
 * required item id and amount into server data, then render
 * {@code jade.circleofcraft.animal_favor.wants} (amount, item name) in GOLD. The item is resolved
 * with {@code BuiltInRegistries.ITEM.get(ResourceLocation.parse(id))} per the Task 3 NBT change.
 */
public final class AnimalFavorProvider {

    public static final ResourceLocation UID = CircleOfCraftMod.id("animal_favor");

    private AnimalFavorProvider() {}
}
