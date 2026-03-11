package io.github.ron1196.thelionking.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

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
                .then(Commands.literal("tpmound").executes(ctx -> teleportToMound(ctx.getSource())))
                .then(Commands.literal("openmound").executes(ctx -> openMound(ctx.getSource())))
                .then(Commands.literal("moundinfo").executes(ctx -> moundInfo(ctx.getSource())))
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

    private static int teleportToMound(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }

        // Ensure we're in the Outlands, or tp there first
        ServerLevel outlands = source.getServer().getLevel(LKDimensions.OUTLANDS_LEVEL);
        if (outlands == null) {
            source.sendFailure(Component.literal("Outlands dimension not found."));
            return 0;
        }

        LKLevelData data = LKLevelData.get(outlands);
        if (!data.generatedMound) {
            // If not in outlands yet, tp there so the mound can generate
            if (player.level().dimension() != LKDimensions.OUTLANDS_LEVEL) {
                source.sendFailure(Component.literal("No mound generated yet. Use '/lk outlands' first to enter the Outlands."));
                return 0;
            }
            source.sendFailure(Component.literal("No mound has been generated yet. Explore the Outlands to find it."));
            return 0;
        }

        int x = data.moundX;
        int y = data.moundY + 20;
        int z = data.moundZ;
        player.teleportTo(outlands, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());
        source.sendSuccess(() -> Component.literal("Teleported to mound at " + x + ", " + y + ", " + z), true);
        return 1;
    }

    private static int openMound(CommandSourceStack source) {
        ServerLevel outlands = source.getServer().getLevel(LKDimensions.OUTLANDS_LEVEL);
        if (outlands == null) {
            source.sendFailure(Component.literal("Outlands dimension not found."));
            return 0;
        }
        LKLevelData data = LKLevelData.get(outlands);
        if (!data.generatedMound) {
            source.sendFailure(Component.literal("No mound has been generated yet."));
            return 0;
        }
        int i = data.moundX;
        int j = data.moundY;
        int k = data.moundZ;

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

    private static int moundInfo(CommandSourceStack source) {
        ServerLevel outlands = source.getServer().getLevel(LKDimensions.OUTLANDS_LEVEL);
        if (outlands == null) {
            source.sendSuccess(() -> Component.literal("Outlands dimension not found."), false);
            return 1;
        }
        LKLevelData data = LKLevelData.get(outlands);
        if (!data.generatedMound) {
            source.sendSuccess(() -> Component.literal("No mound generated yet."), false);
        } else {
            source.sendSuccess(() -> Component.literal("Mound at: " + data.moundX + ", " + data.moundY + ", " + data.moundZ), false);
        }
        return 1;
    }
}
