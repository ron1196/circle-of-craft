package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.entity.projectile.SpearEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpearItem extends Item {

    private final boolean isPoisoned;

    public SpearItem(boolean isPoisoned) {
        super(new Item.Properties()
                .durability(160));
        this.isPoisoned = isPoisoned;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            SpearEntity spear = new SpearEntity(level, player, isPoisoned);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(spear);

            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
