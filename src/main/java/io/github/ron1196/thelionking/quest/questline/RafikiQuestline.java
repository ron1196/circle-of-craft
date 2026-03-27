package io.github.ron1196.thelionking.quest.questline;

import static io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage.*;
import static io.github.ron1196.thelionking.quest.stage.Stage.ItemRequirement;
import static io.github.ron1196.thelionking.quest.stage.StageTrigger.*;

import io.github.ron1196.thelionking.block.PortalBlock;
import io.github.ron1196.thelionking.quest.stage.ClaimableReward;
import io.github.ron1196.thelionking.quest.stage.IStageId;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class RafikiQuestline {

    private static final int GATE_SEARCH_RADIUS = 20;

    public enum Stage implements IStageId {
        FIND_RAFIKI,
        COLLECT_BONES,
        DEFEAT_SCAR,
        RETURN_AFTER_SCAR,
        COLLECT_TERMITES,
        COLLECT_MANGOES,
        USE_STAR_ALTAR,
        COMPLETE
    }

    public static Questline build() {
        return Questline.builder("rafiki")
                .displayName("Rafiki's Quest")
                .icon(() -> new ItemStack(LionKingItems.RAFIKI_STICK.get()))
                .stage(FIND_RAFIKI, new io.github.ron1196.thelionking.quest.stage.Stage("Find Rafiki and speak to him"))
                .stage(
                        COLLECT_BONES,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Rafiki 64 hyena bones",
                                List.of(new ItemRequirement(LionKingItems.HYENA_BONE, 64))))
                .stage(DEFEAT_SCAR, new io.github.ron1196.thelionking.quest.stage.Stage("Defeat Scar"))
                .stage(RETURN_AFTER_SCAR, new io.github.ron1196.thelionking.quest.stage.Stage("Return to Rafiki"))
                .stage(
                        COLLECT_TERMITES,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Rafiki 4 termite dust",
                                List.of(new ItemRequirement(LionKingItems.TERMITE_DUST, 4))))
                .stage(
                        COLLECT_MANGOES,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Bring Rafiki 4 mango dust", List.of(new ItemRequirement(LionKingItems.MANGO_DUST, 4))))
                .stage(
                        USE_STAR_ALTAR,
                        new io.github.ron1196.thelionking.quest.stage.Stage(
                                "Craft a Star Altar and use Rafiki Dust on it"))
                .stage(
                        RafikiQuestline.Stage.COMPLETE,
                        new io.github.ron1196.thelionking.quest.stage.Stage("Quest complete"))
                .claimableReward(COLLECT_BONES, new ClaimableReward(LionKingItems.RAFIKI_STICK, 1))
                .trigger(FIND_RAFIKI, RAFIKI_TALK)
                .trigger(COLLECT_BONES, RAFIKI_TALK)
                .trigger(DEFEAT_SCAR, SCAR_KILLED)
                .trigger(RETURN_AFTER_SCAR, RAFIKI_TALK)
                .trigger(COLLECT_TERMITES, RAFIKI_TALK)
                .trigger(COLLECT_MANGOES, RAFIKI_TALK)
                .trigger(USE_STAR_ALTAR, STAR_ALTAR_USED)
                .customTransition(RETURN_AFTER_SCAR, RafikiQuestline::openOutlandsPortal)
                .build();
    }

    /**
     * When the player returns to Rafiki after defeating Scar, Rafiki opens the gate
     * sealing the Outlands portal in his tree and activates the portal.
     */
    private static void openOutlandsPortal(ServerPlayer player, QuestlineManager manager) {
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();
        Block gateBlock = LionKingBlocks.ZIRA_MOUND_GATE.get();

        // Find the first gate block near the player (inside Rafiki's tree)
        BlockPos gatePos = findNearbyBlock(level, playerPos, gateBlock, GATE_SEARCH_RADIUS);
        if (gatePos == null) return;

        // Chain-break all connected gate blocks
        breakGateChain(level, gatePos, gateBlock);

        // Play explosion sound effect
        level.playSound(null, gatePos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);

        // Try to activate the outlands portal by testing every air block near the gate
        activateNearbyPortal(level, gatePos);
    }

    private static BlockPos findNearbyBlock(Level level, BlockPos center, Block target, int radius) {
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            if (level.getBlockState(pos).is(target)) {
                return pos.immutable();
            }
        }
        return null;
    }

    private static void breakGateChain(Level level, BlockPos pos, Block gateBlock) {
        if (!level.getBlockState(pos).is(gateBlock)) return;

        level.destroyBlock(pos, false);

        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            breakGateChain(level, pos.relative(dir), gateBlock);
        }
    }

    private static void activateNearbyPortal(ServerLevel level, BlockPos center) {
        PortalBlock portalBlock = (PortalBlock) LionKingBlocks.OUTLANDS_PORTAL.get();
        int radius = 10;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            if (level.getBlockState(pos).isAir()) {
                if (portalBlock.trySpawnPortal(level, pos)) {
                    level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return;
                }
            }
        }
    }
}
