package io.github.ron1196.circleofcraft.quest.questline;

import static io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage.*;
import static io.github.ron1196.circleofcraft.quest.stage.QuestObjective.ItemRequirement;
import static io.github.ron1196.circleofcraft.quest.stage.QuestObjective.Source;
import static io.github.ron1196.circleofcraft.quest.stage.QuestTrigger.*;

import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import io.github.ron1196.circleofcraft.entity.projectile.LightningBoltEntity;
import io.github.ron1196.circleofcraft.quest.actions.RafikiQuestActions;
import io.github.ron1196.circleofcraft.quest.stage.ClaimableReward;
import io.github.ron1196.circleofcraft.quest.stage.QuestObjective;
import io.github.ron1196.circleofcraft.quest.stage.StageId;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.util.ChatHelper;
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

    public static final String QUEST_ID = "rafiki";

    public enum Stage implements StageId {
        FIND_RAFIKI,
        CRAFT_RAFIKI_STICK,
        RALLY_PUMBAA,
        COLLECT_BUGS,
        RETURN_TO_RAFIKI,
        COLLECT_BONES,
        DEFEAT_SCAR,
        RETURN_AFTER_SCAR,
        COLLECT_TERMITES,
        COLLECT_MANGOES,
        LION_DUST_CEREMONY,
        USE_STAR_ALTAR,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder(QUEST_ID)
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(ModItems.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new QuestObjective("Find Rafiki and speak to him"))
                .stage(
                        CRAFT_RAFIKI_STICK,
                        new QuestObjective(
                                "Bring Rafiki a stick, a mango, and a bug",
                                List.of(
                                        new ItemRequirement(() -> Items.STICK, 1, Source.INVENTORY),
                                        new ItemRequirement(ModItems.MANGO, 1, Source.INVENTORY),
                                        new ItemRequirement(ModItems.BUG, 1, Source.INVENTORY))))
                .stage(RALLY_PUMBAA, new QuestObjective("Find Timon and Pumbaa"))
                .stage(
                        COLLECT_BUGS,
                        new QuestObjective(
                                "Bring Pumbaa 4 bugs", List.of(new ItemRequirement(ModItems.BUG, 4, Source.INVENTORY))))
                .stage(RETURN_TO_RAFIKI, new QuestObjective("Return to Rafiki"))
                .stage(
                        COLLECT_BONES,
                        new QuestObjective(
                                "Bring Rafiki 64 hyena bones", List.of(new ItemRequirement(ModItems.HYENA_BONE, 64))))
                .stage(DEFEAT_SCAR, new QuestObjective("Defeat Scar using the Rafiki Stick"))
                .stage(RETURN_AFTER_SCAR, new QuestObjective("Return to Rafiki"))
                .stage(
                        COLLECT_TERMITES,
                        new QuestObjective(
                                "Bring Rafiki 4 termite dust", List.of(new ItemRequirement(ModItems.TERMITE_DUST, 4))))
                .stage(
                        COLLECT_MANGOES,
                        new QuestObjective(
                                "Bring Rafiki 4 mango dust", List.of(new ItemRequirement(ModItems.MANGO_DUST, 4))))
                .stage(LION_DUST_CEREMONY, new QuestObjective("Watch Rafiki perform the Lion Dust ceremony"))
                .stage(USE_STAR_ALTAR, new QuestObjective("Craft a Star Altar and use Rafiki Dust on it"))
                .stage(RafikiQuestline.Stage.COMPLETE, new QuestObjective("Quest complete"))
                .claimableReward(CRAFT_RAFIKI_STICK, new ClaimableReward(ModItems.RAFIKI_STICK, 1))
                .trigger(FIND_RAFIKI, RAFIKI_TALK)
                .trigger(CRAFT_RAFIKI_STICK, RAFIKI_TALK)
                .trigger(RALLY_PUMBAA, TIMON_TALK)
                .trigger(COLLECT_BUGS, PUMBAA_TALK)
                .trigger(RETURN_TO_RAFIKI, RAFIKI_TALK)
                .trigger(COLLECT_BONES, RAFIKI_TALK)
                .trigger(DEFEAT_SCAR, SCAR_KILLED)
                .trigger(RETURN_AFTER_SCAR, RAFIKI_TALK)
                .trigger(COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(COLLECT_MANGOES, RAFIKI_TALK)
                // LION_DUST_CEREMONY has no trigger — intercepted in RafikiEntity for timed dialogue
                .trigger(USE_STAR_ALTAR, STAR_ALTAR_USED)
                .customTransition(COLLECT_BONES, RafikiQuestline::spawnScar)
                .customTransition(RETURN_AFTER_SCAR, RafikiQuestline::openOutlandsPortal)
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

        ChatHelper.broadcastNpcMessage(level, "Rafiki", "Asante sana, squash banana, wewe nugu, mimi hapana...");

        // Cosmetic explosion at the portal location + break gates + light portal
        RafikiQuestActions.ensureWorldState(level, Stage.COLLECT_TERMITES);
        level.playSound(
                null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);

        ChatHelper.broadcastNpcMessage(
                level,
                "Rafiki",
                "Dis portal will take you to de Outlands. I want you to go dere, collect four termites, and grind dem up!");
    }
}
