package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.LKPlayerData;
import io.github.ron1196.thelionking.data.LKPlayerDataProvider;
import io.github.ron1196.thelionking.data.LKWorldData;
import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.PlayerDataSyncPacket;
import io.github.ron1196.thelionking.quest.animal.CharacterSpeech;
import io.github.ron1196.thelionking.quest.questline.LKQuestlineManager;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.thelionking.quest.stage.LKQuestTrigger;
import io.github.ron1196.thelionking.registry.LKItems;
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

    private int talkCooldown = 0;

    public RafikiEntity(EntityType<? extends RafikiEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;
        if (this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
        if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        talkCooldown = 40;
        LKWorldData data = LKWorldData.get(serverLevel);
        LKQuestlineManager quests = data.getQuestManager();
        LKPlayerData playerData = LKPlayerDataProvider.get(serverPlayer);
        Stage stage = quests.getStage("rafiki", Stage.class);

        // Give quest book on first meeting
        if (!playerData.hasReceivedQuestBook()) {
            playerData.setReceivedQuestBook(true);
            player.addItem(new ItemStack(LKItems.QUEST_BOOK.get()));
            syncPlayerData(serverPlayer, playerData);
        }

        // Try to claim the next unclaimed reward (earliest stage first)
        int claimedIndex = quests.tryClaimNextReward("rafiki", serverPlayer);
        if (claimedIndex >= 0) {
            // Re-fetch stage after claim
            sendStageDialogue(player, quests.getStage("rafiki", Stage.class));
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Try to advance the quest (rewards are given automatically in tryAdvance)
        if (quests.tryAdvance("rafiki", serverPlayer, LKQuestTrigger.RAFIKI_TALK)) {
            Stage newStage = quests.getStage("rafiki", Stage.class);
            sendStageDialogue(player, newStage);
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case COLLECT_BONES -> sendSpeech(player, CharacterSpeech.HYENA_BONES);
            case DEFEAT_SCAR -> sendSpeech(player, CharacterSpeech.MENTION_SCAR);
            case COLLECT_TERMITES -> sendSpeech(player, CharacterSpeech.TERMITES);
            case COLLECT_MANGOES -> sendSpeech(player, CharacterSpeech.MANGOES);
            case USE_STAR_ALTAR -> sendSpeech(player, CharacterSpeech.STAR_ALTAR);
            case COMPLETE -> sendSpeech(player, CharacterSpeech.HINT);
            default -> {}
        }

        return InteractionResult.SUCCESS;
    }

    private void syncPlayerData(ServerPlayer player, LKPlayerData data) {
        LKNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new PlayerDataSyncPacket(data)
        );
    }

    private void sendStageDialogue(Player player, Stage newStage) {
        String message = switch (newStage) {
            case COLLECT_BONES ->
                    "Welcome to the Pride Lands! I am Rafiki. Bring me sixty-four hyena bones and I will give you my stick.";
            case DEFEAT_SCAR ->
                    "Excellent! Here is my stick. Now go and defeat Scar!";
            case RETURN_AFTER_SCAR ->
                    "Well done! Scar has been defeated. Now come back and see me.";
            case COLLECT_TERMITES ->
                    "Well done! Scar has been defeated. Now bring me four ground termites.";
            case COLLECT_MANGOES ->
                    "Good! Now bring me four ground mangoes.";
            case USE_STAR_ALTAR ->
                    "Perfect! Now craft a Star Altar and use the Rafiki Dust on it.";
            case COMPLETE ->
                    "Wonderful! The spirits of the great kings smile upon you!";
            default -> null;
        };
        if (message != null) sendMessage(player, message);
    }

    private void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<Rafiki> \u00a7f" + message));
    }

    private void sendSpeech(Player player, CharacterSpeech speech) {
        player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(speech)));
    }
}
