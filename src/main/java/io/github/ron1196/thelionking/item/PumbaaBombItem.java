package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.entity.projectile.PumbaaBombEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PumbaaBombItem extends Item {

    public PumbaaBombItem() {
        super(new Item.Properties().stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            PumbaaBombEntity bomb = new PumbaaBombEntity(level, player);
            bomb.setItem(stack);
            bomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(bomb);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS,
                0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        player.getCooldowns().addCooldown(this, 20);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
