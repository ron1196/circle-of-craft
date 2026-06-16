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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Regression guard for the Kivulite fire-tool block auto-smelt ({@code FireToolHelper}) and the lion
 * diet. Forge 1.20.1 port of the mc/1.21.1 tests.
 *
 * <p>Two divergences from the 1.21 version, both deliberate: 1.20.1 smelts the <em>block</em> item
 * (so it auto-smelts cobblestone→stone, but not ores — that drop-smelting fix is 1.21-only), and
 * mob-drop cooking does not exist on 1.20.1 (added on 1.21), so that test is omitted here.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class KivuliteToolGameTests {

    private static final String EMPTY = "empty";
    private static final BlockPos BLOCK_POS = new BlockPos(2, 2, 2);
    private static final BlockPos ARENA_MIN = new BlockPos(0, 0, 0);
    private static final BlockPos ARENA_MAX = new BlockPos(4, 4, 4);

    private static List<ItemEntity> arenaDrops(GameTestHelper helper) {
        BlockPos min = helper.absolutePos(ARENA_MIN);
        BlockPos max = helper.absolutePos(ARENA_MAX);
        AABB bounds = new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, bounds);
    }

    private static void breakBlockAsPlayer(GameTestHelper helper, ItemStack tool) {
        BlockPos abs = helper.absolutePos(BLOCK_POS);
        FakePlayer player = FakePlayerFactory.get(helper.getLevel(), new GameProfile(UUID.randomUUID(), "coc_miner"));
        player.setGameMode(GameType.SURVIVAL);
        player.setPos(abs.getX() + 0.5, abs.getY(), abs.getZ() + 0.5);
        player.setItemInHand(InteractionHand.MAIN_HAND, tool);
        player.gameMode.destroyBlock(abs);
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void kivulitePickaxeAutoSmeltsBlockDrop(GameTestHelper helper) {
        helper.setBlock(BLOCK_POS, Blocks.COBBLESTONE);
        breakBlockAsPlayer(helper, new ItemStack(ModItems.KIVULITE_PICKAXE.get()));

        List<ItemEntity> drops = arenaDrops(helper);
        if (drops.stream().noneMatch(e -> e.getItem().is(Items.STONE))) {
            helper.fail("Kivulite pickaxe should auto-smelt cobblestone to stone, drops=" + drops);
        }
        if (drops.stream().anyMatch(e -> e.getItem().is(Items.COBBLESTONE))) {
            helper.fail("cobblestone should have been replaced by stone, drops=" + drops);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vanillaPickaxeDoesNotAutoSmelt(GameTestHelper helper) {
        helper.setBlock(BLOCK_POS, Blocks.COBBLESTONE);
        breakBlockAsPlayer(helper, new ItemStack(Items.IRON_PICKAXE));

        List<ItemEntity> drops = arenaDrops(helper);
        if (drops.stream().noneMatch(e -> e.getItem().is(Items.COBBLESTONE))) {
            helper.fail("a vanilla iron pickaxe should drop cobblestone, drops=" + drops);
        }
        if (drops.stream().anyMatch(e -> e.getItem().is(Items.STONE))) {
            helper.fail("a vanilla pickaxe must not smelt the drop, drops=" + drops);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void lionEatsVanillaAndModMeatButNotCarrot(GameTestHelper helper) {
        LionEntity lion = helper.spawn(EntityTypes.LION.get(), BLOCK_POS);

        if (!lion.isFood(new ItemStack(Items.BEEF))) {
            helper.fail("lion should accept vanilla meat (beef)");
        }
        if (!lion.isFood(new ItemStack(ModItems.ZEBRA_COOKED.get()))) {
            helper.fail("lion should accept mod meat (zebra_cooked)");
        }
        if (lion.isFood(new ItemStack(Items.CARROT))) {
            helper.fail("lion should not accept a carrot");
        }
        helper.succeed();
    }
}
