package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.List;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class PortalFrameBlock extends Block {

    private record ActivationKey(Supplier<Item> item, boolean consumed) {
        @SuppressWarnings("SameParameterValue")
        static ActivationKey consumed(Supplier<Item> item) {
            return new ActivationKey(item, true);
        }

        static ActivationKey kept(Supplier<Item> item) {
            return new ActivationKey(item, false);
        }
    }

    private record PortalConfig(Supplier<Block> portal, List<ActivationKey> keys) {
        static PortalConfig create(Supplier<Block> portal, ActivationKey... keys) {
            return new PortalConfig(portal, List.of(keys));
        }
    }

    private static final Map<Boolean, PortalConfig> ACTIVATION_KEYS = Map.of(
            false,
                    PortalConfig.create(
                            LionKingBlocks.PRIDE_LANDS_PORTAL,
                            ActivationKey.consumed(LionKingItems.TICKET),
                            ActivationKey.kept(LionKingItems.RHYTHM_STAFF)),
            true,
                    PortalConfig.create(
                            LionKingBlocks.OUTLANDS_PORTAL,
                            ActivationKey.consumed(LionKingItems.TICKET),
                            ActivationKey.kept(LionKingItems.ZIRA_COIN)));

    private final boolean isOutlands;

    public PortalFrameBlock(Properties properties, boolean isOutlands) {
        super(properties);
        this.isOutlands = isOutlands;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        PortalConfig config = ACTIVATION_KEYS.get(isOutlands);
        for (ActivationKey key : config.keys()) {
            if (stack.is(key.item().get())) {
                return tryCreatePortal(level, pos, player, stack, key, config);
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult tryCreatePortal(
            Level level, BlockPos pos, Player player, ItemStack stack, ActivationKey key, PortalConfig config) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        PortalBlock portalBlock = (PortalBlock) config.portal().get();

        for (BlockPos testPos :
                new BlockPos[] {pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()}) {
            if (level.getBlockState(testPos).isAir()) {
                if (portalBlock.trySpawnPortal(level, testPos)) {
                    level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (key.consumed() && !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
