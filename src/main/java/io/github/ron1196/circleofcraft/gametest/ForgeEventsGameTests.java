package io.github.ron1196.circleofcraft.gametest;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.event.ModForgeEvents;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Locks the mutation core of {@link ModForgeEvents}' two terrain-mutating handlers:
 *
 * <ul>
 *   <li>{@link ModForgeEvents#tryTillSand} — hoe-on-sand → tilled sand (RightClickBlock event).</li>
 *   <li>{@link ModForgeEvents#convertSandToOutsand} — lightning landing in the Outlands
 *       (EntityJoinLevelEvent), where sand within a small radius becomes outsand.</li>
 * </ul>
 *
 * <p>Both event handlers themselves cannot be invoked directly here — the hoe path needs a real
 * player to construct {@code PlayerInteractEvent.RightClickBlock}, and the lightning path is
 * gated to the Outlands dimension while game-test arenas live in the overworld. The handlers'
 * pre-conditions (item type, dimension) are simple and unlikely to regress; the mutation algorithms
 * are what's worth pinning, and those are extracted as package-private static methods exactly
 * for that purpose. Tracked in issue #63.
 */
@GameTestHolder(CircleOfCraftMod.MOD_ID)
@PrefixGameTestTemplate(false)
public class ForgeEventsGameTests {

    private static final String EMPTY = "empty";

    // ── tryTillSand ─────────────────────────────────────────────────────────

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void tryTillSandConvertsSandWithAirAbove(GameTestHelper helper) {
        BlockPos sand = new BlockPos(2, 2, 2);
        helper.setBlock(sand.below(), Blocks.STONE.defaultBlockState()); // anchor — sand is a falling block
        helper.setBlock(sand, Blocks.SAND.defaultBlockState());
        helper.setBlock(sand.above(), Blocks.AIR.defaultBlockState());

        boolean tilled = ModForgeEvents.tryTillSand(helper.getLevel(), helper.absolutePos(sand));

        if (!tilled) {
            helper.fail("tryTillSand returned false on plain sand with air above");
            return;
        }
        if (!helper.getBlockState(sand).is(ModBlocks.TILLED_SAND.get())) {
            helper.fail("sand was not converted to tilled sand");
            return;
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void tryTillSandRejectsNonSand(GameTestHelper helper) {
        BlockPos stone = new BlockPos(2, 2, 2);
        helper.setBlock(stone, Blocks.STONE.defaultBlockState());
        helper.setBlock(stone.above(), Blocks.AIR.defaultBlockState());

        boolean tilled = ModForgeEvents.tryTillSand(helper.getLevel(), helper.absolutePos(stone));

        if (tilled) {
            helper.fail("tryTillSand returned true on stone");
            return;
        }
        if (!helper.getBlockState(stone).is(Blocks.STONE)) {
            helper.fail("stone was unexpectedly mutated");
            return;
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void tryTillSandRejectsSandWithBlockAbove(GameTestHelper helper) {
        BlockPos sand = new BlockPos(2, 2, 2);
        helper.setBlock(sand.below(), Blocks.STONE.defaultBlockState()); // anchor sand
        helper.setBlock(sand, Blocks.SAND.defaultBlockState());
        helper.setBlock(sand.above(), Blocks.STONE.defaultBlockState());

        boolean tilled = ModForgeEvents.tryTillSand(helper.getLevel(), helper.absolutePos(sand));

        if (tilled) {
            helper.fail("tryTillSand returned true even though a block sat above");
            return;
        }
        if (!helper.getBlockState(sand).is(Blocks.SAND)) {
            helper.fail("sand under a block was incorrectly tilled");
            return;
        }
        helper.succeed();
    }

    // ── convertSandToOutsand ────────────────────────────────────────────────

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void convertSandToOutsandReplacesSandInRadius(GameTestHelper helper) {
        // Lay a 3x3 patch of sand at the strike point; at least one must convert.
        BlockPos center = new BlockPos(2, 2, 2);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                helper.setBlock(center.offset(dx, -1, dz), Blocks.STONE.defaultBlockState()); // anchor sand
                helper.setBlock(center.offset(dx, 0, dz), Blocks.SAND.defaultBlockState());
                helper.setBlock(center.offset(dx, 1, dz), Blocks.AIR.defaultBlockState());
            }
        }

        ModForgeEvents.convertSandToOutsand(helper.getLevel(), helper.absolutePos(center));

        int outsandCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (helper.getBlockState(center.offset(dx, 0, dz)).is(ModBlocks.OUTSAND.get())) {
                    outsandCount++;
                }
            }
        }
        if (outsandCount == 0) {
            helper.fail("convertSandToOutsand left no outsand in a 3x3 sand patch around the strike point");
            return;
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY, timeoutTicks = 40)
    public void convertSandToOutsandLeavesNonSandUntouched(GameTestHelper helper) {
        // Lay stone at the strike point; nothing should mutate.
        BlockPos center = new BlockPos(2, 2, 2);
        helper.setBlock(center, Blocks.STONE.defaultBlockState());
        helper.setBlock(center.north(), Blocks.STONE.defaultBlockState());
        helper.setBlock(center.south(), Blocks.STONE.defaultBlockState());

        ModForgeEvents.convertSandToOutsand(helper.getLevel(), helper.absolutePos(center));

        if (!helper.getBlockState(center).is(Blocks.STONE)) {
            helper.fail("convertSandToOutsand mutated non-sand at strike point");
            return;
        }
        if (!helper.getBlockState(center.north()).is(Blocks.STONE)
                || !helper.getBlockState(center.south()).is(Blocks.STONE)) {
            helper.fail("convertSandToOutsand mutated non-sand within radius");
            return;
        }
        helper.succeed();
    }
}
