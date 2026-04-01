package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.ai.PumbaaFollowTimonGoal;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.util.ChatHelper;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PumbaaEntity extends PathfinderMob {

    private static final int TALK_COOLDOWN_TICKS = 140;

    private int talkCooldown = 0;

    public PumbaaEntity(EntityType<? extends PumbaaEntity> type, Level level) {
        super(type, level);
        this.setCustomName(Component.literal("Pumbaa"));
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
        OutlandsQuestline.Stage stageKey = qm.getStage("outlands", OutlandsQuestline.Stage.class);

        switch (stageKey) {
            case TALK_TO_PUMBAA -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                handleIntroDialogue(player, serverPlayer, qm);
            }
            case GATHER_PUMBAA_INGREDIENTS -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                if (qm.tryAdvance("outlands", serverPlayer, QuestTrigger.PUMBAA_TALK)) {
                    ChatHelper.sendNpcMessage(player, "Pumbaa", "Stand back!");
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

    private static final String[][] INTRO_DIALOGUE = {
        {"Pumbaa", "Hi there, kid."},
        {"Timon", "You look down. Can we help?"},
        {"Pumbaa", "What's that you say? Outlanders have taken over Rafiki's tree?"},
        {"Timon", "Outlanders? Man, I hate Outlanders. Almost as much as I hate hyenas, and I HATE hyenas."},
        {
            "Timon",
            "Hold on! Pumbaa here could - er, pass gas, and those Outlanders would move out of that tree faster than a wildebeest stampede!"
        },
        {
            "Timon",
            "Bring Pumbaa some planks, sixteen bugs, a jar of lava and a thrown termite, and we'll cook up some weapons of gas destruction."
        }
    };

    private void handleIntroDialogue(Player player, ServerPlayer serverPlayer, QuestlineManager qm) {
        WorldData data = WorldData.get(serverPlayer.serverLevel());
        int talkIndex = data.getPumbaaTalkCount();

        if (talkIndex < INTRO_DIALOGUE.length) {
            ChatHelper.sendNpcMessage(player, INTRO_DIALOGUE[talkIndex][0], INTRO_DIALOGUE[talkIndex][1]);
            data.incrementPumbaaTalkCount();
        }

        if (talkIndex >= INTRO_DIALOGUE.length - 1) {
            qm.tryAdvance("outlands", serverPlayer, QuestTrigger.PUMBAA_TALK);
            data.resetPumbaaTalkCount();
        }
    }

    private void spawnPumbaaBox() {
        if (level().isClientSide()) return;
        spawnFartParticles();
        level().playSound(null, blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.8F, 1.0F);
        var boxPos = new Vec3(getX() + 0.5, getY() + 0.5, getZ() + 0.5);
        var boxStack = new ItemStack(LionKingBlocks.PUMBAA_BOX.get());
        var item = new ItemEntity(level(), boxPos.x, boxPos.y, boxPos.z, boxStack);
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
        ChatHelper.sendNpcMessage(player, speaker, speeches[index]);
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
        ChatHelper.sendNpcMessage(player, "Pumbaa", speeches[random.nextInt(speeches.length)]);
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
