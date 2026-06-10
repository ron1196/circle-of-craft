package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

/**
 * Auto-smelt for Kivulite fire tools: a victim's drops are replaced with their furnace results
 * (raw ore → ingot, clay ball → brick, raw meat → cooked, …).
 *
 * <p>Mined blocks go through {@link BlockDropsEvent} (mining tools); killed mobs through
 * {@link LivingDropsEvent} (any Kivulite weapon). Both smelt the actual drops, so ores work (the
 * drop, raw_iron, has a recipe — the iron_ore block does not) and Fortune/looting counts are kept.
 */
@EventBusSubscriber(modid = CircleOfCraftMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class FireToolHelper {

    private FireToolHelper() {}

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (isMiningFireTool(event.getTool()) && smeltDrops(event.getDrops(), event.getLevel())) {
            spawnFlameParticles(event.getLevel(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity killer)
                || !isFireWeapon(killer.getMainHandItem())) {
            return;
        }
        LivingEntity dead = event.getEntity();
        if (dead.level() instanceof ServerLevel level && smeltDrops(event.getDrops(), level)) {
            spawnFlameParticles(level, dead.blockPosition());
        }
    }

    private static boolean smeltDrops(Iterable<ItemEntity> drops, ServerLevel level) {
        boolean smeltedAny = false;
        for (ItemEntity drop : drops) {
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
            ItemStack smelted = result.copy();
            smelted.setCount(input.getCount() * result.getCount());
            drop.setItem(smelted);
            smeltedAny = true;
        }
        return smeltedAny;
    }

    private static boolean isMiningFireTool(ItemStack tool) {
        return tool.getItem() instanceof KivulitePickaxeItem
                || tool.getItem() instanceof KivuliteAxeItem
                || tool.getItem() instanceof KivuliteShovelItem;
    }

    private static boolean isFireWeapon(ItemStack tool) {
        return tool.getItem() instanceof KivuliteSwordItem || isMiningFireTool(tool);
    }

    private static void spawnFlameParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0.0);
    }
}
