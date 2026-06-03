package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

/** Kivulite Pickaxe — auto-smelts mined blocks that have a furnace recipe (see {@link FireToolHelper}). */
public class KivulitePickaxeItem extends PickaxeItem {

    public KivulitePickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
