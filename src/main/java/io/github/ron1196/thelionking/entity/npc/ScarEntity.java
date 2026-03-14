package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.quest.LKQuestRafiki;
import io.github.ron1196.thelionking.quest.LKQuests;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ScarEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ScarEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean hasSpoken = false;

    public ScarEntity(EntityType<? extends ScarEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HOSTILE, true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isHostile() {
        return this.entityData.get(DATA_HOSTILE);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide() && !hasSpoken) {
            Player nearest = level().getNearestPlayer(this, 16.0);
            if (nearest != null) {
                nearest.sendSystemMessage(Component.literal(
                        "\u00a7e<Scar> \u00a7fSo, you've come to challenge me? How delightfully brave... and foolish."));
                hasSpoken = true;
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                LKLevelData data = LKLevelData.get(serverLevel);
                ((LKQuestRafiki) LKQuests.RAFIKI_QUEST).tryAdvanceStage(serverPlayer, data, "scar_killed");

                serverPlayer.sendSystemMessage(Component.literal(
                        "\u00a7e<Scar> \u00a7fThis... is not... the end..."));
            }
        }
    }

    @Override
    public int getExperienceReward() {
        return 50;
    }
}
