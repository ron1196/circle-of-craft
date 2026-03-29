package io.github.ron1196.thelionking.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Kivulite Axe — auto-smelts mined blocks that have a furnace recipe. */
public class KivuliteAxeItem extends AxeItem {

    public KivuliteAxeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);

        if (isCorrectToolForDrops(state) && FireToolHelper.tryAutoSmelt(stack, level, state, pos, player)) {
            return true;
        }
        return super.onBlockStartBreak(stack, pos, player);
    }
}
