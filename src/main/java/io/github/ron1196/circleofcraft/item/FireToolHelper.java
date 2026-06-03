package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Shared auto-smelt logic for Kivulite fire tools (pickaxe, axe, shovel). When mining a block that
 * has a furnace recipe, drops the smelted result instead.
 *
 * <p>1.21 removed the per-item {@code onBlockStartBreak} pre-break hook, so the smelt is driven from
 * {@link BlockEvent.BreakEvent}, which is cancellable before vanilla spawns the raw drops.
 */
@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class FireToolHelper {

    private FireToolHelper() {}

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack tool = player.getMainHandItem();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (tool.getItem() instanceof KivuliteShovelItem
                && tool.isCorrectToolForDrops(state)
                && trySmeltClay(tool, level, state, pos, player)) {
            event.setCanceled(true);
            return;
        }

        boolean fireTool = tool.getItem() instanceof KivulitePickaxeItem
                || tool.getItem() instanceof KivuliteAxeItem
                || tool.getItem() instanceof KivuliteShovelItem;
        if (fireTool && tool.isCorrectToolForDrops(state) && tryAutoSmelt(tool, level, state, pos, player)) {
            event.setCanceled(true);
        }
    }

    private static boolean tryAutoSmelt(
            ItemStack tool, ServerLevel level, BlockState state, BlockPos pos, Player player) {
        if (player.isCreative()) {
            return false;
        }

        ItemStack blockDrop = new ItemStack(state.getBlock());
        if (blockDrop.isEmpty()) {
            return false;
        }

        Optional<RecipeHolder<SmeltingRecipe>> recipe =
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(blockDrop), level);

        if (recipe.isPresent()) {
            ItemStack smeltResult =
                    recipe.get().value().getResultItem(level.registryAccess()).copy();
            if (!smeltResult.isEmpty()) {
                int count = state.getBlock().getDrops(state, level, pos, null).size();
                for (int i = 0; i < Math.max(1, count); i++) {
                    Block.popResource(level, pos, smeltResult.copy());
                }

                level.destroyBlock(pos, false);
                tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

                spawnFlameParticles(level, pos);
                return true;
            }
        }

        return false;
    }

    private static boolean trySmeltClay(
            ItemStack tool, ServerLevel level, BlockState state, BlockPos pos, Player player) {
        if (player.isCreative()) {
            return false;
        }

        if (state.is(Blocks.CLAY)) {
            Block.popResource(level, pos, new ItemStack(Blocks.BRICKS));
            level.destroyBlock(pos, false);
            tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            spawnFlameParticles(level, pos);
            return true;
        }

        return false;
    }

    private static void spawnFlameParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            double x = pos.getX() + level.random.nextFloat();
            double y = pos.getY() + level.random.nextFloat();
            double z = pos.getZ() + level.random.nextFloat();
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
