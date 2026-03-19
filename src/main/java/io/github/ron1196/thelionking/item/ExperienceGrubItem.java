package io.github.ron1196.thelionking.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExperienceGrubItem extends Item {

  public ExperienceGrubItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    if (entity instanceof Player player && !level.isClientSide()) {
      player.giveExperiencePoints(20 + level.getRandom().nextInt(15));
      level.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          SoundEvents.EXPERIENCE_ORB_PICKUP,
          SoundSource.PLAYERS,
          0.1F,
          0.5F * ((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.7F + 1.8F));
    }
    return super.finishUsingItem(stack, level, entity);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
