package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.NpcInteraction;
import io.github.ron1196.thelionking.quest.actions.OutlandsQuestActions;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import io.github.ron1196.thelionking.util.DirectionHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.NotNull;

public class RafikiEntity extends PathfinderMob {

    private static final int MAX_WANDER_DISTANCE = 10;
    private static final int LEASH_CHECK_INTERVAL = 100;
    private static final int QUEST_CHECK_INTERVAL = 100;

    private int talkCooldown = 0;
    private BlockPos homePos = null;
    private int leashCheckTimer = 0;
    private int questCheckTimer = 0;

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

        Level level = level();
        if (level.isClientSide) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        if (++questCheckTimer >= QUEST_CHECK_INTERVAL) {
            questCheckTimer = 0;
            QuestlineManager questManager = WorldData.get(serverLevel).getQuestManager();
            OutlandsQuestline.Stage stage = questManager.getStage("outlands", OutlandsQuestline.Stage.class);
            if (OutlandsQuestActions.isTreeOccupationStage(stage)) {
                OutlandsQuestActions.ensureWorldState(serverLevel, stage);
                return; // We may have been discarded
            }
        }

        teleportHomeIfTooFar();
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
        NpcInteraction ctx = NpcInteraction.tryCreate(player);
        if (ctx == null) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;

        talkCooldown = 40;

        // Give quest book on first meeting
        if (!ctx.playerData().hasReceivedQuestBook()) {
            ctx.playerData().setReceivedQuestBook(true);
            player.addItem(new ItemStack(LionKingItems.QUEST_BOOK.get()));
            ctx.syncPlayerData();
        }

        // Handle Outlands quest — RAFIKI_RETURNS stage
        OutlandsQuestline.Stage outlandsStage = ctx.stage("outlands", OutlandsQuestline.Stage.class);
        if (outlandsStage == OutlandsQuestline.Stage.RAFIKI_RETURNS) {
            if (ctx.quests().tryAdvance("outlands", ctx.serverPlayer(), QuestTrigger.RAFIKI_TALK)) {
                ChatHelper.sendNpcMessage(
                        player,
                        "Rafiki",
                        "Now's your chance to put tings right. Go through dat portal and put an end to Zira's outlandish scheme!");
            } else {
                ChatHelper.sendNpcMessage(player, "Rafiki", "Go! De Outlands await you. Put an end to Zira's madness!");
            }
            return InteractionResult.SUCCESS;
        }

        // Standard path: try claim reward, then try advance
        // (Must run before DEFEAT_SCAR pre-empt so unclaimed rewards are still claimed)
        if (ctx.tryClaimOrAdvance("rafiki", Stage.class, QuestTrigger.RAFIKI_TALK,
                s -> sendStageDialogue(player, s))) {
            return InteractionResult.SUCCESS;
        }

        // DEFEAT_SCAR — Scar must be killed (SCAR_KILLED trigger), just give hints
        Stage stage = ctx.stage("rafiki", Stage.class);
        if (stage == Stage.DEFEAT_SCAR) {
            sendSpeech(player, CharacterSpeech.MENTION_SCAR);
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
