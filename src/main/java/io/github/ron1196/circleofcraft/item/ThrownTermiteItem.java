package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.entity.projectile.TermiteThrownEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ThrownTermiteItem extends Item {

    public ThrownTermiteItem() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            TermiteThrownEntity termite = new TermiteThrownEntity(level, player);
            termite.setItem(stack);
            termite.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(termite);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SNOWBALL_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        player.getCooldowns().addCooldown(this, 20);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
