package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Zebra Milk — drinkable item that clears all potion effects, like vanilla milk.
 * Returns an empty jar when consumed.
 */
public class ZebraMilkItem extends Item {

    private static final int USE_DURATION = 32;

    public ZebraMilkItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            entity.removeAllEffects();
        }

        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }

        stack.shrink(1);

        if (stack.isEmpty()) {
            return new ItemStack(LionKingItems.JAR_EMPTY.get());
        }

        if (entity instanceof Player player) {
            player.getInventory().add(new ItemStack(LionKingItems.JAR_EMPTY.get()));
        }

        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
