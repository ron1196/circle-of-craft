package io.github.ron1196.thelionking.quest.questline;

import static io.github.ron1196.thelionking.quest.questline.OutlandsQuestline.Stage.*;
import static io.github.ron1196.thelionking.quest.stage.QuestObjective.ItemRequirement;
import static io.github.ron1196.thelionking.quest.stage.QuestObjective.Source;
import static io.github.ron1196.thelionking.quest.stage.QuestTrigger.*;

import io.github.ron1196.thelionking.block.PoolCoverBlock;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.hostile.OutlanderEntity;
import io.github.ron1196.thelionking.entity.npc.RafikiEntity;
import io.github.ron1196.thelionking.entity.npc.ZiraEntity;
import io.github.ron1196.thelionking.quest.stage.QuestObjective;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.quest.stage.StageId;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

public class OutlandsQuestline {

    public enum Stage implements StageId {
        ENTER_OUTLANDS,
        FIND_ZIRA,
        COLLECT_INGOTS,
        THROW_IN_OUTWATER,
        COLLECT_FEATHERS,
        FOLLOW_OUTLANDERS,
        ZIRA_OCCUPIES_TREE,
        TALK_TO_PUMBAA,
        GATHER_PUMBAA_INGREDIENTS,
        USE_PUMBAA_BOX,
        RAFIKI_RETURNS,
        ZIRA_RETURNS,
        DEFEAT_ZIRA,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder("outlands")
                .displayName("An Outlandish Scheme")
                .icon(() -> new ItemStack(LionKingItems.WAYWARD_FEATHER.get()))
                .canStart(manager -> manager.isComplete("rafiki"))
                .prerequisites("Complete Rafiki's Quest")
                .stage(Stage.ENTER_OUTLANDS, new QuestObjective("Enter the Outlands"))
                .stage(FIND_ZIRA, new QuestObjective("Find Zira in the Outlands"))
                .stage(
                        COLLECT_INGOTS,
                        new QuestObjective(
                                "Bring Zira 5 kivulite and 2 silver ingots",
                                List.of(
                                        new ItemRequirement(LionKingItems.KIVULITE, 5, Source.MAIN_HAND),
                                        new ItemRequirement(LionKingItems.SILVER_INGOT, 2, Source.INVENTORY))))
                .stage(
                        THROW_IN_OUTWATER,
                        new QuestObjective(
                                "Use the Outwater pool and bring Zira the Outlandish Helm",
                                List.of(new ItemRequirement(LionKingItems.OUTLANDS_HELMET::get, 1, Source.INVENTORY))))
                .stage(
                        COLLECT_FEATHERS,
                        new QuestObjective(
                                "Bring Zira 3 wayward feathers",
                                List.of(new ItemRequirement(LionKingItems.WAYWARD_FEATHER, 3))))
                .stage(FOLLOW_OUTLANDERS, new QuestObjective("Follow the Outlanders to the Pride Lands"))
                .stage(ZIRA_OCCUPIES_TREE, new QuestObjective("Zira has taken over Rafiki's tree"))
                .stage(TALK_TO_PUMBAA, new QuestObjective("Speak to Timon and Pumbaa"))
                .stage(
                        GATHER_PUMBAA_INGREDIENTS,
                        new QuestObjective(
                                "Bring Pumbaa 16 bugs, planks, a jar of lava, and a thrown termite",
                                List.of(
                                        new ItemRequirement(LionKingItems.BUG, 16, Source.INVENTORY),
                                        new ItemRequirement(() -> Items.OAK_PLANKS, 1, Source.INVENTORY),
                                        new ItemRequirement(LionKingItems.JAR_LAVA, 1, Source.INVENTORY),
                                        new ItemRequirement(LionKingItems.TERMITE_THROWN, 1, Source.INVENTORY))))
                .stage(USE_PUMBAA_BOX, new QuestObjective("Expel the Outlanders from Rafiki's tree"))
                .stage(RAFIKI_RETURNS, new QuestObjective("Rafiki returns"))
                .stage(ZIRA_RETURNS, new QuestObjective("Return to the Outlands to confront Zira"))
                .stage(DEFEAT_ZIRA, new QuestObjective("Defeat Zira"))
                .stage(COMPLETE, new QuestObjective("Quest complete"))
                .trigger(Stage.ENTER_OUTLANDS, QuestTrigger.ENTER_OUTLANDS)
                .trigger(FIND_ZIRA, ZIRA_TALK)
                .trigger(COLLECT_INGOTS, ZIRA_TALK)
                .trigger(THROW_IN_OUTWATER, ZIRA_TALK)
                .trigger(COLLECT_FEATHERS, ZIRA_TALK)
                .trigger(FOLLOW_OUTLANDERS, ENTER_PRIDE_LANDS)
                .trigger(ZIRA_OCCUPIES_TREE, ZIRA_TALK)
                .trigger(TALK_TO_PUMBAA, PUMBAA_TALK)
                .trigger(GATHER_PUMBAA_INGREDIENTS, PUMBAA_TALK)
                .trigger(USE_PUMBAA_BOX, PUMBAA_BOX_USED)
                .trigger(RAFIKI_RETURNS, RAFIKI_TALK)
                .trigger(ZIRA_RETURNS, ZIRA_SPAWN_EVENT)
                .trigger(DEFEAT_ZIRA, ZIRA_KILLED)
                .customTransition(COLLECT_INGOTS, OutlandsQuestline::openPoolCover)
                .customTransition(COLLECT_FEATHERS, OutlandsQuestline::startMarch)
                .customTransition(USE_PUMBAA_BOX, OutlandsQuestline::rafikiReturns)
                .build();
    }

