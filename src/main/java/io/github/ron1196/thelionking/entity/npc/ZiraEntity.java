package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.LKWorldData;
import io.github.ron1196.thelionking.entity.LKLightningBoltEntity;
import io.github.ron1196.thelionking.quest.LKCharacterSpeech;
import io.github.ron1196.thelionking.quest.LKQuestManager;
import io.github.ron1196.thelionking.quest.LKQuestRegistry;
import io.github.ron1196.thelionking.quest.LKQuestTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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

public class ZiraEntity extends Monster {

    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(ZiraEntity.class, EntityDataSerializers.BOOLEAN);

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
            this.targetSelector.addGoal(2,
                    new NearestAttackableTargetGoal<>(this, Player.class, true));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;

        if (!level().isClientSide && isHostile() && !spawnedBossFightOutlanders && getHealth() <= 120F) {
            spawnedBossFightOutlanders = true;
            broadcastMessage("\u00a7e<Zira> \u00a7fOutlanders! Finish this!");
            spawnOutlandersWithLightning(4);
        }
    }

    private void spawnOutlandersWithLightning(int count) {
        for (int i = 0; i < count; i++) {
            int x = Mth.floor(getX()) - 6 + random.nextInt(13);
            int z = Mth.floor(getZ()) - 6 + random.nextInt(13);
            int y = level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    new BlockPos(x, 0, z)).getY();
            level().addFreshEntity(new LKLightningBoltEntity(level(), x, y, z, 0, null));
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (isHostile()) return InteractionResult.PASS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
        if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        talkCooldown = 40;
        LKWorldData data = LKWorldData.get(serverLevel);
        LKQuestManager quests = data.getQuestManager();
        int stage = quests.getStage("outlands");

        // Try to advance the quest
        if (quests.tryAdvance("outlands", serverPlayer, LKQuestTrigger.ZIRA_TALK)) {
            sendStageDialogue(player, quests.getStage("outlands"));
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case LKQuestRegistry.OUTLANDS_COLLECT_INGOTS -> sendSpeech(player, LKCharacterSpeech.ZIRA_INGOTS);
            case LKQuestRegistry.OUTLANDS_COLLECT_FEATHERS -> sendSpeech(player, LKCharacterSpeech.ZIRA_FEATHERS);
            default -> {
                if (stage >= LKQuestRegistry.OUTLANDS_FOLLOW_OUTLANDERS && !isHostile()) {
                    sendSpeech(player, LKCharacterSpeech.ZIRA_CONQUEST);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void sendStageDialogue(Player player, int newStage) {
        String message = switch (newStage) {
            case LKQuestRegistry.OUTLANDS_COLLECT_INGOTS ->
                    "So... a human dares to enter my domain. Perhaps you can be of use to me.";
            case LKQuestRegistry.OUTLANDS_THROW_IN_OUTWATER ->
                    "Good. Now throw these ingots into the Outwater.";
            case LKQuestRegistry.OUTLANDS_FOLLOW_OUTLANDERS ->
                    "Excellent. You have served me well. Now... follow my Outlanders.";
            default -> null;
        };
        if (message != null) sendMessage(player, message);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                LKWorldData data = LKWorldData.get(serverLevel);
                data.getQuestManager().tryAdvance("outlands", serverPlayer, LKQuestTrigger.ZIRA_KILLED);

                serverPlayer.sendSystemMessage(Component.literal(
                        "\u00a7e<Zira> \u00a7fThis is not over... Scar's legacy will live on..."));
            }

            level().explode(this, getX(), getY(), getZ(), 0F, Level.ExplosionInteraction.NONE);
            for (int i = 0; i < 5; i++) {
                int x = Mth.floor(getX()) - 12 + random.nextInt(25);
                int z = Mth.floor(getZ()) - 12 + random.nextInt(25);
                int y = level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                        new BlockPos(x, 0, z)).getY();
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
        player.sendSystemMessage(Component.literal("\u00a7e<Zira> \u00a7f" + message));
    }

    private void sendSpeech(Player player, LKCharacterSpeech speech) {
        player.sendSystemMessage(Component.literal(LKCharacterSpeech.giveSpeech(speech)));
    }

    private void broadcastMessage(String message) {
        for (Player p : level().players()) {
            p.sendSystemMessage(Component.literal(message));
        }
    }
}
