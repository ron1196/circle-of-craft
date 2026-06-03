package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

/**
 * Kivulite Shovel — auto-smelts mined blocks that have a furnace recipe and converts clay blocks
 * directly into bricks (see {@link FireToolHelper}).
 */
public class KivuliteShovelItem extends ShovelItem {

    public KivuliteShovelItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
