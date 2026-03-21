package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.entity.animal.ZebraEntity;
import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Empty Pridestone Jar — bucket-like item that picks up water or lava source blocks.
 * Zebra milking is handled in {@link ZebraEntity#mobInteract}.
 */
public class JarItem extends Item {

    public JarItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack jar = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(jar);
        }

        BlockPos pos = hit.getBlockPos();
        if (!level.mayInteract(player, pos)) {
            return InteractionResultHolder.fail(jar);
        }

        BlockState state = level.getBlockState(pos);
        FluidState fluid = level.getFluidState(pos);

        if (fluid.is(Fluids.WATER) && fluid.isSource()) {
            return fillJar(level, player, jar, pos, state, LionKingItems.JAR_WATER.get());
        }

        if (fluid.is(Fluids.LAVA) && fluid.isSource()) {
            return fillJar(level, player, jar, pos, state, LionKingItems.JAR_LAVA.get());
        }

        return InteractionResultHolder.pass(jar);
    }

    private InteractionResultHolder<ItemStack> fillJar(
            Level level, Player player, ItemStack jar, BlockPos pos, BlockState state, Item filledJar) {
        if (state.getBlock() instanceof BucketPickup pickup) {
            pickup.pickupBlock(level, pos, state);
        } else {
            level.removeBlock(pos, false);
        }

        level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (player.getAbilities().instabuild) {
            return InteractionResultHolder.success(jar);
        }

        return InteractionResultHolder.success(ItemUtils.createFilledResult(jar, player, new ItemStack(filledJar)));
    }
}
