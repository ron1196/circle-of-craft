package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.registry.SoundEvents;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
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
import org.jetbrains.annotations.NotNull;

public class ScarEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ScarEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Scar"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    private static final String SCAR_GREETING =
            "So, you've come to challenge me? How delightfully brave... and foolish.";

    private boolean hasSpoken = false;

    /**
     * Ticks between distant roars (audible from far away to help player find Scar).
     */
    private static final int ROAR_INTERVAL_MIN = 200;

    private static final int ROAR_INTERVAL_RANGE = 400;
    private static final float ROAR_VOLUME = 16.0F;
    private int roarCooldown = 100;

    public ScarEntity(EntityType<? extends ScarEntity> type, Level level) {
        super(type, level);
        this.setCustomName(Component.literal("Scar"));
        this.setCustomNameVisible(true);
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
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
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
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            return;
        }

        bossEvent.setProgress(getHealth() / getMaxHealth());

        // Periodic loud roar — audible from far away to help player find Scar
        if (roarCooldown > 0) {
            roarCooldown--;
        } else {
            level().playSound(
                            null,
                            blockPosition(),
                            SoundEvents.LION_ROAR.get(),
                            SoundSource.HOSTILE,
                            ROAR_VOLUME,
                            0.8F + random.nextFloat() * 0.3F);
            roarCooldown = ROAR_INTERVAL_MIN + random.nextInt(ROAR_INTERVAL_RANGE);
        }

        if (hasSpoken) {
            return;
        }

        Player nearest = level().getNearestPlayer(this, 16.0);
        if (nearest == null) {
            return;
        }
        ChatHelper.sendNpcMessage(nearest, "Scar", SCAR_GREETING);
        hasSpoken = true;
    }

    private static final double DEATH_MESSAGE_RANGE = 50.0;

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        if (level().isClientSide() || !(level() instanceof ServerLevel serverLevel)) return;

        // Mark Scar as dead in world data — Rafiki will advance the quest when talked to
        WorldData data = WorldData.get(serverLevel);
        data.setScarDefeated(true);

        // Message nearby players
        for (Player player :
                level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(DEATH_MESSAGE_RANGE))) {
            ChatHelper.sendNpcMessage(player, "Scar", "This... is not... the end...");
        }
    }

    @Override
    public int getExperienceReward() {
        return 50;
    }
}
