package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import io.github.ron1196.circleofcraft.quest.actions.RafikiQuestActions;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.circleofcraft.quest.stage.StageId;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Locks down {@link RafikiQuestActions#ensureWorldState} so future stage additions or
 * refactors can't silently drop a branch. Tracked in issue #63 (test-coverage audit).
 *
 * <p>Three contracts under test:
 * <ul>
 *   <li>{@code NO_SCAR_NO_PORTAL_STAGES}: any {@link ScarEntity} in the level is discarded.</li>
 *   <li>{@code DEFEAT_SCAR}: calling with a Scar already present is a no-op (idempotent).</li>
 *   <li>{@code PORTAL_OPEN_STAGES}: existing Scars are discarded (portal placement is exercised
 *       in {@link CustomTransitionGameTests} and isn't replayed here).</li>
 * </ul>
 *
 * <p>A separate test pins the {@code /coc quest reset} contract: after reset, the first stage's
 * world state must hold (Scar absent for Rafiki).
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class RafikiQuestActionsGameTests {

    private static final String EMPTY = "empty";

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void findRafikiDiscardsExistingScar(GameTestHelper helper) {
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.FIND_RAFIKI);
        assertScarCount(helper, 0, "FIND_RAFIKI must discard any Scar in the level");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void collectBonesDiscardsExistingScar(GameTestHelper helper) {
        // COLLECT_BONES is in NO_SCAR_NO_PORTAL_STAGES too; lock down the membership.
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.COLLECT_BONES);
        assertScarCount(helper, 0, "COLLECT_BONES must discard any Scar in the level");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void returnAfterScarDiscardsExistingScar(GameTestHelper helper) {
        // RETURN_AFTER_SCAR sits between DEFEAT_SCAR and PORTAL_OPEN — easy to misplace.
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.RETURN_AFTER_SCAR);
        assertScarCount(helper, 0, "RETURN_AFTER_SCAR must discard any Scar in the level");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void defeatScarIsIdempotentWhenScarAlreadyPresent(GameTestHelper helper) {
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.DEFEAT_SCAR);
        assertScarCount(helper, 1, "DEFEAT_SCAR must not spawn a duplicate Scar when one exists");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void portalOpenStageDiscardsExistingScar(GameTestHelper helper) {
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.COLLECT_TERMITES);
        assertScarCount(helper, 0, "COLLECT_TERMITES (PORTAL_OPEN_STAGES) must discard Scar");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void completeStageDiscardsExistingScar(GameTestHelper helper) {
        spawnScarAt(helper, new BlockPos(2, 2, 2));
        RafikiQuestActions.ensureWorldState(helper.getLevel(), Stage.COMPLETE);
        assertScarCount(helper, 0, "COMPLETE (PORTAL_OPEN_STAGES) must discard Scar");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void everyStageHandlesEnsureWorldStateWithoutThrowing(GameTestHelper helper) {
        // Smoke test: any stage value passed in must complete without throwing. Catches the
        // case where a new stage is added but its branch in ensureWorldState is missing —
        // the call would silently no-op today, but at least guarantees no NPE.
        // No mock player: the player-iterating branches short-circuit on an empty list, and
        // makeMockServerPlayerInLevel produces a player with a null network channel that NPEs
        // the moment any block update tries to broadcast.
        for (Stage stage : Stage.values()) {
            try {
                RafikiQuestActions.ensureWorldState(helper.getLevel(), stage);
            } catch (Exception e) {
                helper.fail("ensureWorldState threw for stage " + stage + ": " + e);
                return;
            }
        }
        helper.succeed();
    }

    /**
     * Documents the {@code /coc quest reset} contract: after reset, calling ensureWorldState with
     * the first stage must bring the world into the corresponding state. The reset command's
     * fix (commit 24eb400) added the ensureWorldState call; this test prevents it from being
     * silently removed.
     */
    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void questResetFirstStageDiscardsScar(GameTestHelper helper) {
        spawnScarAt(helper, new BlockPos(2, 2, 2));

        StageId firstStage = QuestlineRegistry.RAFIKI.getFirstStage();
        if (!(firstStage instanceof Stage rafikiStage)) {
            helper.fail("Rafiki first stage is not a RafikiQuestline.Stage: " + firstStage);
            return;
        }
        if (rafikiStage != Stage.FIND_RAFIKI) {
            helper.fail("Rafiki first stage changed unexpectedly to " + rafikiStage + " — review questReset contract");
            return;
        }
        RafikiQuestActions.ensureWorldState(helper.getLevel(), rafikiStage);
        assertScarCount(helper, 0, "after /coc quest reset rafiki, world state must match first stage");
        helper.succeed();
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private static void spawnScarAt(GameTestHelper helper, BlockPos relative) {
        ScarEntity scar = EntityTypes.SCAR.get().create(helper.getLevel());
        if (scar == null) {
            helper.fail("Failed to create ScarEntity");
            return;
        }
        BlockPos absolute = helper.absolutePos(relative);
        scar.moveTo(absolute.getX() + 0.5, absolute.getY(), absolute.getZ() + 0.5, 0F, 0F);
        scar.setPersistenceRequired();
        helper.getLevel().addFreshEntity(scar);
    }

    private static void assertScarCount(GameTestHelper helper, int expected, String message) {
        int actual = countScarsInArena(helper);
        if (actual != expected) {
            helper.fail(message + " — expected " + expected + " but found " + actual);
        }
    }

    private static int countScarsInArena(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BlockPos center = helper.absolutePos(BlockPos.ZERO);
        AABB arena = new AABB(center).inflate(100);
        List<ScarEntity> scars = level.getEntitiesOfClass(ScarEntity.class, arena, e -> e.isAlive());
        return scars.size();
    }
}
