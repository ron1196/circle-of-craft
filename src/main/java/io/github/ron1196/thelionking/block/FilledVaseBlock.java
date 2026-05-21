package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingBlocks;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class FilledVaseBlock extends VaseBlock {

    private final Supplier<? extends Item> contentItem;

    public FilledVaseBlock(Supplier<? extends Item> contentItem, Properties properties) {
        super(properties);
        this.contentItem = contentItem;
        VaseBlock.registerContent(contentItem, this);
    }

    public Item getContent() {
        return contentItem.get();
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        ItemStack handStack = player.getItemInHand(hand);
        if (!handStack.isEmpty()) return InteractionResult.PASS;

        if (!level.isClientSide) {
            ItemStack drop = new ItemStack(contentItem.get());
            if (!player.addItem(drop)) {
                player.drop(drop, false);
            }
            level.setBlock(pos, LionKingBlocks.VASE.get().defaultBlockState(), 3);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
