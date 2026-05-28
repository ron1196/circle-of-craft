package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Pins the deliberate "coin is inert outside its target dimension" rule. The game-test server runs
 * in the overworld, so a Rafiki Coin (target = Pride Lands) must hard-fail without being consumed.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class CoinGameTests {

    private static final String EMPTY = "empty";

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void rafikiCoinInertOutsidePrideLands(GameTestHelper helper) {
        Player player = helper.makeMockSurvivalPlayer();
        player.getInventory().setItem(0, new ItemStack(ModItems.RAFIKI_COIN.get(), 1));

        InteractionResultHolder<ItemStack> result =
                ModItems.RAFIKI_COIN.get().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        if (result.getResult() != InteractionResult.FAIL) {
            helper.fail("coin should be inert in the overworld, got " + result.getResult());
        }
        if (player.getMainHandItem().getCount() != 1) {
            helper.fail("coin was consumed outside its target dimension");
        }
        helper.succeed();
    }
}
