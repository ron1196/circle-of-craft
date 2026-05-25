package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Zira Mound Gate — indestructible block that can only be broken with the Rafiki Stick
 * after completing the Rafiki questline. When broken, recursively destroys all adjacent
 * gate blocks (chain break).
 */
public class ZiraMoundGateBlock extends Block {

    public ZiraMoundGateBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (!held.is(LionKingItems.RAFIKI_STICK.get())) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            if (!WorldData.get(serverLevel).getQuestManager().isComplete(RafikiQuestline.QUEST_ID)) {
                ChatHelper.sendNpcMessage(
                        player,
                        "Rafiki's Stick",
                        "De gate resists your touch... de quest is not yet complete. De spirits say you are not ready.");
                return InteractionResult.SUCCESS;
            }
            breakGateChain(level, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void breakGateChain(Level level, BlockPos pos) {
        if (!(level.getBlockState(pos).getBlock() instanceof ZiraMoundGateBlock)) {
            return;
        }

        level.destroyBlock(pos, false);

        for (Direction dir : Direction.values()) {
            breakGateChain(level, pos.relative(dir));
        }
    }
}
