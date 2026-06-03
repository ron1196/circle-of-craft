package io.github.ron1196.circleofcraft.entity.hostile;

import io.github.ron1196.circleofcraft.entity.ai.HeadHopGoal;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkeletalHyenaHeadEntity extends Monster {

    public SkeletalHyenaHeadEntity(EntityType<? extends SkeletalHyenaHeadEntity> type, Level level) {
        super(type, level);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HeadHopGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        if (!level().isClientSide && level().getDifficulty() == Difficulty.PEACEFUL) {
            discard();
            return;
        }
        super.tick();
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (hasLineOfSight(player) && distanceToSqr(player) < 1.0D) {
            player.hurt(damageSources().mobAttack(this), 3.0F);
            playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (recentlyHit && source.getEntity() instanceof Player && random.nextInt(40) == 0) {
            ItemStack headStack = new ItemStack(ModItems.HYENA_HEAD_ITEM.get());
            CompoundTag blockEntityTag = new CompoundTag();
            blockEntityTag.putInt("HyenaType", 3);
            BlockItem.setBlockEntityData(headStack, BlockEntityTypes.HYENA_HEAD.get(), blockEntityTag);
            spawnAtLocation(headStack, 0.0F);
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@Nullable DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }
}
