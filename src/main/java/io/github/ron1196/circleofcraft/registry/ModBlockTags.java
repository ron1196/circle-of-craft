package io.github.ron1196.circleofcraft.registry;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockTags {

    public static final TagKey<Block> BUG_TRAPS = tag("bug_traps");

    private ModBlockTags() {}

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK, CircleOfCraftMod.id(name));
    }
}
