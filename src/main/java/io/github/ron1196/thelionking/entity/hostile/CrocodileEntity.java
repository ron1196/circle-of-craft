package io.github.ron1196.thelionking.entity.hostile;

import io.github.ron1196.thelionking.registry.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CrocodileEntity extends Monster {

  public CrocodileEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.setMaxUpStep(1.0F);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public boolean doHurtTarget(@NotNull Entity target) {
    if (this.isInWater()) {
      // Deal double damage when in water
      float baseDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      target.hurt(this.damageSources().mobAttack(this), baseDamage * 2.0F);
      return true;
    }
    return super.doHurtTarget(target);
  }

  @Override
  protected @NotNull SoundEvent getAmbientSound() {
    return SoundEvents.CROCODILE_AMBIENT.get();
  }

  @Override
  protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
    return SoundEvents.CROCODILE_SNAP.get();
  }

  @Override
  protected @NotNull SoundEvent getDeathSound() {
    return SoundEvents.CROCODILE_DEATH.get();
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 18.0)
        .add(Attributes.ATTACK_DAMAGE, 2.0)
        .add(Attributes.MOVEMENT_SPEED, 0.3);
  }
}
