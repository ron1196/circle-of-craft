package io.github.ron1196.thelionking.quest.questline;

import static io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage.*;
import static io.github.ron1196.thelionking.quest.stage.QuestObjective.ItemRequirement;
import static io.github.ron1196.thelionking.quest.stage.QuestObjective.Source;
import static io.github.ron1196.thelionking.quest.stage.QuestTrigger.*;

import io.github.ron1196.thelionking.entity.npc.ScarEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.quest.actions.RafikiQuestActions;
import io.github.ron1196.thelionking.quest.stage.ClaimableReward;
import io.github.ron1196.thelionking.quest.stage.QuestObjective;
import io.github.ron1196.thelionking.quest.stage.StageId;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;

public class RafikiQuestline {

    private static final int GATE_SEARCH_RADIUS = 20;

    public enum Stage implements StageId {
        FIND_RAFIKI,
        CRAFT_RAFIKI_STICK,
        RALLY_PUMBAA,
        COLLECT_BONES,
        DEFEAT_SCAR,
        COLLECT_TERMITES,
        COLLECT_MANGOES,
        USE_STAR_ALTAR,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(LionKingItems.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new QuestObjective("Find Rafiki and speak to him"))
                .stage(
                        CRAFT_RAFIKI_STICK,
                        new QuestObjective(
                                "Bring Rafiki a stick, a mango, and a bug",
                                List.of(
                                        new ItemRequirement(() -> Items.STICK, 1, Source.INVENTORY),
                                        new ItemRequirement(LionKingItems.MANGO, 1, Source.INVENTORY),
                                        new ItemRequirement(LionKingItems.BUG, 1, Source.INVENTORY))))
                .stage(RALLY_PUMBAA, new QuestObjective("Find Timon and Pumbaa"))
                .stage(
                        COLLECT_BONES,
                        new QuestObjective(
                                "Bring Rafiki 64 hyena bones",
                                List.of(new ItemRequirement(LionKingItems.HYENA_BONE, 64))))
                .stage(DEFEAT_SCAR, new QuestObjective("Defeat Scar and return to Rafiki"))
                .stage(
                        COLLECT_TERMITES,
                        new QuestObjective(
                                "Bring Rafiki 4 termite dust",
                                List.of(new ItemRequirement(LionKingItems.TERMITE_DUST, 4))))
                .stage(
                        COLLECT_MANGOES,
                        new QuestObjective(
                                "Bring Rafiki 4 mango dust", List.of(new ItemRequirement(LionKingItems.MANGO_DUST, 4))))
                .stage(USE_STAR_ALTAR, new QuestObjective("Craft a Star Altar and use Rafiki Dust on it"))
                .stage(RafikiQuestline.Stage.COMPLETE, new QuestObjective("Quest complete"))
                .claimableReward(CRAFT_RAFIKI_STICK, new ClaimableReward(LionKingItems.RAFIKI_STICK, 1))
                .trigger(FIND_RAFIKI, RAFIKI_TALK)
                .trigger(CRAFT_RAFIKI_STICK, RAFIKI_TALK)
                .trigger(RALLY_PUMBAA, PUMBAA_TALK)
                .trigger(COLLECT_BONES, RAFIKI_TALK)
                .trigger(DEFEAT_SCAR, SCAR_KILLED)
                .trigger(COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(COLLECT_MANGOES, RAFIKI_TALK)
                .trigger(USE_STAR_ALTAR, STAR_ALTAR_USED)
                .customTransition(COLLECT_BONES, RafikiQuestline::spawnScar)
                .customTransition(DEFEAT_SCAR, RafikiQuestline::openOutlandsPortal)
                .build();
    }

    // ── Scar spawn constants ──────────────────────────────────────────────────
    private static final int SCAR_SEARCH_RADIUS = 60;
    private static final int SCAR_MIN_Y = 10;
    private static final int SCAR_MAX_Y = 40;
    private static final int SCAR_SEARCH_ATTEMPTS = 200;
    private static final int SCAR_FALLBACK_DISTANCE = 30;

    /**
     * When player brings 64 bones to Rafiki, spawn Scar underground nearby.
     * Searches for a cave (air block with solid ground) within range.
     */
    private static void spawnScar(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();

        BlockPos spawnPos = findCaveSpawn(level, player.blockPosition());
        if (spawnPos == null) {
            // Fallback: spawn on surface nearby
            int x = Mth.floor(player.getX()) + SCAR_FALLBACK_DISTANCE;
            int z = Mth.floor(player.getZ()) + SCAR_FALLBACK_DISTANCE;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            spawnPos = new BlockPos(x, y, z);
        }

        ScarEntity scar = EntityTypes.SCAR.get().create(level);
        if (scar != null) {
            scar.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0F, 0F);
            scar.setPersistenceRequired();
            level.addFreshEntity(scar);
            level.addFreshEntity(
                    new LightningBoltEntity(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, player));
        }
    }

    private static BlockPos findCaveSpawn(ServerLevel level, BlockPos center) {
        for (int attempt = 0; attempt < SCAR_SEARCH_ATTEMPTS; attempt++) {
            int x = center.getX() + level.random.nextInt(SCAR_SEARCH_RADIUS * 2) - SCAR_SEARCH_RADIUS;
            int z = center.getZ() + level.random.nextInt(SCAR_SEARCH_RADIUS * 2) - SCAR_SEARCH_RADIUS;
            int y = SCAR_MIN_Y + level.random.nextInt(SCAR_MAX_Y - SCAR_MIN_Y);

            BlockPos pos = new BlockPos(x, y, z);
            if (level.getBlockState(pos).isAir()
                    && level.getBlockState(pos.above()).isAir()
                    && level.getBlockState(pos.below()).isSolid()) {
                return pos;
            }
        }
        return null;
    }

    private static void openOutlandsPortal(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();
        RafikiQuestActions.ensureWorldState(level, Stage.COLLECT_TERMITES);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
