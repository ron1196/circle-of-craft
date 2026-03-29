package io.github.ron1196.thelionking.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Kivulite Shovel — auto-smelts mined blocks that have a furnace recipe. Also converts clay blocks
 * directly into bricks.
 */
public class KivuliteShovelItem extends ShovelItem {

    public KivuliteShovelItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);

        // Auto-smelt if the shovel is effective against this block
        if (isCorrectToolForDrops(state) && FireToolHelper.tryAutoSmelt(stack, level, state, pos, player)) {
            return true;
        }

        // Special: clay → bricks
        if (FireToolHelper.trySmeltClay(stack, level, state, pos, player)) {
            return true;
        }

        return super.onBlockStartBreak(stack, pos, player);
    }
}
