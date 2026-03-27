package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.ai.PumbaaFollowTimonGoal;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.StageTrigger;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PumbaaEntity extends PathfinderMob {

    private static final int TALK_COOLDOWN_TICKS = 140;

    private int talkCooldown = 0;

    public PumbaaEntity(EntityType<? extends PumbaaEntity> type, Level level) {
        super(type, level);
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
        this.goalSelector.addGoal(1, new PumbaaFollowTimonGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;

        if (level().isClientSide() && random.nextInt(1200) == 0) {
            spawnFartParticles();
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

        ServerLevel serverLevel = (ServerLevel) level();
        QuestlineManager qm = WorldData.get(serverLevel.getServer().overworld()).getQuestManager();
        OutlandsQuestline.Stage stage = qm.getStage("outlands", OutlandsQuestline.Stage.class);

        switch (stage) {
            case TALK_TO_PUMBAA -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                sendChat(player, "Pumbaa", "Hi there, kid.");
                sendChat(player, "Timon", "You look down. Can we help?");
                sendChat(player, "Pumbaa", "What's that you say? Outlanders have taken over Rafiki's tree?");
                sendChat(
                        player,
                        "Timon",
                        "Outlanders? Man, I hate Outlanders. Almost as much as I hate hyenas, " + "and I HATE hyenas.");
                sendChat(
                        player,
                        "Timon",
                        "Hold on! Pumbaa here could - er, pass gas, and those Outlanders would "
                                + "move out of that tree faster than a wildebeest stampede!");
                sendChat(
                        player,
                        "Timon",
                        "Bring Pumbaa some planks, sixteen bugs, a jar of lava and a "
                                + "thrown termite, and we'll cook up some weapons of gas "
                                + "destruction.");
                qm.tryAdvance("outlands", serverPlayer, StageTrigger.PUMBAA_TALK);
            }
            case GATHER_PUMBAA_INGREDIENTS -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                if (qm.tryAdvance("outlands", serverPlayer, StageTrigger.PUMBAA_TALK)) {
                    sendChat(player, "Pumbaa", "Stand back!");
                    spawnPumbaaBox();
                } else {
                    sendMissingIngredientsDialogue(player);
                }
            }
            default -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                sendRandomQuote(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void spawnPumbaaBox() {
        if (level().isClientSide()) return;
        spawnFartParticles();
        level().playSound(null, blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.8F, 1.0F);
        ItemEntity item = new ItemEntity(
                level(), getX() + 0.5, getY() + 0.5, getZ() + 0.5, new ItemStack(LionKingBlocks.PUMBAA_BOX.get()));
        level().addFreshEntity(item);
    }

    private void sendMissingIngredientsDialogue(@NotNull Player player) {
        String[] speeches = {
            "I'll need sixteen bugs to get myself going, a jar of lava and a thrown termite "
                    + "to heat things up, and some planks to put it all in.",
            "The flatulence needs to be extremely powerful for there to be even a hope of "
                    + "this working. Get us those ingredients!",
            "Bring Pumbaa planks, sixteen bugs, a termite and a jar of lava and " + "he'll give you the flatulence.",
            "Kid, we want to help, but I just don't have those ingredients yet!"
        };
        int index = random.nextInt(speeches.length);
        String speaker = index % 2 == 0 ? "Pumbaa" : "Timon";
        sendChat(player, speaker, speeches[index]);
    }

    private void sendRandomQuote(@NotNull Player player) {
        String[] speeches = {
            "They call me... Mr. Pig!",
            "Hakuna Matata! What a wonderful phrase!",
            "Are you talking to me?",
            "Timon! There's a human looking at me!",
            "I'm a sensitive soul, though I seem thick-skinned.",
            "When I was a young warthog...",
            "It's our problem-free philosophy!"
        };
        sendChat(player, "Pumbaa", speeches[random.nextInt(speeches.length)]);
    }

    private void sendChat(@NotNull Player player, String name, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<" + name + "> \u00a7f" + message));
    }

    private void spawnFartParticles() {
        for (int i = 0; i < 5; i++) {
            level().addParticle(
                            ParticleTypes.SMOKE,
                            getX() - 0.5 + random.nextFloat(),
                            getY() + 0.5 + random.nextFloat(),
                            getZ() - 0.5 + random.nextFloat(),
                            0,
                            0.05,
                            0);
        }
    }
}
