package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.PlayerDataProvider;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.projectile.LightningBoltEntity;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.PlayerDataSyncPacket;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline.Stage;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.StageTrigger;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ZiraEntity extends Monster {

  private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
      SynchedEntityData.defineId(ZiraEntity.class, EntityDataSerializers.BOOLEAN);

  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          Component.literal("Zira"),
          BossEvent.BossBarColor.PURPLE,
          BossEvent.BossBarOverlay.PROGRESS);

  private static final int OUTLANDER_SPAWN_COUNT = 4;

  private int talkCooldown = 0;
  private boolean spawnedBossFightOutlanders = false;

  public ZiraEntity(EntityType<? extends ZiraEntity> type, Level level) {
    super(type, level);
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

    if (!level().isClientSide
        && isHostile()
        && !spawnedBossFightOutlanders
        && getHealth() <= 120F) {
      spawnedBossFightOutlanders = true;
      broadcastMessage();
      spawnOutlandersWithLightning();
    }
  }

  private void spawnOutlandersWithLightning() {
    for (int i = 0; i < OUTLANDER_SPAWN_COUNT; i++) {
      int x = Mth.floor(getX()) - 6 + random.nextInt(13);
      int z = Mth.floor(getZ()) - 6 + random.nextInt(13);
      int y =
          level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z)).getY();
      level().addFreshEntity(new LightningBoltEntity(level(), x, y, z, 0, null));
    }
  }

  @Override
  protected @NotNull InteractionResult mobInteract(
      @NotNull Player player, @NotNull InteractionHand hand) {
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

    // Try to claim the next unclaimed reward (earliest stage first)
    int claimedIndex = quests.tryClaimNextReward("outlands", serverPlayer);
    if (claimedIndex >= 0) {
      sendStageDialogue(player, quests.getStage("outlands", Stage.class));
      syncPlayerData(serverPlayer, playerData);
      return InteractionResult.SUCCESS;
    }

    // Try to advance the quest (rewards are given automatically in tryAdvance)
    if (quests.tryAdvance("outlands", serverPlayer, StageTrigger.ZIRA_TALK)) {
      syncPlayerData(serverPlayer, playerData);
      sendStageDialogue(player, quests.getStage("outlands", Stage.class));
      return InteractionResult.SUCCESS;
    }

    // Quest didn't advance — give contextual speech
    switch (stage) {
      case COLLECT_INGOTS -> sendSpeech(player, CharacterSpeech.ZIRA_INGOTS);
      case COLLECT_FEATHERS -> sendSpeech(player, CharacterSpeech.ZIRA_FEATHERS);
      default -> {
        if (quests.isStageAtOrPast("outlands", Stage.FOLLOW_OUTLANDERS) && !isHostile()) {
          sendSpeech(player, CharacterSpeech.ZIRA_CONQUEST);
        }
      }
    }

    return InteractionResult.SUCCESS;
  }

  private void sendStageDialogue(Player player, Stage newStage) {
    String message =
        switch (newStage) {
          case COLLECT_INGOTS ->
              "So... a human dares to enter my domain. Perhaps you can be of use to me.";
          case THROW_IN_OUTWATER -> "Good. Now throw these ingots into the Outwater.";
          case FOLLOW_OUTLANDERS ->
              "Excellent. You have served me well. Now... follow my Outlanders.";
          default -> null;
        };
    if (message != null) sendMessage(player, message);
  }

  @Override
  public void die(@NotNull DamageSource source) {
    super.die(source);
    if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
      if (source.getEntity() instanceof ServerPlayer serverPlayer) {
        WorldData data = WorldData.get(serverLevel);
        data.getQuestManager().tryAdvance("outlands", serverPlayer, StageTrigger.ZIRA_KILLED);

        serverPlayer.sendSystemMessage(
            Component.literal("§e<Zira> §fThis is not over... Scar's legacy will live on..."));
      }

      level().explode(this, getX(), getY(), getZ(), 0F, Level.ExplosionInteraction.NONE);
      for (int i = 0; i < 5; i++) {
        int x = Mth.floor(getX()) - 12 + random.nextInt(25);
        int z = Mth.floor(getZ()) - 12 + random.nextInt(25);
        int y =
            level()
                .getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    new BlockPos(x, 0, z))
                .getY();
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level());
        if (bolt != null) {
          bolt.moveTo(x, y, z);
          bolt.setVisualOnly(true);
          level().addFreshEntity(bolt);
        }
      }
    }
  }

  @Override
  public int getExperienceReward() {
    return 100;
  }

  private void sendMessage(Player player, String message) {
    player.sendSystemMessage(Component.literal("§e<Zira> §f" + message));
  }

  private void sendSpeech(Player player, CharacterSpeech speech) {
    player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(speech)));
  }

  private void syncPlayerData(ServerPlayer player, PlayerData data) {
    Networking.CHANNEL.send(
        PacketDistributor.PLAYER.with(() -> player), new PlayerDataSyncPacket(data));
  }

  private void broadcastMessage() {
    for (Player p : level().players()) {
      p.sendSystemMessage(Component.literal("§e<Zira> §fOutlanders! Finish this!"));
    }
  }
}
