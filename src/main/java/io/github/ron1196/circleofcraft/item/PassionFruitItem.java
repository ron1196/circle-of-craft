package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.util.LevelHelper;
import io.github.ron1196.circleofcraft.world.dimension.Dimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Passion fruit — a normal food that, eaten at full health, ferries the player between Upendi and
 * the Pride Lands (in Upendi → out, anywhere else → in). This is the only survival passage to
 * Upendi, which is why passion trees also grow (very rarely) in the Pride Lands: the first fruit
 * must be obtainable before you can ever reach Upendi. Farming the dense Upendi trees provides the
 * fruit to leave again.
 */
public class PassionFruitItem extends Item {

    public PassionFruitItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        boolean fullHealth = entity.getHealth() >= entity.getMaxHealth();
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide() && fullHealth && entity instanceof ServerPlayer player) {
            travel(player);
        }
        return result;
    }

    private static void travel(@NotNull ServerPlayer player) {
        boolean inUpendi = player.level().dimension() == Dimensions.UPENDI_LEVEL;
        ResourceKey<Level> destKey = inUpendi ? Dimensions.PRIDE_LANDS_LEVEL : Dimensions.UPENDI_LEVEL;
        ServerLevel dest = player.server.getLevel(destKey);
        if (dest == null) {
            return;
        }
        int x = (int) player.getX();
        int z = (int) player.getZ();
        int y = LevelHelper.surfaceY(dest, x, z);
        player.serverLevel()
                .playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.teleportTo(dest, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());
    }
}
