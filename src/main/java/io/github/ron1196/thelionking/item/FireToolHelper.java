package io.github.ron1196.thelionking.item;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared auto-smelt logic for Kivulite fire tools (pickaxe, axe, shovel). When mining a block that
 * has a furnace recipe, drops the smelted result instead.
 */
public final class FireToolHelper {

    private FireToolHelper() {}

    /**
     * Attempts auto-smelt on block break. Returns true if the block was auto-smelted (caller should
     * cancel normal drop logic).
     */
    public static boolean tryAutoSmelt(ItemStack tool, Level level, BlockState state, BlockPos pos, Player player) {
        if (player.isCreative() || level.isClientSide) {
            return false;
        }

        ItemStack blockDrop = new ItemStack(state.getBlock());
        if (blockDrop.isEmpty()) {
            return false;
        }

        Optional<SmeltingRecipe> recipe = ((ServerLevel) level)
                .getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(blockDrop), level);

        if (recipe.isPresent()) {
            ItemStack smeltResult =
                    recipe.get().getResultItem(level.registryAccess()).copy();
            if (!smeltResult.isEmpty()) {
                // Drop smelted items (respect quantity dropped)
                int count = state.getBlock()
                        .getDrops(state, (ServerLevel) level, pos, null)
                        .size();
                for (int i = 0; i < Math.max(1, count); i++) {
                    Block.popResource(level, pos, smeltResult.copy());
                }

                // Break the block without normal drops
                level.destroyBlock(pos, false);
                tool.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));

                // Flame particles
                spawnFlameParticles(level, pos);
                return true;
            }
        }

        return false;
    }

    /** Special case: Shovel on clay block → bricks directly. */
    public static boolean trySmeltClay(ItemStack tool, Level level, BlockState state, BlockPos pos, Player player) {
        if (player.isCreative() || level.isClientSide) {
            return false;
        }

        if (state.is(Blocks.CLAY)) {
            Block.popResource(level, pos, new ItemStack(Blocks.BRICKS));
            level.destroyBlock(pos, false);
            tool.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
            spawnFlameParticles(level, pos);
            return true;
        }

        return false;
    }

    public static void spawnFlameParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            double x = pos.getX() + level.random.nextFloat();
            double y = pos.getY() + level.random.nextFloat();
            double z = pos.getZ() + level.random.nextFloat();
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
