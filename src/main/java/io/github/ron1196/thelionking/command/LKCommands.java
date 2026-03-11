package io.github.ron1196.thelionking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * Debug/testing commands for The Lion King mod.
 * Usage: /lk <subcommand>
 */
public class LKCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lk")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("pridelands").executes(ctx -> teleportToDimension(ctx.getSource(), LKDimensions.PRIDE_LANDS_LEVEL, "Pride Lands")))
                .then(Commands.literal("outlands").executes(ctx -> teleportToDimension(ctx.getSource(), LKDimensions.OUTLANDS_LEVEL, "Outlands")))
                .then(Commands.literal("upendi").executes(ctx -> teleportToDimension(ctx.getSource(), LKDimensions.UPENDI_LEVEL, "Upendi")))
                .then(Commands.literal("overworld").executes(ctx -> teleportToDimension(ctx.getSource(), Level.OVERWORLD, "Overworld")))
                .then(Commands.literal("tpmound").executes(ctx -> teleportToStructure(ctx.getSource(), LKDimensions.OUTLANDS_LEVEL, "zira_mound", "Zira's Mound")))
                .then(Commands.literal("tptree").executes(ctx -> teleportToStructure(ctx.getSource(), LKDimensions.PRIDE_LANDS_LEVEL, "rafiki_tree", "Rafiki Tree")))
                .then(Commands.literal("tpbooth").executes(ctx -> teleportToStructure(ctx.getSource(), Level.OVERWORLD, "ticket_booth", "Ticket Booth")))
                .then(Commands.literal("tplodge").executes(ctx -> teleportToStructure(ctx.getSource(), LKDimensions.PRIDE_LANDS_LEVEL, "timon_pumbaa_lodge", "Timon & Pumbaa Lodge")))
                .then(Commands.literal("tptreasure").executes(ctx -> teleportToStructure(ctx.getSource(), LKDimensions.OUTLANDS_LEVEL, "treasure_mound", "Treasure Mound")))
                .then(Commands.literal("openmound").executes(ctx -> openMound(ctx.getSource())))
        );
    }

    private static int teleportToDimension(CommandSourceStack source, ResourceKey<Level> dimensionKey, String name) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
        ServerLevel targetLevel = source.getServer().getLevel(dimensionKey);
        if (targetLevel == null) {
            source.sendFailure(Component.literal("Dimension " + name + " not found."));
            return 0;
        }
        if (player.level().dimension() == dimensionKey) {
            source.sendFailure(Component.literal("You are already in " + name + "."));
            return 0;
        }
        BlockPos spawnPos = targetLevel.getSharedSpawnPos();
        int x = spawnPos.getX();
        int z = spawnPos.getZ();
        int y = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 1;
        player.teleportTo(targetLevel, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());
        source.sendSuccess(() -> Component.literal("Teleported to " + name + " at " + x + ", " + y + ", " + z), true);
        return 1;
    }

    private static BlockPos findNearestStructure(ServerLevel level, String structureName, BlockPos searchFrom) {
        ResourceKey<Structure> structureKey = ResourceKey.create(
                Registries.STRUCTURE, new ResourceLocation(TheLionKingMod.MOD_ID, structureName));
        Holder.Reference<Structure> holder = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolder(structureKey)
                .orElse(null);
        if (holder == null) return null;

        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator()
                .findNearestMapStructure(level, HolderSet.direct(holder), searchFrom, 100, false);
        return result != null ? result.getFirst() : null;
    }

    private static int teleportToStructure(CommandSourceStack source, ResourceKey<Level> dimensionKey,
                                            String structureName, String displayName) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }

        ServerLevel targetLevel = source.getServer().getLevel(dimensionKey);
        if (targetLevel == null) {
            source.sendFailure(Component.literal("Dimension not found."));
            return 0;
        }

        // First teleport to the dimension if not already there
        if (player.level().dimension() != dimensionKey) {
            BlockPos spawnPos = targetLevel.getSharedSpawnPos();
            int sy = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, spawnPos.getX(), spawnPos.getZ()) + 1;
            player.teleportTo(targetLevel, spawnPos.getX() + 0.5, sy, spawnPos.getZ() + 0.5, player.getYRot(), player.getXRot());
        }

        BlockPos structurePos = findNearestStructure(targetLevel, structureName, player.blockPosition());
        if (structurePos == null) {
            source.sendFailure(Component.literal("No " + displayName + " found nearby."));
            return 0;
        }

        int x = structurePos.getX();
        int z = structurePos.getZ();
        int y = 200; // Aerial view
        player.teleportTo(targetLevel, x + 0.5, y, z + 0.5, 0, 90); // Look down
        source.sendSuccess(() -> Component.literal("Teleported above " + displayName + " at " + x + ", " + y + ", " + z), true);
        return 1;
    }

    private static int openMound(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }

        ServerLevel outlands = source.getServer().getLevel(LKDimensions.OUTLANDS_LEVEL);
        if (outlands == null) {
            source.sendFailure(Component.literal("Outlands dimension not found."));
            return 0;
        }

        // Locate nearest mound
        BlockPos searchFrom = player.level().dimension() == LKDimensions.OUTLANDS_LEVEL
                ? player.blockPosition()
                : outlands.getSharedSpawnPos();
        BlockPos moundPos = findNearestStructure(outlands, "zira_mound", searchFrom);
        if (moundPos == null) {
            source.sendFailure(Component.literal("No mound found nearby."));
            return 0;
        }

        // The structure center is at moundPos; the Y of the mound base comes from terrain height
        int i = moundPos.getX();
        int j = outlands.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, i, moundPos.getZ());
        int k = moundPos.getZ();

        // Clear pool cover blocks (from old LKWorldGenZiraMound.clearPoolCover)
        int[][] poolCoverPositions = {
                {-4, 17, 12}, {-4, 17, 11}, {-3, 17, 12}, {-2, 17, 12}, {-1, 17, 12},
                {-3, 17, 11}, {-2, 17, 11}, {-1, 17, 11}, {-1, 17, 10}, {-1, 18, 10},
                {-2, 17, 10}, {-3, 17, 10}, {-4, 17, 10}, {-2, 17, 9}, {-3, 17, 9},
                {-4, 17, 9}, {-4, 17, 8}, {-5, 17, 8}, {-6, 17, 8}, {-5, 17, 9},
                {-6, 17, 9}, {-5, 17, 10}, {-6, 17, 10}, {-5, 17, 11}, {-1, 18, 11},
                {-1, 18, 12}, {-2, 18, 12}, {-2, 18, 11}, {-2, 18, 10}, {-3, 18, 10},
                {-3, 18, 9}, {-4, 18, 9}, {-5, 18, 9}, {-6, 18, 10}, {-5, 18, 10},
                {-4, 18, 10}, {-3, 18, 11}, {-4, 18, 11}, {-3, 18, 12}, {-5, 19, 10},
                {-4, 19, 10}, {-3, 19, 10}, {-3, 19, 11}, {-2, 19, 11}, {-2, 19, 12},
                {-1, 19, 11}, {-2, 19, 10}, {-2, 20, 11}, {-2, 21, 11}, {-3, 20, 10},
                {-3, 21, 10}, {-2, 20, 10}, {-4, 20, 10}, {-4, 19, 9}, {-4, 21, 10},
                {-3, 22, 10}, {-3, 23, 10}, {-4, 22, 10}, {-2, 22, 11}, {-3, 24, 10},
                {-3, 25, 10}, {-2, 21, 10}, {-3, 20, 11}, {-3, 21, 11}, {-5, 20, 10},
                {-4, 19, 11}, {-5, 18, 11}, {-4, 20, 11}, {-5, 21, 10}, {-4, 23, 10},
                {-3, 22, 11}, {-3, 23, 11}, {-3, 24, 11}, {-3, 26, 10}, {-3, 27, 10},
                {-3, 28, 10}, {-4, 20, 9}
        };

        int count = 0;
        for (int[] offset : poolCoverPositions) {
            BlockPos pos = new BlockPos(i + offset[0], j + offset[1], k + offset[2]);
            if (!outlands.getBlockState(pos).isAir()) {
                outlands.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                count++;
            }
        }

        // Also clear the entrance door blocks (metadata 2 in old code)
        int[][] doorPositions = {
                {1, 9, -13}, {0, 9, -13}, {-14, 10, 0}
        };
        for (int[] offset : doorPositions) {
            BlockPos pos = new BlockPos(i + offset[0], j + offset[1], k + offset[2]);
            if (!outlands.getBlockState(pos).isAir()) {
                outlands.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                count++;
            }
        }

        int cleared = count;
        source.sendSuccess(
                () -> Component.literal("Opened mound: cleared " + cleared + " blocks at " + i + ", " + j + ", " + k),
                true
        );
        return 1;
    }
}
