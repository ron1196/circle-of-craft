package io.github.ron1196.thelionking.registry;

import io.github.ron1196.thelionking.TheLionKingMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class LionKingBlockTags {

    public static final TagKey<Block> BUG_TRAPS = tag("bug_traps");

    private LionKingBlockTags() {}

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(TheLionKingMod.MOD_ID, name));
    }
}
