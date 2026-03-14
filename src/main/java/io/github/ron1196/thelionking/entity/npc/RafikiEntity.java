package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.LKPlayerData;
import io.github.ron1196.thelionking.data.LKPlayerDataProvider;
import io.github.ron1196.thelionking.data.LKWorldData;
import io.github.ron1196.thelionking.network.LKNetworking;
import io.github.ron1196.thelionking.network.PlayerDataSyncPacket;
import io.github.ron1196.thelionking.quest.ClaimableReward;
import io.github.ron1196.thelionking.quest.LKCharacterSpeech;
import io.github.ron1196.thelionking.quest.LKQuestline;
import io.github.ron1196.thelionking.quest.LKQuestManager;
import io.github.ron1196.thelionking.quest.LKQuestRegistry;
import io.github.ron1196.thelionking.quest.LKQuestTrigger;
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

import java.util.List;

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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;
        if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;

        talkCooldown = 40;
        LKWorldData data = LKWorldData.get(serverLevel);
        LKQuestManager quests = data.getQuestManager();
        LKPlayerData playerData = LKPlayerDataProvider.get(serverPlayer);
        int stage = quests.getStage("rafiki");

        // Give quest book on first meeting
        if (!playerData.hasReceivedQuestBook()) {
            playerData.setReceivedQuestBook(true);
            player.addItem(new ItemStack(LKItems.QUEST_BOOK.get()));
            syncPlayerData(serverPlayer, playerData);
        }

        // Try to claim the next unclaimed reward (earliest stage first)
        int claimedStage = tryClaimNextReward(serverPlayer, playerData, quests);
        if (claimedStage >= 0) {
            sendStageDialogue(player, claimedStage);
            syncPlayerData(serverPlayer, playerData);
            return InteractionResult.SUCCESS;
        }

        // Try to advance the quest
        if (quests.tryAdvance("rafiki", serverPlayer, LKQuestTrigger.RAFIKI_TALK)) {
            sendStageDialogue(player, quests.getStage("rafiki"));
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case LKQuestRegistry.RAFIKI_COLLECT_BONES -> sendSpeech(player, LKCharacterSpeech.HYENA_BONES);
            case LKQuestRegistry.RAFIKI_DEFEAT_SCAR -> sendSpeech(player, LKCharacterSpeech.MENTION_SCAR);
            case LKQuestRegistry.RAFIKI_COLLECT_TERMITES -> sendSpeech(player, LKCharacterSpeech.TERMITES);
            case LKQuestRegistry.RAFIKI_COLLECT_MANGOES -> sendSpeech(player, LKCharacterSpeech.MANGOES);
            case LKQuestRegistry.RAFIKI_USE_STAR_ALTAR -> sendSpeech(player, LKCharacterSpeech.STAR_ALTAR);
            default -> {
                if (quests.isComplete("rafiki")) sendSpeech(player, LKCharacterSpeech.HINT);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private int tryClaimNextReward(ServerPlayer player, LKPlayerData playerData, LKQuestManager quests) {
        LKQuestline quest = LKQuestRegistry.get("rafiki");
        int currentStage = quests.getStage("rafiki");
        for (int stage = 0; stage < currentStage; stage++) {
            List<ClaimableReward> rewards = quest.getClaimableRewards(stage);
            for (ClaimableReward reward : rewards) {
                if (!playerData.hasClaimedReward(reward.rewardKey())) {
                    player.addItem(new ItemStack(reward.item().get(), reward.count()));
                    playerData.claimReward(reward.rewardKey());
                    return stage;
                }
            }
        }
        return -1;
    }

    private void syncPlayerData(ServerPlayer player, LKPlayerData data) {
        LKNetworking.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new PlayerDataSyncPacket(data)
        );
    }

    private void sendStageDialogue(Player player, int newStage) {
        String message = switch (newStage) {
            case LKQuestRegistry.RAFIKI_COLLECT_BONES ->
                    "Welcome to the Pride Lands! I am Rafiki. Bring me sixty-four hyena bones and I will give you my stick.";
            case LKQuestRegistry.RAFIKI_DEFEAT_SCAR ->
                    "Excellent! Here is my stick. Now go and defeat Scar!";
            case LKQuestRegistry.RAFIKI_COLLECT_TERMITES ->
                    "Well done! Scar has been defeated. Now bring me four ground termites.";
            case LKQuestRegistry.RAFIKI_COLLECT_MANGOES ->
                    "Good! Now bring me four ground mangoes.";
            case LKQuestRegistry.RAFIKI_USE_STAR_ALTAR ->
                    "Perfect! Now craft a Star Altar and use the Rafiki Dust on it.";
            case LKQuestRegistry.RAFIKI_COMPLETE ->
                    "Wonderful! The spirits of the great kings smile upon you!";
            default -> null;
        };
        if (message != null) sendMessage(player, message);
    }

    private void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<Rafiki> \u00a7f" + message));
    }

    private void sendSpeech(Player player, LKCharacterSpeech speech) {
        player.sendSystemMessage(Component.literal(LKCharacterSpeech.giveSpeech(speech)));
    }
}
