package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;

@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class VaseGameTests {

    private static final String EMPTY = "empty";
    private static final BlockPos VASE_POS = new BlockPos(2, 2, 2);
    private static final BlockPos ARENA_MIN = new BlockPos(0, 0, 0);
    private static final BlockPos ARENA_MAX = new BlockPos(4, 4, 4);

    private static BlockHitResult hitAt(GameTestHelper helper, BlockPos relativePos) {
        BlockPos abs = helper.absolutePos(relativePos);
        return new BlockHitResult(Vec3.atCenterOf(abs), Direction.UP, abs, false);
    }

    private static Player playerHolding(GameTestHelper helper, ItemStack stack) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        return player;
    }

    private static int countInInventory(Player player, Item item) {
        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(item)) total += s.getCount();
        }
        return total;
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vaseFillConsumesPlant(GameTestHelper helper) {
        helper.setBlock(VASE_POS, ModBlocks.VASE.get());
        BlockPos abs = helper.absolutePos(VASE_POS);
        ItemStack stack = new ItemStack(ModItems.PASSION_SAPLING_ITEM.get(), 4);
        Player player = playerHolding(helper, stack);

        helper.getLevel()
                .getBlockState(abs)
                .useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitAt(helper, VASE_POS));

        BlockState after = helper.getLevel().getBlockState(abs);
        if (!after.is(ModBlocks.VASE_PASSION.get())) {
            helper.fail("expected vase_passion, got " + after.getBlock());
        }
        if (stack.getCount() != 3) {
            helper.fail("expected stack count 3 (1 consumed of 4), got " + stack.getCount());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vaseExtractReturnsPlant(GameTestHelper helper) {
        helper.setBlock(VASE_POS, ModBlocks.VASE_ACACIA.get());
        BlockPos abs = helper.absolutePos(VASE_POS);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        helper.getLevel().getBlockState(abs).useWithoutItem(helper.getLevel(), player, hitAt(helper, VASE_POS));

        BlockState after = helper.getLevel().getBlockState(abs);
        if (!after.is(ModBlocks.VASE.get())) {
            helper.fail("expected empty vase after extraction, got " + after.getBlock());
        }
        int count = countInInventory(player, ModItems.ACACIA_SAPLING_ITEM.get());
        if (count < 1) {
            helper.fail("player inventory should have pride_acacia_sapling, found " + count);
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vaseCreativeDoesNotConsume(GameTestHelper helper) {
        helper.setBlock(VASE_POS, ModBlocks.VASE.get());
        BlockPos abs = helper.absolutePos(VASE_POS);
        ItemStack stack = new ItemStack(ModItems.WHITE_FLOWER_ITEM.get(), 8);
        Player player = playerHolding(helper, stack);
        player.getAbilities().instabuild = true;

        helper.getLevel()
                .getBlockState(abs)
                .useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitAt(helper, VASE_POS));

        BlockState after = helper.getLevel().getBlockState(abs);
        if (!after.is(ModBlocks.VASE_WHITE_FLOWER.get())) {
            helper.fail("creative fill should still succeed, got " + after.getBlock());
        }
        if (stack.getCount() != 8) {
            helper.fail("creative player should not lose a plant, count = " + stack.getCount());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vaseGlowingEmitsCorrectLight(GameTestHelper helper) {
        BlockPos abs = helper.absolutePos(VASE_POS);

        helper.setBlock(VASE_POS, ModBlocks.VASE_OUTSHROOM_GLOWING.get());
        int glowing = helper.getLevel().getBlockState(abs).getLightEmission();
        if (glowing != 13) {
            helper.fail("vase_outshroom_glowing should emit 13, got " + glowing);
        }

        helper.setBlock(VASE_POS, ModBlocks.VASE_PASSION.get());
        int passion = helper.getLevel().getBlockState(abs).getLightEmission();
        if (passion != 11) {
            helper.fail("vase_passion should emit 11, got " + passion);
        }

        helper.setBlock(VASE_POS, ModBlocks.VASE_ACACIA.get());
        int acacia = helper.getLevel().getBlockState(abs).getLightEmission();
        if (acacia != 0) {
            helper.fail("vase_acacia should emit 0, got " + acacia);
        }

        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void filledVaseBreakDropsBoth(GameTestHelper helper) {
        helper.setBlock(VASE_POS, ModBlocks.VASE_RED_FLOWER.get());
        BlockPos abs = helper.absolutePos(VASE_POS);
        BlockState state = helper.getLevel().getBlockState(abs);

        Block.dropResources(state, helper.getLevel(), abs);

        BlockPos minAbs = helper.absolutePos(ARENA_MIN);
        BlockPos maxAbs = helper.absolutePos(ARENA_MAX);
        AABB bounds = AABB.encapsulatingFullBlocks(minAbs, maxAbs);
        List<ItemEntity> drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, bounds);

        boolean hasVase = drops.stream().anyMatch(e -> e.getItem().is(ModItems.VASE_ITEM.get()));
        boolean hasFlower = drops.stream().anyMatch(e -> e.getItem().is(ModItems.RED_FLOWER_ITEM.get()));

        if (!hasVase) {
            helper.fail("expected drops to include empty vase");
        }
        if (!hasFlower) {
            helper.fail("expected drops to include red flower");
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void vaseWrongItemIsNoOp(GameTestHelper helper) {
        helper.setBlock(VASE_POS, ModBlocks.VASE.get());
        BlockPos abs = helper.absolutePos(VASE_POS);
        ItemStack stack = new ItemStack(Items.DIRT, 2);
        Player player = playerHolding(helper, stack);

        helper.getLevel()
                .getBlockState(abs)
                .useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitAt(helper, VASE_POS));

        BlockState after = helper.getLevel().getBlockState(abs);
        if (!after.is(ModBlocks.VASE.get())) {
            helper.fail("dirt should not transform the vase, got " + after.getBlock());
        }
        if (stack.getCount() != 2) {
            helper.fail("dirt count should be unchanged, got " + stack.getCount());
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 100)
    public void allElevenContentsResolve(GameTestHelper helper) {
        record Pair(DeferredHolder<Item, ? extends Item> plant, DeferredHolder<Block, Block> filledVase, String name) {}

        List<Pair> pairs = List.of(
                new Pair(ModItems.ACACIA_SAPLING_ITEM, ModBlocks.VASE_ACACIA, "acacia"),
                new Pair(ModItems.RAINFOREST_SAPLING_ITEM, ModBlocks.VASE_RAINFOREST, "rainforest"),
                new Pair(ModItems.MANGO_SAPLING_ITEM, ModBlocks.VASE_MANGO, "mango"),
                new Pair(ModItems.PASSION_SAPLING_ITEM, ModBlocks.VASE_PASSION, "passion"),
                new Pair(ModItems.BANANA_SAPLING_ITEM, ModBlocks.VASE_BANANA, "banana"),
                new Pair(ModItems.WHITE_FLOWER_ITEM, ModBlocks.VASE_WHITE_FLOWER, "white_flower"),
                new Pair(ModItems.BLUE_FLOWER_ITEM, ModBlocks.VASE_BLUE_FLOWER, "blue_flower"),
                new Pair(ModItems.RED_FLOWER_ITEM, ModBlocks.VASE_RED_FLOWER, "red_flower"),
                new Pair(ModItems.PURPLE_FLOWER_ITEM, ModBlocks.VASE_PURPLE_FLOWER, "purple_flower"),
                new Pair(ModItems.OUTSHROOM_ITEM, ModBlocks.VASE_OUTSHROOM, "outshroom"),
                new Pair(ModItems.OUTSHROOM_GLOWING_ITEM, ModBlocks.VASE_OUTSHROOM_GLOWING, "outshroom_glowing"));

        BlockPos abs = helper.absolutePos(VASE_POS);
        for (Pair p : pairs) {
            helper.setBlock(VASE_POS, ModBlocks.VASE.get());
            ItemStack stack = new ItemStack(p.plant().get(), 1);
            Player player = playerHolding(helper, stack);
            helper.getLevel()
                    .getBlockState(abs)
                    .useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitAt(helper, VASE_POS));
            BlockState after = helper.getLevel().getBlockState(abs);
            if (!after.is(p.filledVase().get())) {
                helper.fail(
                        "variant " + p.name() + ": expected " + p.filledVase().getId() + ", got " + after.getBlock());
                return;
            }
        }
        helper.succeed();
    }
}
