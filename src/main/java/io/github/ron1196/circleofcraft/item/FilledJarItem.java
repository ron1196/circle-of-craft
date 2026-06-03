package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Filled Pridestone Jar (water or lava) — places the liquid like a bucket,
 * then returns an empty jar. Water fizzles in the Nether.
 */
public class FilledJarItem extends Item {

    private static final int NETHER_PARTICLE_COUNT = 8;

    private final Fluid fluid;

    public FilledJarItem(Fluid fluid, Properties properties) {
        super(properties);
        this.fluid = fluid;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack jar = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(jar);
        }

        BlockPos hitPos = hit.getBlockPos();
        Direction side = hit.getDirection();
        BlockPos placePos = hitPos.relative(side);

        if (!level.mayInteract(player, hitPos) || !player.mayUseItemAt(placePos, side, jar)) {
            return InteractionResultHolder.fail(jar);
        }

        return tryPlace(level, player, jar, placePos);
    }

    private InteractionResultHolder<ItemStack> tryPlace(Level level, Player player, ItemStack jar, BlockPos pos) {
        // Water fizzles in the Nether
        if (fluid == Fluids.WATER && level.dimensionType().ultraWarm()) {
            level.playSound(
                    player,
                    pos,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    0.5F,
                    2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
            for (int i = 0; i < NETHER_PARTICLE_COUNT; i++) {
                level.addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        pos.getX() + level.random.nextDouble(),
                        pos.getY() + level.random.nextDouble(),
                        pos.getZ() + level.random.nextDouble(),
                        0.0,
                        0.0,
                        0.0);
            }
            return InteractionResultHolder.success(returnEmptyJar(jar, player));
        }

        BlockState existing = level.getBlockState(pos);

        // Try to place into a waterloggable block
        if (existing.getBlock() instanceof LiquidBlockContainer container
                && fluid == Fluids.WATER
                && container.canPlaceLiquid(player, level, pos, existing, fluid)) {
            container.placeLiquid(level, pos, existing, Fluids.WATER.getSource(false));
            playPlaceSound(level, player, pos);
            return InteractionResultHolder.success(returnEmptyJar(jar, player));
        }

        // Place in air or replaceable blocks
        if (level.isEmptyBlock(pos) || !existing.getFluidState().isEmpty() || existing.canBeReplaced(fluid)) {
            Block fluidBlock = fluid == Fluids.WATER ? Blocks.WATER : Blocks.LAVA;
            level.setBlock(pos, fluidBlock.defaultBlockState(), Block.UPDATE_ALL);
            playPlaceSound(level, player, pos);
            return InteractionResultHolder.success(returnEmptyJar(jar, player));
        }

        return InteractionResultHolder.fail(jar);
    }

    private void playPlaceSound(Level level, Player player, BlockPos pos) {
        level.playSound(
                player,
                pos,
                fluid == Fluids.WATER ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA,
                SoundSource.BLOCKS,
                1.0F,
                1.0F);
    }

    private ItemStack returnEmptyJar(ItemStack jar, Player player) {
        if (player.getAbilities().instabuild) {
            return jar;
        }
        return ItemUtils.createFilledResult(jar, player, new ItemStack(ModItems.JAR_EMPTY.get()));
    }
}
