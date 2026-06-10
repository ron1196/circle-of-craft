package io.github.ron1196.circleofcraft.gametest;

import com.mojang.authlib.GameProfile;
import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.entity.animal.LionEntity;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Regression guard for the Kivulite fire-tool auto-smelt (see {@code FireToolHelper}) and the lion
 * diet tag fix. Outcome-only assertions so this file cherry-picks cleanly to other MC versions.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class KivuliteToolGameTests {

    private static final String EMPTY = "empty";
    private static final BlockPos ORE_POS = new BlockPos(2, 2, 2);
    private static final BlockPos ARENA_MIN = new BlockPos(0, 0, 0);
    private static final BlockPos ARENA_MAX = new BlockPos(4, 4, 4);

    private static List<ItemEntity> arenaDrops(GameTestHelper helper) {
        AABB bounds = AABB.encapsulatingFullBlocks(helper.absolutePos(ARENA_MIN), helper.absolutePos(ARENA_MAX));
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, bounds);
    }

    private static void breakBlockAsPlayer(GameTestHelper helper, ItemStack tool) {
        BlockPos abs = helper.absolutePos(ORE_POS);
        GameProfile profile = new GameProfile(UUID.randomUUID(), "coc_gametest_miner");
        FakePlayer player = FakePlayerFactory.get(helper.getLevel(), profile);
        player.setGameMode(GameType.SURVIVAL);
        player.setPos(abs.getX() + 0.5, abs.getY(), abs.getZ() + 0.5);
        player.setItemInHand(InteractionHand.MAIN_HAND, tool);
        player.gameMode.destroyBlock(abs);
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void kivulitePickaxeAutoSmeltsOreDrop(GameTestHelper helper) {
        helper.setBlock(ORE_POS, Blocks.IRON_ORE);
        breakBlockAsPlayer(helper, new ItemStack(ModItems.KIVULITE_PICKAXE.get()));

        List<ItemEntity> drops = arenaDrops(helper);
        boolean hasIngot = drops.stream().anyMatch(e -> e.getItem().is(Items.IRON_INGOT));
        boolean hasRaw = drops.stream().anyMatch(e -> e.getItem().is(Items.RAW_IRON));
        if (!hasIngot) {
            helper.fail("Kivulite pickaxe should auto-smelt the ore drop to iron_ingot, drops=" + drops);
        }
        if (hasRaw) {
            helper.fail("raw_iron should have been replaced by iron_ingot, drops=" + drops);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vanillaPickaxeDoesNotAutoSmelt(GameTestHelper helper) {
        helper.setBlock(ORE_POS, Blocks.IRON_ORE);
        breakBlockAsPlayer(helper, new ItemStack(Items.IRON_PICKAXE));

        List<ItemEntity> drops = arenaDrops(helper);
        boolean hasRaw = drops.stream().anyMatch(e -> e.getItem().is(Items.RAW_IRON));
        boolean hasIngot = drops.stream().anyMatch(e -> e.getItem().is(Items.IRON_INGOT));
        if (!hasRaw) {
            helper.fail("a vanilla iron pickaxe should drop raw_iron, drops=" + drops);
        }
        if (hasIngot) {
            helper.fail("a vanilla pickaxe must not smelt the drop, drops=" + drops);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void kivuliteSwordCooksMobDrop(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.KIVULITE_SWORD.get()));

        Cow cow = helper.spawn(net.minecraft.world.entity.EntityType.COW, new BlockPos(2, 2, 2));
        cow.hurt(helper.getLevel().damageSources().playerAttack(player), 1000f);

        if (cow.isAlive()) {
            helper.fail("cow should be dead after a 1000-damage player attack");
        }
        List<ItemEntity> drops = arenaDrops(helper);
        boolean hasCooked = drops.stream().anyMatch(e -> e.getItem().is(Items.COOKED_BEEF));
        boolean hasRaw = drops.stream().anyMatch(e -> e.getItem().is(Items.BEEF));
        if (!hasCooked) {
            helper.fail("Kivulite sword should cook the cow's beef drop, drops=" + drops);
        }
        if (hasRaw) {
            helper.fail("raw beef should have been replaced by cooked_beef, drops=" + drops);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void lionEatsVanillaAndModMeatButNotCarrot(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), new BlockPos(2, 2, 2));

        if (!lion.isFood(new ItemStack(Items.BEEF))) {
            helper.fail("lion should accept vanilla meat (beef)");
        }
        if (!lion.isFood(new ItemStack(ModItems.ZEBRA_COOKED.get()))) {
            helper.fail("lion should accept mod meat (zebra_cooked) — must be in #minecraft:meat");
        }
        if (lion.isFood(new ItemStack(Items.CARROT))) {
            helper.fail("lion should not accept a carrot");
        }
        helper.succeed();
    }
}
