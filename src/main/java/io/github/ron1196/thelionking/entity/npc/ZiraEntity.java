package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.PlayerDataProvider;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.hostile.OutlanderEntity;
import io.github.ron1196.thelionking.entity.hostile.TermiteQueenEntity;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.PlayerDataSyncPacket;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.actions.OutlandsQuestActions;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline.Stage;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.util.ChatHelper;
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
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ZiraEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ZiraEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Zira"), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);

    private static final int OUTLANDER_SPAWN_COUNT = 4;
    private static final double QUEEN_SEARCH_RADIUS = 64.0;
    private static final int REMOUNT_CHECK_INTERVAL = 20;

    private int talkCooldown = 0;
    private static final int MAX_WANDER_DISTANCE = 15;
    private static final int LEASH_CHECK_INTERVAL = 100;
    private static final int QUEST_CHECK_INTERVAL = 100;

    private boolean lowHpRage = false;
    private BlockPos homePos = null;
    private int leashCheckTimer = 0;
    private int questCheckTimer = 0;
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HOSTILE, false);
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

        if (talkCooldown > 0) talkCooldown--;

        Level level = level();
        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        if (!isHostile()) {
            handleZiraQuest(serverLevel);
            return;
        }

        // Remount Termite Queen if dismounted during the queen fight
        tryRemountQueen();
        if (!lowHpRage && getHealth() <= 120F && !isPassenger()) {
            lowHpRage = true;
            ChatHelper.broadcastNpcMessage(level(), "Zira", "Outlanders! Finish this!");
            spawnOutlandersWithLightning();
        }
    }

    private void handleZiraQuest(ServerLevel serverLevel) {
        teleportHomeIfTooFar();

        if (++questCheckTimer < QUEST_CHECK_INTERVAL) return;

        questCheckTimer = 0;
        QuestlineManager questManager = WorldData.get(serverLevel).getQuestManager();
        Stage stage = questManager.getStage("outlands", Stage.class);
        if (!OutlandsQuestActions.isTreeOccupationStage(stage)) {
            OutlandsQuestActions.ensureWorldState(serverLevel, stage);
            return;
        }

        if (stage == Stage.PUMBAA_BOX_EXPLODING) {
            OutlandsQuestActions.ensureWorldState(serverLevel, stage);
        }
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
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (isHostile()) return InteractionResult.PASS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
        if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        talkCooldown = 40;
        WorldData data = WorldData.get(serverLevel);
        QuestlineManager quests = data.getQuestManager();
        PlayerData playerData = PlayerDataProvider.get(serverPlayer);
        Stage stage = quests.getStage("outlands", Stage.class);

        // Tree occupation — special 3-part dialogue
        if (stage == Stage.ZIRA_OCCUPIES_TREE) {
            handleTreeOccupationDialogue(player, serverPlayer, data, quests);
            return InteractionResult.SUCCESS;
        }

        // Try to claim the next unclaimed reward (earliest stage first)
        int claimedIndex = quests.tryClaimNextReward("outlands", serverPlayer);
        if (claimedIndex >= 0) {
            sendStageDialogue(player, quests.getStage("outlands", Stage.class));
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Try to advance the quest (rewards are given automatically in tryAdvance)
        if (quests.tryAdvance("outlands", serverPlayer, QuestTrigger.ZIRA_TALK)) {
            syncPlayerData(serverPlayer, playerData);
            sendStageDialogue(player, quests.getStage("outlands", Stage.class));
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case COLLECT_INGOTS -> CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_INGOTS);
            case COLLECT_FEATHERS -> CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_FEATHERS);
            default -> {
                if (quests.isStageAtOrPast("outlands", Stage.FOLLOW_OUTLANDERS) && !isHostile()) {
                    CharacterSpeech.sendSpeech(player, CharacterSpeech.ZIRA_CONQUEST);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void sendStageDialogue(Player player, Stage newStage) {
        String message =
                switch (newStage) {
                    case COLLECT_INGOTS -> "So... a human dares to enter my domain. Perhaps you can be of use to me.";
                    case THROW_IN_OUTWATER -> "Good. I've opened up a lower cavern in this mound. There's a pool of Outwater in there - throw the ingots in and come back.";
                    case COLLECT_FEATHERS -> "Ah, the Outlandish Helm! That has some very useful tricks. Now I need Wayward Feathers - throw colored feathers into the Outwater.";
                    case FOLLOW_OUTLANDERS -> "Excellent. You have served me well. Now... follow my Outlanders.";
                    default -> null;
                };
        if (message != null) ChatHelper.sendNpcMessage(player, "Zira", message);
    }

    private void handleTreeOccupationDialogue(
            Player player, ServerPlayer serverPlayer, WorldData data, QuestlineManager quests) {
        int talkCount = data.getZiraTreeTalkCount();
        String message =
                switch (talkCount) {
                    case 0 -> "Ah, the Pride Lands! Just as I remember them. This tree will serve well as the starting point for our conquest.";
                    case 1 -> "And don't worry, I disposed of that idiotic Rafiki who lived here. Would you like to hear the old fool's last words before we removed him?";
                    default -> "'Find Timon and Pumbaa! They'll know what to do!' Ha! As if you would even consider betraying the Outlanders.";
                };
        ChatHelper.sendNpcMessage(player, "Zira", message);

        if (talkCount >= 2) {
            quests.tryAdvance("outlands", serverPlayer, QuestTrigger.ZIRA_TALK);
        } else {
            data.incrementZiraTreeTalkCount();
        }
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        if (level().isClientSide() || !(level() instanceof ServerLevel serverLevel)) return;

        if (source.getEntity() instanceof ServerPlayer serverPlayer) {
            WorldData data = WorldData.get(serverLevel);
            data.getQuestManager().tryAdvance("outlands", serverPlayer, QuestTrigger.ZIRA_KILLED);
            ChatHelper.broadcastNpcMessage(level(), "Zira", "This is not over... Scar's legacy will live on...");
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
    public int getExperienceReward() {
        return 100;
    }

    private void teleportHomeIfTooFar() {
        if (homePos == null) {
            homePos = blockPosition();
            return;
        }

        if (++leashCheckTimer < LEASH_CHECK_INTERVAL) return;
        leashCheckTimer = 0;

        if (blockPosition().distSqr(homePos) > MAX_WANDER_DISTANCE * MAX_WANDER_DISTANCE) {
            this.moveTo(homePos.getX() + 0.5, homePos.getY(), homePos.getZ() + 0.5, getYRot(), getXRot());
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Hostile", isHostile());
        if (homePos != null) {
            tag.putInt("HomeX", homePos.getX());
            tag.putInt("HomeY", homePos.getY());
            tag.putInt("HomeZ", homePos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getBoolean("Hostile")) {
            setHostile(true);
        }
        if (tag.contains("HomeX")) {
            homePos = new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"));
        }
    }

    private void syncPlayerData(ServerPlayer player, PlayerData data) {
        Networking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PlayerDataSyncPacket(data));
    }
}
