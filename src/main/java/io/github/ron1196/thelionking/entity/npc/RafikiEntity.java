package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.PlayerData;
import io.github.ron1196.thelionking.data.PlayerDataProvider;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.network.PlayerDataSyncPacket;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import io.github.ron1196.thelionking.util.DirectionHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class RafikiEntity extends PathfinderMob {

    private static final int MAX_WANDER_DISTANCE = 10;
    private static final int LEASH_CHECK_INTERVAL = 100;

    private int talkCooldown = 0;
    private BlockPos homePos = null;
    private int leashCheckTimer = 0;

    public RafikiEntity(EntityType<? extends RafikiEntity> type, Level level) {
        super(type, level);
        this.setCustomName(Component.literal("Rafiki"));
        this.setCustomNameVisible(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;
        if (this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
        if (!level().isClientSide()) {
            if (level() instanceof ServerLevel serverLevel
                    && WorldData.get(serverLevel).isZiraOccupiesTree()) {
                spawnZiraAtPosition(serverLevel);
                this.discard();
                return;
            }
            teleportHomeIfTooFar();
        }
    }

    private void spawnZiraAtPosition(ServerLevel serverLevel) {
        if (!serverLevel
                .getEntitiesOfClass(ZiraEntity.class, getBoundingBox().inflate(30))
                .isEmpty()) {
            return;
        }
        ZiraEntity zira = EntityTypes.ZIRA.get().create(serverLevel);
        if (zira != null) {
            zira.moveTo(getX(), getY(), getZ(), getYRot(), 0F);
            zira.setHostile(false);
            zira.setPersistenceRequired();
            serverLevel.addFreshEntity(zira);
        }
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
        if (homePos != null) {
            tag.putInt("HomeX", homePos.getX());
            tag.putInt("HomeY", homePos.getY());
            tag.putInt("HomeZ", homePos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HomeX")) {
            homePos = new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"));
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
        if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        talkCooldown = 40;
        WorldData data = WorldData.get(serverLevel);
        QuestlineManager quests = data.getQuestManager();
        PlayerData playerData = PlayerDataProvider.get(serverPlayer);
        Stage stage = quests.getStage("rafiki", Stage.class);

        // Give quest book on first meeting
        if (!playerData.hasReceivedQuestBook()) {
            playerData.setReceivedQuestBook(true);
            player.addItem(new ItemStack(LionKingItems.QUEST_BOOK.get()));
            syncPlayerData(serverPlayer, playerData);
        }

        // Handle Outlands quest — RAFIKI_RETURNS stage
        OutlandsQuestline.Stage outlandsStage = quests.getStage("outlands", OutlandsQuestline.Stage.class);
        if (outlandsStage == OutlandsQuestline.Stage.RAFIKI_RETURNS) {
            if (quests.tryAdvance("outlands", serverPlayer, QuestTrigger.RAFIKI_TALK)) {
                ChatHelper.sendNpcMessage(
                        player,
                        "Rafiki",
                        "Now's your chance to put tings right. Go through dat portal and put an end to Zira's outlandish scheme!");
            } else {
                ChatHelper.sendNpcMessage(player, "Rafiki", "Go! De Outlands await you. Put an end to Zira's madness!");
            }
            return InteractionResult.SUCCESS;
        }

        // Try to claim the next unclaimed reward (earliest stage first)
        int claimedIndex = quests.tryClaimNextReward("rafiki", serverPlayer);
        if (claimedIndex >= 0) {
            // Re-fetch stage after claim
            sendStageDialogue(player, quests.getStage("rafiki", Stage.class));
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Try to advance the quest
        // DEFEAT_SCAR requires scarDefeated — block RAFIKI_TALK unless Scar is dead
        if (stage == Stage.DEFEAT_SCAR && !data.isScarDefeated()) {
            sendSpeech(player, CharacterSpeech.MENTION_SCAR);
            return InteractionResult.SUCCESS;
        }

        if (quests.tryAdvance("rafiki", serverPlayer, QuestTrigger.RAFIKI_TALK)) {
            Stage newStage = quests.getStage("rafiki", Stage.class);
            sendStageDialogue(player, newStage);
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case COLLECT_BONES -> sendSpeech(player, CharacterSpeech.HYENA_BONES);
            case COLLECT_TERMITES -> sendSpeech(player, CharacterSpeech.TERMITES);
            case COLLECT_MANGOES -> sendSpeech(player, CharacterSpeech.MANGOES);
            case USE_STAR_ALTAR -> sendSpeech(player, CharacterSpeech.STAR_ALTAR);
            case COMPLETE -> sendSpeech(player, CharacterSpeech.COMPLETE_HINT);
            default -> {}
        }

        return InteractionResult.SUCCESS;
    }

    private void syncPlayerData(ServerPlayer player, PlayerData data) {
        Networking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PlayerDataSyncPacket(data));
    }

    private void sendStageDialogue(Player player, Stage newStage) {
        String message =
                switch (newStage) {
                    case COLLECT_BONES -> "Ahh, welcome to de Pride Lands! I am Rafiki. Bring me sixty-four hyena bones and I will give you my stick, eh?";
                    case DEFEAT_SCAR -> getScarHint(player);
                    case COLLECT_TERMITES -> "Hah! You did it! Scar is no more! Now, dis portal will take you to de Outlands. Go dere and bring old Rafiki four termite dust, yes?";
                    case COLLECT_MANGOES -> "Very good! Now bring me four mango dust. De spirits are pleased wit your progress!";
                    case USE_STAR_ALTAR -> "Ahh, perfect! Now craft a Star Altar and use de Rafiki Dust on it. De ancestors are waiting!";
                    case COMPLETE -> "It is done! De spirits of de great kings smile upon you! Rafiki is very proud, hehe!";
                    default -> null;
                };
        if (message != null) ChatHelper.sendNpcMessage(player, "Rafiki", message);
    }

    private String getScarHint(Player player) {
        String base = "Excellent! Here is my stick — it is de only weapon dat can harm Scar! "
                + "Watch de hyenas... dey know where deir master hides. Follow dem, and you will find him!";
        if (level() instanceof ServerLevel serverLevel) {
            List<ScarEntity> scars = serverLevel.getEntitiesOfClass(
                    ScarEntity.class, player.getBoundingBox().inflate(250));
            if (!scars.isEmpty()) {
                String direction = DirectionHelper.getCompassDirection(
                        player.blockPosition(), scars.get(0).blockPosition());
                base += " I hear he was seen lurking in de caves " + direction + ".";
            }
        }
        return base;
    }

    private void sendSpeech(Player player, CharacterSpeech speech) {
        CharacterSpeech.sendSpeech(player, speech);
    }
}
