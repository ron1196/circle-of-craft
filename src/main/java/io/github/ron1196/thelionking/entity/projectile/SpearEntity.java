package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SpearEntity extends AbstractArrow
    implements net.minecraft.world.entity.projectile.ItemSupplier {

  private static final EntityDataAccessor<Boolean> DATA_POISONED =
      SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BOOLEAN);

  public SpearEntity(EntityType<? extends AbstractArrow> type, Level level) {
    super(type, level);
  }

  public SpearEntity(Level level, LivingEntity shooter, boolean isPoisoned) {
    super(EntityTypes.SPEAR.get(), shooter, level);
    setPoisoned(isPoisoned);
    this.pickup = Pickup.ALLOWED;
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_POISONED, false);
  }

  public boolean isPoisoned() {
    return this.entityData.get(DATA_POISONED);
  }

  public void setPoisoned(boolean poisoned) {
    this.entityData.set(DATA_POISONED, poisoned);
  }

  @Override
  protected void onHitEntity(@NotNull EntityHitResult result) {
    boolean poisoned = isPoisoned();
    float damage = poisoned ? 5.0F + this.random.nextInt(4) : 7.0F + this.random.nextInt(4);
    setBaseDamage(damage);

    super.onHitEntity(result);

    if (!this.level().isClientSide
        && poisoned
        && result.getEntity() instanceof LivingEntity target) {
      if (this.random.nextFloat() < 0.75F) {
        int duration = 60 + this.random.nextInt(61); // 60-120 ticks
        target.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 0));
      }
    }
  }

  @Override
  public @NotNull ItemStack getItem() {
    return getPickupItem();
  }

  @Override
  protected @NotNull ItemStack getPickupItem() {
    return isPoisoned()
        ? new ItemStack(Items.POISONED_SPEAR.get())
        : new ItemStack(Items.GEMSBOK_SPEAR.get());
  }
}
