package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Mango Juice — drinkable food item that returns an empty jar when consumed.
 */
public class MangoJuiceItem extends Item {

    public MangoJuiceItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player && player.getAbilities().instabuild) {
            return result;
        }

        if (result.isEmpty()) {
            return new ItemStack(LionKingItems.JAR_EMPTY.get());
        }

        if (entity instanceof Player player) {
            player.getInventory().add(new ItemStack(LionKingItems.JAR_EMPTY.get()));
        }

        return result;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }
}
