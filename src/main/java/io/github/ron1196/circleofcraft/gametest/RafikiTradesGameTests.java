package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.entity.npc.RafikiTrades;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Verifies {@link RafikiTrades} matching, stage-gating, and consumption — the single source of truth
 * for Rafiki's trades. Each test builds its own {@link QuestlineManager} (not the shared world one)
 * so the parallel arenas can't race on quest stage.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class RafikiTradesGameTests {

    private static final String EMPTY = "empty";

    private static QuestlineManager questsAt(GameTestHelper helper, Stage stage) {
        QuestlineManager quests = new QuestlineManager(WorldData.get(helper.getLevel()));
        quests.getState(RafikiQuestline.QUEST_ID).setCurrentStageId(stage.name());
        return quests;
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void silverTradeConsumesExactlyThree(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(ModItems.SILVER_INGOT.get(), 10));

        RafikiTrades.Trade trade = RafikiTrades.tryExecute(player, questsAt(helper, Stage.CRAFT_RAFIKI_STICK));

        if (trade == null) helper.fail("silver trade did not fire");
        if (player.getMainHandItem().getCount() != 7) {
            helper.fail(
                    "expected 7 silver left, got " + player.getMainHandItem().getCount());
        }
        if (!player.getInventory().contains(new ItemStack(ModItems.RAFIKI_COIN.get()))) {
            helper.fail("player did not receive a Rafiki Coin");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void silverTradeRefusedBelowThreshold(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(ModItems.SILVER_INGOT.get(), 2));

        RafikiTrades.Trade trade = RafikiTrades.tryExecute(player, questsAt(helper, Stage.CRAFT_RAFIKI_STICK));

        if (trade != null) helper.fail("silver trade fired with only 2 ingots");
        if (player.getMainHandItem().getCount() != 2) helper.fail("silver was consumed despite no trade");
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void silverTradeRefusedBeforeStageUnlock(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(ModItems.SILVER_INGOT.get(), 3));

        RafikiTrades.Trade trade = RafikiTrades.tryExecute(player, questsAt(helper, Stage.FIND_RAFIKI));

        if (trade != null) helper.fail("silver trade fired before CRAFT_RAFIKI_STICK");
        if (player.getInventory().contains(new ItemStack(ModItems.RAFIKI_COIN.get()))) {
            helper.fail("coin awarded before stage unlock");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void dustTradeIsSymmetric(GameTestHelper helper) {
        Player termiteInHand = helper.makeMockPlayer(GameType.SURVIVAL);
        termiteInHand.getInventory().setItem(0, new ItemStack(ModItems.TERMITE_DUST.get()));
        termiteInHand.getInventory().setItem(1, new ItemStack(ModItems.MANGO_DUST.get()));
        if (RafikiTrades.tryExecute(termiteInHand, questsAt(helper, Stage.USE_STAR_ALTAR)) == null) {
            helper.fail("dust trade did not fire with termite dust in hand");
        }
        if (!termiteInHand.getInventory().contains(new ItemStack(ModItems.RAFIKI_DUST.get()))) {
            helper.fail("no Rafiki Dust from termite-in-hand ordering");
        }

        Player mangoInHand = helper.makeMockPlayer(GameType.SURVIVAL);
        mangoInHand.getInventory().setItem(0, new ItemStack(ModItems.MANGO_DUST.get()));
        mangoInHand.getInventory().setItem(1, new ItemStack(ModItems.TERMITE_DUST.get()));
        if (RafikiTrades.tryExecute(mangoInHand, questsAt(helper, Stage.USE_STAR_ALTAR)) == null) {
            helper.fail("dust trade did not fire with mango dust in hand — symmetric flip broken");
        }
        if (!mangoInHand.getInventory().contains(new ItemStack(ModItems.RAFIKI_DUST.get()))) {
            helper.fail("no Rafiki Dust from mango-in-hand ordering");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void bookTradeIsNotSymmetric(GameTestHelper helper) {
        Player documented = helper.makeMockPlayer(GameType.SURVIVAL);
        documented.getInventory().setItem(0, new ItemStack(Items.BOOK));
        documented.getInventory().setItem(1, new ItemStack(ModItems.LION_FUR.get()));
        if (RafikiTrades.tryExecute(documented, questsAt(helper, Stage.CRAFT_RAFIKI_STICK)) == null) {
            helper.fail("book+fur trade did not fire in the documented direction");
        }

        Player reversed = helper.makeMockPlayer(GameType.SURVIVAL);
        reversed.getInventory().setItem(0, new ItemStack(ModItems.LION_FUR.get()));
        reversed.getInventory().setItem(1, new ItemStack(Items.BOOK));
        if (RafikiTrades.tryExecute(reversed, questsAt(helper, Stage.CRAFT_RAFIKI_STICK)) != null) {
            helper.fail("book trade fired with fur in hand — should be asymmetric");
        }
        helper.succeed();
    }
}
