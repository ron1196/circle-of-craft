package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.quest.animal.CharacterSpeech;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.network.chat.Component;
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

public class TimonEntity extends PathfinderMob {

    private int talkCooldown = 0;
    private boolean hasGivenFirstBugs = false;

    public TimonEntity(EntityType<? extends TimonEntity> type, Level level) {
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
        return false; // Invulnerable NPC
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        talkCooldown = 120;

        ItemStack held = player.getItemInHand(hand);

        // Accept bugs for trade
        if (held.is(LKItems.BUG.get()) && held.getCount() >= 5) {
            held.shrink(5);
            // Give random reward
            int reward = random.nextInt(3);
            switch (reward) {
                case 0 -> player.addItem(new ItemStack(LKItems.PUMBAA_BOMB.get(), 3));
                case 1 -> player.addItem(new ItemStack(LKItems.CRYSTAL.get(), 1));
                case 2 -> player.giveExperiencePoints(50);
            }
            hasGivenFirstBugs = true;
            sendMessage(player, "Slimy, yet satisfying! Here's a little something for you.");
            return InteractionResult.SUCCESS;
        }

        // Regular speech
        if (hasGivenFirstBugs) {
            sendSpeech(player, CharacterSpeech.MORE_BUGS);
        } else {
            sendSpeech(player, CharacterSpeech.BUGS);
        }
        return InteractionResult.SUCCESS;
    }

    private void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<Timon> \u00a7f" + message));
    }

    private void sendSpeech(Player player, CharacterSpeech speech) {
        player.sendSystemMessage(Component.literal(CharacterSpeech.giveSpeech(speech)));
    }
}
