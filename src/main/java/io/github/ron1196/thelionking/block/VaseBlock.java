package io.github.ron1196.thelionking.block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class VaseBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);

    // Resolved lazily — plant Item instances aren't available during DeferredRegister construction.
    private static final Map<Supplier<? extends Item>, FilledVaseBlock> PENDING_CONTENTS = new HashMap<>();
    private static Map<Item, FilledVaseBlock> resolvedContents;

    public static void registerContent(Supplier<? extends Item> plantItem, FilledVaseBlock filledVase) {
        PENDING_CONTENTS.put(plantItem, filledVase);
    }

    private static Map<Item, FilledVaseBlock> contents() {
        Map<Item, FilledVaseBlock> map = resolvedContents;
        if (map == null) {
            map = new HashMap<>();
            for (Map.Entry<Supplier<? extends Item>, FilledVaseBlock> e : PENDING_CONTENTS.entrySet()) {
                map.put(e.getKey().get(), e.getValue());
            }
            resolvedContents = map;
        }
        return map;
    }

    public VaseBlock(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        FilledVaseBlock filled = contents().get(stack.getItem());
        if (filled == null) return InteractionResult.PASS;

        if (!level.isClientSide) {
            level.setBlock(pos, filled.defaultBlockState(), 3);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            level.playSound(
                    null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
