package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

/**
 * A compass that points toward the player's last-used portal location in the Pride Lands.
 * Spins randomly in other dimensions (like vanilla compass in the Nether).
 *
 * <p>The needle angle is computed client-side via an {@code ItemProperties} function
 * registered in {@code ClientEvents}.</p>
 */
public class PrideCompassItem extends Item {

    public PrideCompassItem(@NotNull Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}
