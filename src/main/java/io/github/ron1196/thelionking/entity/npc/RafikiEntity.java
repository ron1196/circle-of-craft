package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.quest.LKCharacterSpeech;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import io.github.ron1196.thelionking.registry.LKItems;
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
        // Rafiki is invulnerable
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;
        // Heal to full
        if (this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;

        talkCooldown = 40;

        int questStage = LKQuestBase.RAFIKI_QUEST.getQuestStage();
        ItemStack held = player.getItemInHand(hand);

        // Stage 0: First meeting
        if (questStage == 0) {
            sendMessage(player, "Welcome to the Pride Lands! I am Rafiki. Bring me sixty-four hyena bones and I will give you my stick.");
            LKQuestBase.RAFIKI_QUEST.progress(1);
            LKQuestBase.updateAllQuests();
            return InteractionResult.SUCCESS;
        }

        // Stage 1: Waiting for hyena bones
        if (questStage == 1) {
            if (held.is(LKItems.HYENA_BONE.get()) && held.getCount() >= 64) {
                held.shrink(64);
                player.addItem(new ItemStack(LKItems.STAFF.get()));
                sendMessage(player, "Excellent! Here is my stick. Now go and defeat Scar!");
                LKQuestBase.RAFIKI_QUEST.progress(2);
                LKQuestBase.updateAllQuests();
            } else {
                sendSpeech(player, LKCharacterSpeech.HYENA_BONES);
            }
            return InteractionResult.SUCCESS;
        }

        // Stage 2: Waiting for Scar to be defeated
        if (questStage == 2) {
            sendSpeech(player, LKCharacterSpeech.MENTION_SCAR);
            return InteractionResult.SUCCESS;
        }

        // Stage 3: Return after defeating Scar
        if (questStage == 3) {
            sendMessage(player, "Well done! Scar has been defeated. Now bring me four ground termites.");
            LKQuestBase.RAFIKI_QUEST.progress(4);
            LKQuestBase.updateAllQuests();
            return InteractionResult.SUCCESS;
        }

        // Stage 4: Waiting for ground termites
        if (questStage == 4) {
            if (held.is(LKItems.TERMITE_DUST.get()) && held.getCount() >= 4) {
                held.shrink(4);
                sendMessage(player, "Good! Now bring me four ground mangoes.");
                LKQuestBase.RAFIKI_QUEST.progress(5);
                LKQuestBase.updateAllQuests();
            } else {
                sendSpeech(player, LKCharacterSpeech.TERMITES);
            }
            return InteractionResult.SUCCESS;
        }

        // Stage 5: Waiting for ground mangoes
        if (questStage == 5) {
            if (held.is(LKItems.MANGO_DUST.get()) && held.getCount() >= 4) {
                held.shrink(4);
                sendMessage(player, "Perfect! Now craft a Star Altar and use the Rafiki Dust on it.");
                LKQuestBase.RAFIKI_QUEST.progress(6);
                LKQuestBase.updateAllQuests();
            } else {
                sendSpeech(player, LKCharacterSpeech.MANGOES);
            }
            return InteractionResult.SUCCESS;
        }

        // Stage 6: Waiting for Star Altar usage
        if (questStage == 6) {
            sendSpeech(player, LKCharacterSpeech.STAR_ALTAR);
            return InteractionResult.SUCCESS;
        }

        // Quest complete — give hints
        if (LKQuestBase.RAFIKI_QUEST.isComplete()) {
            sendSpeech(player, LKCharacterSpeech.HINT);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.SUCCESS;
    }

    private void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<Rafiki> \u00a7f" + message));
    }

    private void sendSpeech(Player player, LKCharacterSpeech speech) {
        player.sendSystemMessage(Component.literal(LKCharacterSpeech.giveSpeech(speech)));
    }
}