    private static final int POOL_SEARCH_RADIUS = 30;
    private static final int MARCH_KILL_RADIUS = 64;
    private static final int FLAME_PARTICLE_COUNT = 24;
    private static final int TREE_KILL_RADIUS = 40;

    private static void openPoolCover(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();

        boolean cleared = false;
        for (BlockPos pos : BlockPos.betweenClosed(
                playerPos.offset(-POOL_SEARCH_RADIUS, -POOL_SEARCH_RADIUS, -POOL_SEARCH_RADIUS),
                playerPos.offset(POOL_SEARCH_RADIUS, POOL_SEARCH_RADIUS, POOL_SEARCH_RADIUS))) {
            if (level.getBlockState(pos).getBlock() instanceof PoolCoverBlock) {
                level.destroyBlock(pos, false);
                cleared = true;
            }
        }

        if (cleared) {
            level.playSound(null, playerPos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    // ── March cutscene: Outlanders "teleport" to Pride Lands ────────────

    private static void startMarch(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();
        WorldData data = WorldData.get(level);

        // Zira's announcement
        sendNpcMessage(
                player,
                "Zira",
                "Well, you've done better dan I expected. Now I can finally leave dis accursed wasteland! Come, Outlanders, and let us reclaim what was once ours!");

        // Kill all Outlanders nearby with flame particles
        AABB searchBox = player.getBoundingBox().inflate(MARCH_KILL_RADIUS);
        for (OutlanderEntity outlander : level.getEntitiesOfClass(OutlanderEntity.class, searchBox)) {
            spawnFlameParticles(level, outlander);
            outlander.discard();
        }

        // Kill Zira with flame particles
        for (ZiraEntity zira : level.getEntitiesOfClass(ZiraEntity.class, searchBox)) {
            spawnFlameParticles(level, zira);
            zira.discard();
        }

        // Portal travel sound
        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.PORTAL_TRAVEL,
                SoundSource.HOSTILE,
                1.0F,
                level.random.nextFloat() * 0.4F + 0.8F);

        // Set world flags — Zira now occupies Rafiki's tree
        data.setZiraOccupiesTree(true);
        data.resetZiraTreeTalkCount();

        // Quest stays at FOLLOW_OUTLANDERS — advances to ZIRA_OCCUPIES_TREE when player enters Pride Lands

        sendNpcMessage(player, "Rafiki", "No! De Outlanders have marched to de Pride Lands! You must follow dem!");
    }

    // ── Pumbaa Box explosion → Rafiki returns ───────────────────────────

    private static void rafikiReturns(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();
        WorldData data = WorldData.get(level);

        // Clear occupation flags
        data.setZiraOccupiesTree(false);

        // Kill Zira (spawn Rafiki at her position) and Outlanders nearby
        AABB searchArea = player.getBoundingBox().inflate(TREE_KILL_RADIUS);
        for (ZiraEntity zira : level.getEntitiesOfClass(ZiraEntity.class, searchArea)) {
            spawnFlameParticles(level, zira);

            // Spawn Rafiki where Zira was standing
            RafikiEntity rafiki = EntityTypes.RAFIKI.get().create(level);
            if (rafiki != null) {
                rafiki.moveTo(zira.getX(), zira.getY(), zira.getZ(), zira.getYRot(), 0F);
                rafiki.setPersistenceRequired();
                level.addFreshEntity(rafiki);
            }

            // Explosion particles around Zira
            for (int i = 0; i < 10; i++) {
                double x = zira.getX() + level.random.nextGaussian() * 5;
                double y = zira.getY() + 3 + level.random.nextFloat() * 8;
                double z = zira.getZ() + level.random.nextGaussian() * 5;
                level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0, 0, 0);
            }

            zira.discard();
        }
        for (OutlanderEntity outlander : level.getEntitiesOfClass(OutlanderEntity.class, searchArea)) {
            spawnFlameParticles(level, outlander);
            outlander.discard();
        }

        // Flatulence + explosion sounds
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, 0.7F);

        // Rafiki's return dialogue
        sendNpcMessage(
                player,
                "Rafiki",
                "Ohoho! Old Rafiki was never gone for good! But you've kicked up quite a stink here, haven't you?");
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private static void spawnFlameParticles(ServerLevel level, Mob entity) {
        for (int i = 0; i < FLAME_PARTICLE_COUNT; i++) {
            double dx = level.random.nextGaussian() * 0.02;
            double dy = level.random.nextGaussian() * 0.02 + level.random.nextFloat() * 0.5;
            double dz = level.random.nextGaussian() * 0.02;
            level.sendParticles(
                    ParticleTypes.FLAME,
                    entity.getX() + level.random.nextGaussian() * 0.5,
                    entity.getY() + 0.25 + level.random.nextFloat(),
                    entity.getZ() + level.random.nextGaussian() * 0.5,
                    1,
                    dx,
                    dy,
                    dz,
                    0);
        }
    }

    private static void sendNpcMessage(ServerPlayer player, String name, String message) {
        ChatHelper.sendNpcMessage(player, name, message);
    }
}
