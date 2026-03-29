package io.github.ron1196.thelionking.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.datafixers.util.Pair;
import io.github.ron1196.thelionking.TheLionKingMod;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import io.github.ron1196.thelionking.quest.stage.StageId;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

/** Debug/testing commands for The Lion King mod. Usage: /lk <subcommand> */
public class LionKingCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lk")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("pridelands")
                        .executes(ctx ->
                                teleportToDimension(ctx.getSource(), Dimensions.PRIDE_LANDS_LEVEL, "Pride Lands")))
                .then(Commands.literal("outlands")
                        .executes(ctx -> teleportToDimension(ctx.getSource(), Dimensions.OUTLANDS_LEVEL, "Outlands")))
                .then(Commands.literal("upendi")
                        .executes(ctx -> teleportToDimension(ctx.getSource(), Dimensions.UPENDI_LEVEL, "Upendi")))
                .then(Commands.literal("overworld")
                        .executes(ctx -> teleportToDimension(ctx.getSource(), Level.OVERWORLD, "Overworld")))
                .then(Commands.literal("tpmound")
                        .executes(ctx -> teleportToStructure(
                                ctx.getSource(), Dimensions.OUTLANDS_LEVEL, "zira_mound", "Zira's Mound")))
                .then(Commands.literal("tptree")
                        .executes(ctx -> teleportToStructure(
                                ctx.getSource(), Dimensions.PRIDE_LANDS_LEVEL, "rafiki_tree", "Rafiki Tree")))
                .then(Commands.literal("tpbooth")
                        .executes(ctx ->
                                teleportToStructure(ctx.getSource(), Level.OVERWORLD, "ticket_booth", "Ticket Booth")))
                .then(Commands.literal("tplodge")
                        .executes(ctx -> teleportToStructure(
                                ctx.getSource(),
                                Dimensions.PRIDE_LANDS_LEVEL,
                                "timon_pumbaa_lodge",
                                "Timon & Pumbaa Lodge")))
                .then(Commands.literal("tptreasure")
                        .executes(ctx -> teleportToStructure(
                                ctx.getSource(), Dimensions.OUTLANDS_LEVEL, "treasure_mound", "Treasure Mound")))
                .then(Commands.literal("quest")
                        .then(Commands.literal("info")
                                .then(Commands.argument("questId", StringArgumentType.word())
                                        .suggests(SUGGEST_QUEST_IDS)
                                        .executes(ctx -> questInfo(
                                                ctx.getSource(), StringArgumentType.getString(ctx, "questId")))))
                        .then(Commands.literal("advance")
                                .then(Commands.argument("questId", StringArgumentType.word())
                                        .suggests(SUGGEST_QUEST_IDS)
                                        .executes(ctx -> questAdvance(
                                                ctx.getSource(), StringArgumentType.getString(ctx, "questId")))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("questId", StringArgumentType.word())
                                        .suggests(SUGGEST_QUEST_IDS)
                                        .then(Commands.argument("stage", StringArgumentType.word())
                                                .suggests(SUGGEST_STAGE_IDS)
                                                .executes(ctx -> questSet(
                                                        ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "questId"),
                                                        StringArgumentType.getString(ctx, "stage"))))))
                        .then(Commands.literal("reset")
                                .then(Commands.argument("questId", StringArgumentType.word())
                                        .suggests(SUGGEST_QUEST_IDS)
                                        .executes(ctx -> questReset(
                                                ctx.getSource(), StringArgumentType.getString(ctx, "questId")))))));
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_QUEST_IDS = (ctx, builder) -> {
        List<Questline> questlines = QuestlineRegistry.getOrdered();
        List<String> ids = questlines.stream().map(Questline::getId).toList();
        return SharedSuggestionProvider.suggest(ids, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_STAGE_IDS = (ctx, builder) -> {
        try {
            String questId = StringArgumentType.getString(ctx, "questId");
            Questline quest = QuestlineRegistry.get(questId);
            if (quest != null) {
                List<StageId> stageOrder = quest.getStageOrder();
                List<String> stages = stageOrder.stream().map(StageId::name).toList();
                return SharedSuggestionProvider.suggest(stages, builder);
            }
        } catch (IllegalArgumentException ignored) {
            // questId argument not yet provided
        }
        return builder.buildFuture();
    };

    private static int questInfo(CommandSourceStack source, String questId) {
        Questline quest = QuestlineRegistry.get(questId);
        if (quest == null) {
            source.sendFailure(Component.literal("Unknown quest: " + questId));
            return 0;
        }

        ServerLevel level = source.getServer().overworld();
        QuestlineManager manager = WorldData.get(level).getQuestManager();
        String stageId = manager.getStageId(questId);
        boolean complete = quest.isComplete(stageId);
        int stageIndex = quest.getStageIndex(stageId);

        source.sendSuccess(
                () -> Component.literal("§6["
                        + quest.getDisplayName()
                        + "]§r Stage: §e"
                        + (stageId.isEmpty() ? "NOT_STARTED" : stageId)
                        + "§r ("
                        + stageIndex
                        + "/"
                        + (quest.getNumStages() - 1)
                        + ")"
                        + (complete ? " §a✔ COMPLETE" : "")),
                false);

        if (!complete && stageIndex >= 0) {
            StageId currentStage = quest.getStageOrder().get(stageIndex);
            String objective = quest.getObjectiveByStage(currentStage);
            source.sendSuccess(() -> Component.literal("§7Objective: " + objective), false);
        }
        return 1;
    }

    private static int questAdvance(CommandSourceStack source, String questId) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }

        Questline quest = QuestlineRegistry.get(questId);
        if (quest == null) {
            source.sendFailure(Component.literal("Unknown quest: " + questId));
            return 0;
        }

        ServerLevel level = player.serverLevel();
        QuestlineManager manager = WorldData.get(level).getQuestManager();
        String currentStageId = manager.getStageId(questId);

        if (quest.isComplete(currentStageId)) {
            source.sendFailure(Component.literal("Quest '" + questId + "' is already complete."));
            return 0;
        }

        // Force-advance by directly setting the next stage (bypasses trigger/item checks)
        StageId currentStage = quest.findStageByName(currentStageId);
        if (currentStage == null) {
            currentStage = quest.getFirstStage();
        }
        StageId nextStage = quest.getNextStage(currentStage);
        if (nextStage == null) {
            source.sendFailure(Component.literal("No next stage."));
            return 0;
        }

        QuestlineState state = manager.getState(questId);
        state.setCurrentStageId(nextStage.name());
        state.setChecked(false);
        WorldData.get(level).setDirty();
        manager.syncToAllPlayers(player.server);

        String newStage = nextStage.name();
        source.sendSuccess(() -> Component.literal("§aAdvanced '" + questId + "' to stage: " + newStage), true);
        return 1;
    }

    private static int questSet(CommandSourceStack source, String questId, String stageName) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }

        Questline quest = QuestlineRegistry.get(questId);
        if (quest == null) {
            source.sendFailure(Component.literal("Unknown quest: " + questId));
            return 0;
        }

        StageId target = quest.findStageByName(stageName);
        if (target == null) {
            source.sendFailure(Component.literal("Unknown stage: " + stageName));
            return 0;
        }

        ServerLevel level = player.serverLevel();
        QuestlineManager manager = WorldData.get(level).getQuestManager();
        QuestlineState state = manager.getState(questId);
        state.setCurrentStageId(target.name());
        state.setChecked(false);
        WorldData.get(level).setDirty();
        manager.syncToAllPlayers(player.server);

        source.sendSuccess(() -> Component.literal("§aSet '" + questId + "' to stage: " + stageName), true);
        return 1;
    }

    private static int questReset(CommandSourceStack source, String questId) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player."));
            return 0;
        }
        Questline quest = QuestlineRegistry.get(questId);
        if (quest == null) {
            source.sendFailure(Component.literal("Unknown quest: " + questId));
            return 0;
        }

        ServerLevel level = player.serverLevel();
        QuestlineManager manager = WorldData.get(level).getQuestManager();
        QuestlineState state = manager.getState(questId);
        state.setCurrentStageId(quest.getFirstStage().name());
        state.setChecked(false);
        WorldData.get(level).setDirty();
        manager.syncToAllPlayers(player.server);

        source.sendSuccess(() -> Component.literal("§aReset '" + questId + "' to first stage."), true);
        return 1;
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
        ResourceKey<Structure> structureKey =
                ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(TheLionKingMod.MOD_ID, structureName));
        Holder.Reference<Structure> holder = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolder(structureKey)
                .orElse(null);
        if (holder == null) return null;

        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, HolderSet.direct(holder), searchFrom, 100, false);
        if (result == null) return null;

        return result.getFirst();
    }

    private static int teleportToStructure(
            CommandSourceStack source, ResourceKey<Level> dimensionKey, String structureName, String displayName) {
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
            player.teleportTo(
                    targetLevel, spawnPos.getX() + 0.5, sy, spawnPos.getZ() + 0.5, player.getYRot(), player.getXRot());
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
        source.sendSuccess(
                () -> Component.literal("Teleported above " + displayName + " at " + x + ", " + y + ", " + z), true);
        return 1;
    }
}
