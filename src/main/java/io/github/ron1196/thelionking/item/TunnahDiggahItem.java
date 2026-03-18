package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.registry.Blocks;
import io.github.ron1196.thelionking.registry.LKEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Tunnah Diggah — AoE mining tool that breaks stone, dirt, and grass in a cube radius.
 * Biggah Diggah enchantment increases the radius. Precision enchantment increases drop rate.
 */
public class TunnahDiggahItem extends PickaxeItem {

    public TunnahDiggahItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public boolean mineBlock(
            @NotNull ItemStack stack,
            @NotNull Level level,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull LivingEntity miner
    ) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return super.mineBlock(stack, level, state, pos, miner);
        }

        if (!isAoETarget(state)) {
            return super.mineBlock(stack, level, state, pos, miner);
        }

        int radius = 1 + EnchantmentHelper.getItemEnchantmentLevel(LKEnchantments.BIGGAH_DIGGAH.get(), stack);
        boolean hasPrecision = EnchantmentHelper.getItemEnchantmentLevel(LKEnchantments.PRECISION.get(), stack) > 0;
        boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    BlockPos targetPos = pos.offset(dx, dy, dz);
                    BlockState targetState = level.getBlockState(targetPos);

                    if (!isAoETarget(targetState)) continue;

                    // Drop with chance: 33% normally, 66% with Precision
                    boolean shouldDrop = hasPrecision
                            ? level.random.nextInt(3) > 0
                            : level.random.nextInt(3) == 0;

                    if (shouldDrop) {
                        BlockState dropState = getDropState(targetState, hasSilkTouch);
                        Block.popResource(level, targetPos, new ItemStack(dropState.getBlock()));
                    }

                    level.destroyBlock(targetPos, false);
                    stack.hurtAndBreak(1, miner, e -> e.broadcastBreakEvent(miner.getUsedItemHand()));
                }
            }
        }

        return super.mineBlock(stack, level, state, pos, miner);
    }

    private static boolean isAoETarget(BlockState state) {
        return state.is(net.minecraft.world.level.block.Blocks.DIRT)
                || state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                || state.is(net.minecraft.world.level.block.Blocks.STONE)
                || state.is(net.minecraft.world.level.block.Blocks.NETHERRACK)
                || state.is(net.minecraft.world.level.block.Blocks.END_STONE)
                || state.is(Blocks.PRIDESTONE.get())
                || state.is(Blocks.CORRUPT_PRIDESTONE.get());
    }

    private static BlockState getDropState(BlockState state, boolean silkTouch) {
        if (silkTouch) return state;
        if (state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) return net.minecraft.world.level.block.Blocks.DIRT.defaultBlockState();
        if (state.is(net.minecraft.world.level.block.Blocks.STONE)) return net.minecraft.world.level.block.Blocks.COBBLESTONE.defaultBlockState();
        return state;
    }
}
