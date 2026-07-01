package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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
                    ModBlocks.PRIDE_LANDS_PORTAL,
                    ActivationKey.consumed(ModItems.TICKET),
                    ActivationKey.kept(ModItems.RAFIKI_STICK)),
            true,
            PortalConfig.create(
                    ModBlocks.OUTLANDS_PORTAL,
                    ActivationKey.consumed(ModItems.TICKET),
                    ActivationKey.kept(ModItems.ZIRA_COIN),
                    ActivationKey.kept(ModItems.RAFIKI_STICK)));

    private final boolean isOutlands;

    public PortalFrameBlock(Properties properties, boolean isOutlands) {
        super(properties);
        this.isOutlands = isOutlands;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        PortalConfig config = ACTIVATION_KEYS.get(isOutlands);
        for (ActivationKey key : config.keys()) {
            if (stack.is(key.item().get())) {
                return switch (tryCreatePortal(level, pos, player, stack, key, config)) {
                    case SUCCESS -> ItemInteractionResult.SUCCESS;
                    case CONSUME -> ItemInteractionResult.CONSUME;
                    default -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                };
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
