package io.github.ron1196.thelionking.entity.hostile;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.npc.ZiraEntity;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TermiteQueenEntity extends Monster implements GeoEntity {

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.termite_queen.idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.termite_queen.walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.termite_queen.attack");
    private static final RawAnimation DEATH_ANIM =
            RawAnimation.begin().thenPlayAndHold("animation.termite_queen.death");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public static final int SPAWN_INTERVAL = 100;
    public static final int MAX_NEARBY_TERMITES = 8;
    public static final double TERMITE_SEARCH_RADIUS = 24.0;
    private static final double SPAWN_OFFSET_SPREAD = 2.0;
    private static final float DAMAGE_PER_TERMITE = 6.0F;
    private static final int EXPERIENCE_REWARD = 500;
    private static final double MELEE_SPEED = 1.0;
    private static final double WANDER_SPEED = 0.8;
    private static final float LOOK_DISTANCE = 8.0F;
    private static final double MAX_HEALTH = 150.0;
    private static final double MOVEMENT_SPEED = 0.25;
    private static final double ATTACK_DAMAGE = 3.0;
    private static final double ARMOR = 4.0;
    private static final double FOLLOW_RANGE = 32.0;
    private static final double KNOCKBACK_RESISTANCE = 0.5;
    private static final int MIN_NUKA_SHARDS = 5;
    private static final int EXTRA_NUKA_SHARDS = 6;
    private static final int MIN_CRYSTALS = 1;
    private static final int EXTRA_CRYSTALS = 3;
    private static final float ZIRA_DISMOUNT_DAMAGE = 100.0F;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.thelionking.termite_queen"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS);

    private int spawnCooldown;
    private float termiteDamageAccumulator;

    public int getSpawnCooldown() {
        return spawnCooldown;
    }

    public TermiteQueenEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = EXPERIENCE_REWARD;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, MELEE_SPEED, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, WANDER_SPEED));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, LOOK_DISTANCE));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.getEntity() instanceof TermiteEntity) {
            return false;
        }
        float healthBefore = this.getHealth();
        boolean result = super.hurt(source, amount);
        if (result && this.getHealth() < healthBefore && !this.level().isClientSide) {
            termiteDamageAccumulator += healthBefore - this.getHealth();
            while (termiteDamageAccumulator >= DAMAGE_PER_TERMITE) {
                spawnTermite(true);
                termiteDamageAccumulator -= DAMAGE_PER_TERMITE;
            }
        }
        return result;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            if (this.getTarget() != null && this.isAlive()) {
                if (spawnCooldown <= 0) {
                    spawnTermite(false);
                    spawnCooldown = SPAWN_INTERVAL;
                }
            }
            if (spawnCooldown > 0) {
                spawnCooldown--;
            }
        }
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof ZiraEntity) {
            passenger.setYRot(getYRot());
            passenger.setXRot(getXRot());
            passenger.setYBodyRot(yBodyRot);
            passenger.setYHeadRot(getYHeadRot());
        }
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
            // Dismount and damage Zira
            for (Entity passenger : getPassengers()) {
                if (passenger instanceof ZiraEntity zira) {
                    zira.stopRiding();
                    zira.hurt(damageSources().magic(), ZIRA_DISMOUNT_DAMAGE);
                    ChatHelper.broadcastNpcMessage(
                            level(),
                            "Zira",
                            "I don't NEED that overgrown insect! I'll tear you apart with my OWN claws!");
                }
            }

            // Explosion at queen's death location
            level().explode(this, getX(), getY() + 3.0, getZ(), 3.0F, Level.ExplosionInteraction.NONE);

            // Advance quest from DEFEAT_TERMITE_QUEEN to DEFEAT_ZIRA
            if (source.getEntity() instanceof ServerPlayer player) {
                WorldData data = WorldData.get(serverLevel);
                data.getQuestManager()
                        .tryAdvance(OutlandsQuestline.QUEST_ID, player, QuestTrigger.TERMITE_QUEEN_KILLED);
            }
        }
        super.die(source);
    }

    private void spawnTermite(boolean exploding) {
        int nearbyCount = this.level()
                .getEntitiesOfClass(TermiteEntity.class, this.getBoundingBox().inflate(TERMITE_SEARCH_RADIUS))
                .size();
        if (nearbyCount >= MAX_NEARBY_TERMITES) return;

        TermiteEntity termite = EntityTypes.TERMITE.get().create(this.level());
        if (termite == null) return;
        termite.setExploding(exploding);
        termite.moveTo(
                this.getX() + this.getRandom().nextGaussian() * SPAWN_OFFSET_SPREAD,
                this.getY(),
                this.getZ() + this.getRandom().nextGaussian() * SPAWN_OFFSET_SPREAD,
                this.getRandom().nextFloat() * 360.0F,
                0.0F);
        if (this.getTarget() != null) {
            termite.setTarget(this.getTarget());
        }
        this.level().addFreshEntity(termite);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource source, int lootingLevel, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);
        int nukShardCount = MIN_NUKA_SHARDS + this.getRandom().nextInt(EXTRA_NUKA_SHARDS);
        for (int i = 0; i < nukShardCount; i++) {
            this.spawnAtLocation(new ItemStack(LionKingItems.NUKA_SHARD.get()));
        }
        int crystalCount = MIN_CRYSTALS + this.getRandom().nextInt(EXTRA_CRYSTALS);
        for (int i = 0; i < crystalCount; i++) {
            this.spawnAtLocation(new ItemStack(LionKingItems.CRYSTAL.get()));
        }
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.ARMOR, ARMOR)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE);
    }

    @Override
    public void registerControllers(@NotNull AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainController)
                .triggerableAnim("animation.termite_queen.attack", ATTACK_ANIM));
    }

    private PlayState mainController(@NotNull AnimationState<TermiteQueenEntity> state) {
        if (this.dead) {
            return state.setAndContinue(DEATH_ANIM);
        }
        if (state.isMoving()) {
            return state.setAndContinue(WALK_ANIM);
        }
        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        triggerAnim("main", "animation.termite_queen.attack");
        return super.doHurtTarget(target);
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
