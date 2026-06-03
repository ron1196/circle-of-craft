package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

/** Kivulite Axe — auto-smelts mined blocks that have a furnace recipe (see {@link FireToolHelper}). */
public class KivuliteAxeItem extends AxeItem {

    public KivuliteAxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
