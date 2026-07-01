package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.item.RafikiStickItem;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

    public static final String REGISTRY_NAME = "scar";

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ScarEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Scar"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    private static final String SCAR_GREETING =
            "Well, well, well... look what we have here. A little visitor. How delightfully... brave. And foolish.";

    private boolean hasSpoken = false;

    /** Players who have already been told that only the Rafiki Stick can harm Scar (once per encounter). */
    private final Set<UUID> hintedPlayers = new HashSet<>();

    /**
     * Ticks between distant roars (audible from far away to help player find Scar).
     */
    private static final int ROAR_INTERVAL_MIN = 200;

    private static final int ROAR_INTERVAL_RANGE = 400;
    private static final float ROAR_VOLUME = 16.0F;

    /** The closer a player is, the faster Scar roars — turning the roar into a hot/cold homing signal. */
    private static final double ROAR_TRACK_RANGE = 128.0;

    private static final double ROAR_DISTANCE_FACTOR = 4.0;
    private static final int ROAR_CLOSE_MIN = 40;
    private static final int ROAR_FAR_MAX = 400;
    private static final int ROAR_JITTER = 20;
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

    /**
     * Scar is invulnerable to all damage except from the Rafiki Stick. When hit with a
     * non-Rafiki-Stick weapon, plays a metallic clang sound, spawns smoke particles, and
     * gives the player a one-time hint.
     */
    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof RafikiStickItem) {
            return super.hurt(source, amount);
        }

        // Reject all non-Rafiki-Stick damage with feedback
        if (attacker instanceof Player player) {
            level().playSound(null, blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 1.0F, 1.0F);
            if (level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SMOKE, getX(), getY() + 0.5, getZ(), 10, 0.3, 0.5, 0.3, 0.02);
            }
            if (hintedPlayers.add(player.getUUID())) {
                ChatHelper.sendNpcMessage(player, "Rafiki", "De Rafiki Stick is de ONLY weapon dat can harm Scar!");
            }
        }
        return false;
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
                            ModSoundEvents.LION_ROAR.get(),
                            SoundSource.HOSTILE,
                            ROAR_VOLUME,
                            0.8F + random.nextFloat() * 0.3F);
            roarCooldown = nextRoarDelay();
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

    private int nextRoarDelay() {
        Player nearest = level().getNearestPlayer(this, ROAR_TRACK_RANGE);
        if (nearest == null) {
            return ROAR_INTERVAL_MIN + random.nextInt(ROAR_INTERVAL_RANGE);
        }
        int base = (int) Mth.clamp(distanceTo(nearest) * ROAR_DISTANCE_FACTOR, ROAR_CLOSE_MIN, ROAR_FAR_MAX);
        return base + random.nextInt(ROAR_JITTER);
    }

    private static final double DEATH_MESSAGE_RANGE = 50.0;

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        if (level().isClientSide() || !(level() instanceof ServerLevel serverLevel)) return;

        WorldData data = WorldData.get(serverLevel);
        for (Player player :
                level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(DEATH_MESSAGE_RANGE))) {
            ChatHelper.sendNpcMessage(player, "Scar", "This is... not... the end. I will... always... be king...");
            if (player instanceof ServerPlayer sp) {
                data.getQuestManager().tryAdvance(RafikiQuestline.QUEST_ID, sp, QuestTrigger.SCAR_KILLED);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        spawnAtLocation(ModItems.SCAR_RUG.get());
    }

    @Override
    public int getExperienceReward() {
        return 50;
    }
}
