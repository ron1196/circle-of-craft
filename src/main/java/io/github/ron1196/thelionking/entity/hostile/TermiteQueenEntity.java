package io.github.ron1196.thelionking.entity.hostile;

import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
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

public class TermiteQueenEntity extends Monster {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.thelionking.termite_queen"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private int spawnCooldown = 0;

    public TermiteQueenEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 500;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            if (this.getTarget() != null && this.isAlive()) {
                double dist = this.distanceTo(this.getTarget());
                if (dist > 6.0 && spawnCooldown <= 0) {
                    spawnTermite();
                    spawnCooldown = 60;
                }
            }
            if (spawnCooldown > 0) {
                spawnCooldown--;
            }
        }
    }

    private void spawnTermite() {
        // Cap at 8 nearby termites
        int nearbyTermites = this.level().getEntitiesOfClass(TermiteEntity.class,
                this.getBoundingBox().inflate(24.0)).size();
        if (nearbyTermites >= 8) return;

        int count = 1 + this.getRandom().nextInt(3); // 1-3 termites per spawn
        for (int i = 0; i < count && (nearbyTermites + i) < 8; i++) {
            TermiteEntity termite = LKEntityTypes.TERMITE.get().create(this.level());
            if (termite != null) {
                termite.moveTo(this.getX() + this.getRandom().nextGaussian() * 2.0,
                        this.getY(), this.getZ() + this.getRandom().nextGaussian() * 2.0,
                        this.getRandom().nextFloat() * 360.0F, 0.0F);
                this.level().addFreshEntity(termite);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource source, int lootingLevel, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);
        int nukShardCount = 5 + this.getRandom().nextInt(6); // 5-10
        for (int i = 0; i < nukShardCount; i++) {
            this.spawnAtLocation(new ItemStack(LKItems.NUKA_SHARD.get()));
        }
        int crystalCount = 1 + this.getRandom().nextInt(3); // 1-3
        for (int i = 0; i < crystalCount; i++) {
            this.spawnAtLocation(new ItemStack(LKItems.CRYSTAL.get()));
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }
}
