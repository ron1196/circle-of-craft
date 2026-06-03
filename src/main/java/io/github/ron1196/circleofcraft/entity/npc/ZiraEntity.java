package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.entity.hostile.OutlanderEntity;
import io.github.ron1196.circleofcraft.entity.hostile.TermiteQueenEntity;
import io.github.ron1196.circleofcraft.entity.projectile.LightningBoltEntity;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.quest.NpcInteraction;
import io.github.ron1196.circleofcraft.quest.actions.OutlandsQuestActions;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline.Stage;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ZiraEntity extends Monster {

    public static final String REGISTRY_NAME = "zira";

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ZiraEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Zira"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);

    private static final int OUTLANDER_SPAWN_COUNT = 4;
    private static final double QUEEN_SEARCH_RADIUS = 64.0;
    private static final int REMOUNT_CHECK_INTERVAL = 20;

    private final NpcBehavior questBehavior = new NpcBehavior(this, 16, this::onQuestCheck);

    private boolean lowHpRage = false;
    private int remountTimer = 0;

    public ZiraEntity(EntityType<? extends ZiraEntity> type, Level level) {
        super(type, level);
        this.setCustomName(Component.literal("Zira"));
        this.setCustomNameVisible(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HOSTILE, false);
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
    }

    public boolean isHostile() {
        return this.entityData.get(DATA_HOSTILE);
    }

    public void setHostile(boolean hostile) {
        this.entityData.set(DATA_HOSTILE, hostile);
        if (hostile) {
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (!isHostile()) return;
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

        if (!level().isClientSide()) {
            bossEvent.setProgress(getHealth() / getMaxHealth());
        }

        if (isHostile()) {
            // Hostile mode: skip quest behavior, handle combat mechanics
            Level level = level();
            if (level.isClientSide) return;

            tryRemountQueen();
            if (!lowHpRage && getHealth() <= 120F && !isPassenger()) {
                lowHpRage = true;
                ChatHelper.broadcastNpcMessage(level(), "Zira", "Outlanders! FINISH THIS WRETCHED FOOL!");
                spawnOutlandersWithLightning();
            }
            return;
        }

        questBehavior.tick();
    }

    private boolean onQuestCheck(@NotNull ServerLevel serverLevel, @NotNull QuestlineManager quests) {
        Stage stage = quests.getStage(OutlandsQuestline.QUEST_ID, Stage.class);
        if (!OutlandsQuestActions.isTreeOccupationStage(stage)) {
            OutlandsQuestActions.ensureWorldState(serverLevel, stage);
            return true; // May have been discarded
        }

        if (stage == Stage.PUMBAA_BOX_EXPLODING) {
            OutlandsQuestActions.ensureWorldState(serverLevel, stage);
        }
        return false;
    }

    private void spawnOutlandersWithLightning() {
        Level level = level();
        for (int i = 0; i < OUTLANDER_SPAWN_COUNT; i++) {
            Vec3 nearbySurface = randomNearbySurface(level, 6);

            OutlanderEntity outlander = EntityTypes.OUTLANDER.get().create(level);
            if (outlander != null) {
                int outlanderX = Mth.floor(nearbySurface.x + 0.5);
                int outlanderZ = Mth.floor(nearbySurface.z + 0.5);
                BlockPos outlanderPos = new BlockPos(outlanderX, Mth.floor(nearbySurface.y), outlanderZ);
                outlander.moveTo(outlanderPos, random.nextFloat() * 360F, 0F);
                level.addFreshEntity(outlander);
            }

            level.addFreshEntity(new LightningBoltEntity(level, nearbySurface, 0, null));
        }
    }

    private void tryRemountQueen() {
        if (isPassenger()) return;
        if (++remountTimer < REMOUNT_CHECK_INTERVAL) return;
        remountTimer = 0;

        AABB searchBox = getBoundingBox().inflate(QUEEN_SEARCH_RADIUS);
        List<TermiteQueenEntity> queens =
                level().getEntitiesOfClass(TermiteQueenEntity.class, searchBox, e -> e.isAlive() && !e.isVehicle());
        if (queens.isEmpty()) return;

        startRiding(queens.get(0));
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!isHostile()) return false;
        if (isPassenger() && getVehicle() instanceof TermiteQueenEntity) return false;
        return super.hurt(source, amount);
    }

    private Vec3 randomNearbySurface(Level level, int radius) {
        int x = Mth.floor(getX()) - radius + random.nextInt(radius * 2 + 1);
        int z = Mth.floor(getZ()) - radius + random.nextInt(radius * 2 + 1);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        return new Vec3(x, y, z);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        NpcInteraction ctx = NpcInteraction.tryCreate(player);
        if (ctx == null) return InteractionResult.SUCCESS;
        if (isHostile()) return InteractionResult.PASS;
        if (questBehavior.isOnCooldown()) return InteractionResult.SUCCESS;

        questBehavior.startCooldown(40);
        Stage stage = ctx.stage(OutlandsQuestline.QUEST_ID, Stage.class);

        // Tree occupation — special 3-part dialogue
        if (stage == Stage.ZIRA_OCCUPIES_TREE) {
            handleTreeOccupationDialogue(player, ctx);
            return InteractionResult.SUCCESS;
        }

        // Standard path: try claim reward, then try advance
        if (ctx.tryClaimOrAdvance(
                OutlandsQuestline.QUEST_ID,
                Stage.class,
                QuestTrigger.ZIRA_TALK,
                null,
                s -> sendStageDialogue(player, s))) {
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case COLLECT_INGOTS -> CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_INGOTS);
            case THROW_IN_OUTWATER -> CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_OUTWATER);
            case COLLECT_FEATHERS -> CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_FEATHERS);
            default -> {
                if (ctx.quests().isStageAtOrPast(OutlandsQuestline.QUEST_ID, Stage.FOLLOW_OUTLANDERS) && !isHostile()) {
                    CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_CONQUEST);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void sendStageDialogue(Player player, Stage newStage) {
        String message =
                switch (newStage) {
                    case COLLECT_INGOTS -> "So... a human dares enter my domain. How amusing. Perhaps you can be of use to me after all.";
                    case THROW_IN_OUTWATER -> "Good. I've opened a lower cavern in this mound. There's a pool of Outwater down there — throw the ingots in and come back. Do NOT keep me waiting.";
                    case COLLECT_FEATHERS -> "The Outlandish Helm! That has some very useful properties. Now I need Wayward Feathers — throw colored feathers into the Outwater. Scar's plan demands it.";
                    case FOLLOW_OUTLANDERS -> "Excellent. You have served me well. Now... follow my Outlanders. And don't fall behind.";
                    default -> null;
                };
        if (message != null) ChatHelper.sendNpcMessage(player, "Zira", message);
    }

    private void handleTreeOccupationDialogue(@NotNull Player player, @NotNull NpcInteraction ctx) {
        int talkCount = ctx.worldData().getZiraTreeTalkCount();
        String message =
                switch (talkCount) {
                    case 0 -> "Ah, the Pride Lands! Just as I remember them. This tree will serve nicely as the beginning of our glorious conquest!";
                    case 1 -> "And don't worry about that idiotic Rafiki. I disposed of him personally. Would you like to hear the old fool's last words?";
                    default -> "'Find Timon and Pumbaa! They'll know what to do!' HA! As if a meerkat and a warthog could challenge ME.";
                };
        ChatHelper.sendNpcMessage(player, "Zira", message);

        if (talkCount >= 2) {
            ctx.quests().tryAdvance(OutlandsQuestline.QUEST_ID, ctx.serverPlayer(), QuestTrigger.ZIRA_TALK);
        } else {
            ctx.worldData().incrementZiraTreeTalkCount();
        }
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        if (level().isClientSide() || !(level() instanceof ServerLevel serverLevel)) return;

        if (source.getEntity() instanceof ServerPlayer serverPlayer) {
            WorldData data = WorldData.get(serverLevel);
            data.getQuestManager().tryAdvance(OutlandsQuestline.QUEST_ID, serverPlayer, QuestTrigger.ZIRA_KILLED);
            ChatHelper.broadcastNpcMessage(level(), "Zira", "This is not over... Scar's legacy... will NEVER die...");
        }

        level().explode(this, getX(), getY(), getZ(), 0F, Level.ExplosionInteraction.NONE);
        for (int i = 0; i < 5; i++) {
            Vec3 pos = randomNearbySurface(level(), 12);
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level());
            if (bolt != null) {
                bolt.moveTo(pos.x, pos.y, pos.z);
                bolt.setVisualOnly(true);
                level().addFreshEntity(bolt);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        spawnAtLocation(ModItems.ZIRA_RUG.get());
    }

    @Override
    protected int getBaseExperienceReward() {
        return 100;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hostile", isHostile());
        questBehavior.saveToNbt(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean("Hostile")) {
            setHostile(true);
        }
        questBehavior.loadFromNbt(tag);
    }
}
