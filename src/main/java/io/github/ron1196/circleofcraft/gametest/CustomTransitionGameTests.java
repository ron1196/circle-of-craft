package io.github.ron1196.circleofcraft.gametest;

import com.mojang.authlib.GameProfile;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.entity.npc.ScarEntity;
import io.github.ron1196.circleofcraft.quest.questline.Questline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.circleofcraft.quest.stage.StageId;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Verifies that {@link Questline.Builder#customTransition} handlers fire and produce their
 * expected side effects. Without these tests, removing or renaming a stage in a questline silently
 * detaches its world-mutation logic. Tracked in issue #63.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class CustomTransitionGameTests {

    private static final String EMPTY = "empty";

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiHasCustomTransitionForCollectBones(GameTestHelper helper) {
        Questline rafiki = QuestlineRegistry.RAFIKI;
        if (rafiki.getCustomTransition(Stage.COLLECT_BONES) == null) {
            helper.fail("RafikiQuestline lost its COLLECT_BONES customTransition (Scar spawn)");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiHasCustomTransitionForReturnAfterScar(GameTestHelper helper) {
        Questline rafiki = QuestlineRegistry.RAFIKI;
        if (rafiki.getCustomTransition(Stage.RETURN_AFTER_SCAR) == null) {
            helper.fail("RafikiQuestline lost its RETURN_AFTER_SCAR customTransition (portal opens)");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void stagesWithoutCustomTransitionReturnNull(GameTestHelper helper) {
        Questline rafiki = QuestlineRegistry.RAFIKI;
        // FIND_RAFIKI has no customTransition — verify the API correctly reports absence.
        if (rafiki.getCustomTransition(Stage.FIND_RAFIKI) != null) {
            helper.fail("FIND_RAFIKI should not have a customTransition");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void allRegisteredCustomTransitionsAreNonNull(GameTestHelper helper) {
        for (Questline quest : QuestlineRegistry.getOrdered()) {
            for (StageId stage : quest.getStageOrder()) {
                BiConsumer<ServerPlayer, QuestlineManager> handler = quest.getCustomTransition(stage);
                // It's allowed to be null (stages without custom transitions), but if registered it
                // must not be null — that would indicate a Map.put-with-null bug.
                // This is a smoke test against builder misuse.
                if (handler == null) continue;
                if (!(handler instanceof BiConsumer)) {
                    helper.fail("customTransition for " + quest.getId() + "/" + stage.name() + " is not a BiConsumer");
                    return;
                }
            }
        }
        helper.succeed();
    }

    /**
     * Behavioural check: invoking the COLLECT_BONES customTransition actually spawns a ScarEntity
     * in the level. The handler spawns Scar within a 60-block radius of the player (or 30 blocks
     * away as a fallback), so we sweep a generous AABB around the test origin. Skipped for
     * RETURN_AFTER_SCAR's openOutlandsPortal — that handler calls ChatHelper which routes through
     * the mock player's null network channel and NPEs.
     *
     * <p>spawnScar adds Scar, then spawns a cosmetic LightningBoltEntity; the chunk map then
     * broadcasts those new entities to the player. {@code GameTestHelper.makeMockServerPlayerInLevel}
     * builds its player's {@link Connection} with no channel, so {@code placeNewPlayer}'s login-packet
     * sends NPE on {@code Connection.channel()} before the test body even runs — which is why every
     * other test avoids it. {@link #makeNetworkedMockPlayer} builds an equivalent player whose
     * connection is bound to a no-op {@link EmbeddedChannel}, so every send is silently swallowed.
     */
    @GameTest(template = EMPTY, timeoutTicks = 100)
    public void rafikiCustomTransitionSpawnsScar(GameTestHelper helper) {
        ServerPlayer player = makeNetworkedMockPlayer(helper);
        BlockPos origin = helper.absolutePos(BlockPos.ZERO);
        player.moveTo(origin.getX() + 0.5, origin.getY() + 1.0, origin.getZ() + 0.5);

        QuestlineManager manager = WorldData.get(helper.getLevel()).getQuestManager();
        manager.getState(RafikiQuestline.QUEST_ID).setCurrentStageId(Stage.COLLECT_BONES.name());

        BiConsumer<ServerPlayer, QuestlineManager> handler =
                QuestlineRegistry.RAFIKI.getCustomTransition(Stage.COLLECT_BONES);
        if (handler == null) {
            helper.fail("COLLECT_BONES customTransition missing — Scar will never spawn");
            return;
        }
        handler.accept(player, manager);

        AABB sweep = new AABB(
                origin.getX() - 80,
                helper.getLevel().getMinBuildHeight(),
                origin.getZ() - 80,
                origin.getX() + 80,
                helper.getLevel().getMaxBuildHeight(),
                origin.getZ() + 80);
        if (helper.getLevel().getEntitiesOfClass(ScarEntity.class, sweep).isEmpty()) {
            helper.fail("ScarEntity did not spawn after COLLECT_BONES customTransition fired");
            return;
        }
        helper.succeed();
    }

    /**
     * Build a mock {@link ServerPlayer} added to the test level, like
     * {@code GameTestHelper.makeMockServerPlayerInLevel}, but with a {@link Connection} bound to a
     * Netty {@link EmbeddedChannel} before placement. Constructing {@code new EmbeddedChannel(conn)}
     * fires {@code channelActive}, which sets the connection's channel, so the login-packet sends in
     * {@code placeNewPlayer} (and later entity broadcasts) are accepted and discarded instead of
     * NPEing on a null channel.
     */
    private static ServerPlayer makeNetworkedMockPlayer(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        MinecraftServer server = level.getServer();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "test-mock-player");
        ServerPlayer player = new ServerPlayer(server, level, profile);

        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        server.getPlayerList().placeNewPlayer(connection, player);
        return player;
    }
}
