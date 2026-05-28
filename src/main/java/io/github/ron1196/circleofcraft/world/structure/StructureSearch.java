package io.github.ron1196.circleofcraft.world.structure;

import com.mojang.datafixers.util.Pair;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StructureSearch {

    private static final int SEARCH_CHUNK_RADIUS = 100;

    private StructureSearch() {}

    @Nullable
    public static BlockPos findNearest(
            @NotNull ServerLevel level, @NotNull String structureId, @NotNull BlockPos from) {
        ResourceKey<Structure> structureKey =
                ResourceKey.create(Registries.STRUCTURE, CircleOfCraftMod.id(structureId));
        Holder.Reference<Structure> holder = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolder(structureKey)
                .orElse(null);
        if (holder == null) return null;

        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, HolderSet.direct(holder), from, SEARCH_CHUNK_RADIUS, false);
        return result == null ? null : result.getFirst();
    }
}
