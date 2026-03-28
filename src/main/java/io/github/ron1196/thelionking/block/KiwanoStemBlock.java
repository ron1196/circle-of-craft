package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Kiwano Stem — a crop that grows on tilled sand or vanilla farmland.
 * Tilled sand works because it extends FarmBlock.
 */
public class KiwanoStemBlock extends CropBlock {

    public KiwanoStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return LionKingItems.KIWANO_SEEDS.get();
    }
}
