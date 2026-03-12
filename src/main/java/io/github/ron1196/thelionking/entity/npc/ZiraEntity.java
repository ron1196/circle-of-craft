package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.entity.LKLightningBoltEntity;
import io.github.ron1196.thelionking.quest.LKCharacterSpeech;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.item.ItemStack;
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

        // Boss fight: when health drops below 120, summon outlanders with lightning
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

            // Visual lightning bolt (power 0 = no damage, just dramatic effect)
            level().addFreshEntity(new LKLightningBoltEntity(level(), x, y, z, 0, null));
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (isHostile()) return InteractionResult.PASS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        talkCooldown = 40;

        int questStage = LKQuestBase.OUTLANDS_QUEST.getQuestStage();
        ItemStack held = player.getItemInHand(hand);

        // Stage 1: First meeting
        if (questStage == 1) {
            sendMessage(player, "So... a human dares to enter my domain. Perhaps you can be of use to me.");
            LKQuestBase.OUTLANDS_QUEST.progress(2);
            LKQuestBase.updateAllQuests();
            return InteractionResult.SUCCESS;
        }

        // Stage 2: Waiting for ingots
        if (questStage == 2) {
            if (held.is(LKItems.KIVULITE.get()) && held.getCount() >= 5) {
                // Check for silver too
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(LKItems.SILVER_INGOT.get()) && stack.getCount() >= 2) {
                        held.shrink(5);
                        stack.shrink(2);
                        sendMessage(player, "Good. Now throw these ingots into the Outwater.");
                        LKQuestBase.OUTLANDS_QUEST.progress(3);
                        LKQuestBase.updateAllQuests();
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            sendSpeech(player, LKCharacterSpeech.ZIRA_INGOTS);
            return InteractionResult.SUCCESS;
        }

        // Stage 4: Waiting for feathers
        if (questStage == 4) {
            if (held.is(LKItems.WAYWARD_FEATHER.get()) && held.getCount() >= 3) {
                held.shrink(3);
                sendMessage(player, "Excellent. You have served me well. Now... follow my Outlanders.");
                LKQuestBase.OUTLANDS_QUEST.progress(5);
                LKQuestBase.updateAllQuests();
                return InteractionResult.SUCCESS;
            }
            sendSpeech(player, LKCharacterSpeech.ZIRA_FEATHERS);
            return InteractionResult.SUCCESS;
        }

        // Default speech based on quest state
        if (questStage >= 5 && !isHostile()) {
            sendSpeech(player, LKCharacterSpeech.ZIRA_CONQUEST);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!level().isClientSide()) {
            if (LKQuestBase.OUTLANDS_QUEST.getQuestStage() == 9) {
                LKQuestBase.OUTLANDS_QUEST.progress(10);
                LKQuestBase.updateAllQuests();
            }
            if (source.getEntity() instanceof Player player) {
                player.sendSystemMessage(Component.literal(
                        "\u00a7e<Zira> \u00a7fThis is not over... Scar's legacy will live on..."));
            }

            // Dramatic death: explosion and vanilla lightning bolts
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
