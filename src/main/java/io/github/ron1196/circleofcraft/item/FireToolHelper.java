package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

/**
 * Auto-smelt for Kivulite fire tools (pickaxe, axe, shovel): a broken block's drops are replaced
 * with their furnace results (raw ore → ingot, clay ball → brick, …).
 *
 * <p>Driven from {@link BlockDropsEvent} so it operates on the real drops — smelting the drop
 * (e.g. raw_iron), not the mined block (iron_ore has no smelting recipe) — and keeps Fortune counts.
 */
@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class FireToolHelper {

    private FireToolHelper() {}

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!isFireTool(event.getTool())) {
            return;
        }
        ServerLevel level = event.getLevel();
        boolean smelted = false;
        for (ItemEntity drop : event.getDrops()) {
            ItemStack input = drop.getItem();
            Optional<RecipeHolder<SmeltingRecipe>> recipe =
                    level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level);
            if (recipe.isEmpty()) {
                continue;
            }
            ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
            if (result.isEmpty()) {
                continue;
            }
            ItemStack smeltedStack = result.copy();
            smeltedStack.setCount(input.getCount() * result.getCount());
            drop.setItem(smeltedStack);
            smelted = true;
        }
        if (smelted) {
            spawnFlameParticles(level, event.getPos());
        }
    }

    private static boolean isFireTool(ItemStack tool) {
        return tool.getItem() instanceof KivulitePickaxeItem
                || tool.getItem() instanceof KivuliteAxeItem
                || tool.getItem() instanceof KivuliteShovelItem;
    }

    private static void spawnFlameParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0.0);
    }
}
