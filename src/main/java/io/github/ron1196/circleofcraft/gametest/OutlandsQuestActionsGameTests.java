package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.ZiraEntity;
import io.github.ron1196.circleofcraft.quest.actions.OutlandsQuestActions;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline.Stage;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Locks down {@link OutlandsQuestActions#ensureWorldState} so future stage additions or
 * refactors can't silently drop a branch — and pins the {@code TREE_OCCUPATION_STAGES}
 * membership (CLAUDE.md flags it as the known footgun: forgetting to add a new stage causes
 * Rafiki to spawn prematurely or Zira's tree corruption to toggle incorrectly).
 *
 * <p>Tracked in issue #63 (test-coverage audit).
 *
 * <p>Stages whose effects are gated on {@code level.players()} ({@code POOL_OPEN_STAGES},
 * {@code FOLLOW_OUTLANDERS} → near-player Outlander discard, {@code TREE_OCCUPATION_STAGES},
 * {@code RAFIKI_RESTORED_STAGES}) are not covered here: {@code helper.makeMockServerPlayerInLevel}
 * produces a player with a null network channel, so any subsequent block/entity broadcast NPEs.
 * Those branches need a real client connection to test end-to-end and belong in a higher-level
 * integration scenario.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class OutlandsQuestActionsGameTests {

    private static final String EMPTY = "empty";

    /** Stages that must report true from {@link OutlandsQuestActions#isTreeOccupationStage}. */
    private static final Set<Stage> EXPECTED_TREE_OCCUPATION_STAGES = EnumSet.of(
            Stage.ZIRA_OCCUPIES_TREE,
            Stage.TALK_TO_PUMBAA,
            Stage.GATHER_PUMBAA_INGREDIENTS,
            Stage.USE_PUMBAA_BOX,
            Stage.PUMBAA_BOX_EXPLODING);

    // ── TREE_OCCUPATION_STAGES membership lock ──────────────────────────────

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void treeOccupationStagesMembershipIsExact(GameTestHelper helper) {
        // For every Stage, isTreeOccupationStage must agree with EXPECTED_TREE_OCCUPATION_STAGES.
        // A mismatch means a stage was added/moved without updating the EnumSet — exactly the
        // CLAUDE.md-documented footgun.
        for (Stage stage : Stage.values()) {
            boolean expected = EXPECTED_TREE_OCCUPATION_STAGES.contains(stage);
            boolean actual = OutlandsQuestActions.isTreeOccupationStage(stage);
            if (expected != actual) {
                helper.fail("TREE_OCCUPATION_STAGES membership mismatch for " + stage
                        + ": expected " + expected + ", got " + actual
                        + ". If you added a stage, update TREE_OCCUPATION_STAGES in OutlandsQuestActions.");
                return;
            }
        }
        helper.succeed();
    }

    // ── POOL_COVER_STAGES: no-op ────────────────────────────────────────────

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void poolCoverStagesAreNoOp(GameTestHelper helper) {
        // Place a pool cover; calling ensureWorldState(ENTER_OUTLANDS) must NOT destroy it.
        BlockPos coverPos = new BlockPos(2, 2, 2);
        helper.setBlock(coverPos, ModBlocks.POOL_COVER.get().defaultBlockState());

        OutlandsQuestActions.ensureWorldState(helper.getLevel(), Stage.ENTER_OUTLANDS);

        if (!helper.getBlockState(coverPos).is(ModBlocks.POOL_COVER.get())) {
            helper.fail("ENTER_OUTLANDS must not destroy pool covers");
            return;
        }
        helper.succeed();
    }

    // ── FOLLOW_OUTLANDERS: discard non-hostile Zira (the player-free branch) ─

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void followOutlandersDiscardsNonHostileZira(GameTestHelper helper) {
        spawnNonHostileZiraAt(helper, new BlockPos(2, 2, 2));

        OutlandsQuestActions.ensureWorldState(helper.getLevel(), Stage.FOLLOW_OUTLANDERS);

        if (countNonHostileZiraInArena(helper) != 0) {
            helper.fail("FOLLOW_OUTLANDERS must discard non-hostile Zira");
            return;
        }
        helper.succeed();
    }

    // ── Smoke test across every stage ───────────────────────────────────────

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void everyStageHandlesEnsureWorldStateWithoutThrowing(GameTestHelper helper) {
        // Same rationale as the Rafiki version: no mock player, so player-iterating branches
        // are no-ops; this purely catches NPE / missing-branch regressions.
        for (Stage stage : Stage.values()) {
            try {
                OutlandsQuestActions.ensureWorldState(helper.getLevel(), stage);
            } catch (Exception e) {
                helper.fail("ensureWorldState threw for stage " + stage + ": " + e);
                return;
            }
        }
        helper.succeed();
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private static void spawnNonHostileZiraAt(GameTestHelper helper, BlockPos relative) {
        ZiraEntity zira = EntityTypes.ZIRA.get().create(helper.getLevel());
        if (zira == null) {
            helper.fail("Failed to create ZiraEntity");
            return;
        }
        BlockPos absolute = helper.absolutePos(relative);
        zira.moveTo(absolute.getX() + 0.5, absolute.getY(), absolute.getZ() + 0.5, 0F, 0F);
        zira.setHostile(false);
        zira.setPersistenceRequired();
        helper.getLevel().addFreshEntity(zira);
    }

    private static int countNonHostileZiraInArena(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        AABB arena = new AABB(helper.absolutePos(BlockPos.ZERO)).inflate(100);
        List<ZiraEntity> ziras = level.getEntitiesOfClass(ZiraEntity.class, arena, e -> e.isAlive() && !e.isHostile());
        return ziras.size();
    }
}
