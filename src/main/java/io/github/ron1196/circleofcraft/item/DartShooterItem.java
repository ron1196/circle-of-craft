package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.entity.projectile.DartEntity;
import io.github.ron1196.circleofcraft.entity.projectile.DartEntity.DartType;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DartShooterItem extends Item {

    private final boolean isSilver;

    public DartShooterItem(boolean isSilver) {
        super(new Item.Properties().durability(isSilver ? 286 : 214));
        this.isSilver = isSilver;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack shooterStack = player.getItemInHand(hand);

        // Find the first dart item in the player's inventory
        DartType dartType = null;
        int dartSlot = -1;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;

            DartType found = getDartTypeFromItem(stack);
            if (found != null) {
                dartType = found;
                dartSlot = i;
                break;
            }
        }

        if (dartType == null) {
            return InteractionResultHolder.fail(shooterStack);
        }

        if (!level.isClientSide) {
            // Consume one dart
            if (!player.getAbilities().instabuild) {
                player.getInventory().getItem(dartSlot).shrink(1);
            }

            // Spawn the dart entity
            DartEntity dart = new DartEntity(level, player, dartType);
            dart.setSilverShooter(isSilver);

            float velocity = dartType == DartType.BLUE ? 1.5F : 2.0F;
            dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 1.0F);

            level.addFreshEntity(dart);
            ModCriteriaTriggers.SHOOT_DART.trigger((ServerPlayer) player);

            // Damage the shooter item
            shooterStack.hurtAndBreak(
                    1,
                    player,
                    hand == InteractionHand.MAIN_HAND
                            ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                            : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        }

        // Play shoot sound
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARROW_SHOOT,
                SoundSource.PLAYERS,
                1.0F,
                1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

        // Apply cooldown
        int cooldownTicks = isSilver ? 12 : 20;
        player.getCooldowns().addCooldown(this, cooldownTicks);

        return InteractionResultHolder.sidedSuccess(shooterStack, level.isClientSide());
    }

    private static DartType getDartTypeFromItem(ItemStack stack) {
        if (stack.is(ModItems.DART_BLUE.get())) return DartType.BLUE;
        if (stack.is(ModItems.DART_RED.get())) return DartType.RED;
        if (stack.is(ModItems.DART_YELLOW.get())) return DartType.YELLOW;
        if (stack.is(ModItems.DART_PINK.get())) return DartType.PINK;
        if (stack.is(ModItems.DART_BLACK.get())) return DartType.BLACK;
        return null;
    }
}
