package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.entity.ai.PumbaaFollowTimonGoal;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PumbaaEntity extends PathfinderMob {

    private static final int TALK_COOLDOWN_TICKS = 40;
    private static final int COOKING_EAT_START = 20;
    private static final int COOKING_EAT_END = 48;
    private static final int COOKING_EAT_INTERVAL = 4;
    private static final int COOKING_SPAWN_TICK = 100;
    private static final int COOKING_DONE_TICK = 140;
    private static final int FART_PARTICLE_COUNT = 14;

    private int talkCooldown = 0;
    private boolean cookingBox = false;
    private int cookingTimer = 0;

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
        if (this.getHealth() < this.getMaxHealth()) this.setHealth(this.getMaxHealth());

        if (!level().isClientSide() && random.nextInt(1200) == 0) {
            fart();
        }

        if (cookingBox) {
            tickCookingAnimation();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    private void tickCookingAnimation() {
        cookingTimer++;

        // Eating/munching sounds
        if (cookingTimer >= COOKING_EAT_START
                && cookingTimer <= COOKING_EAT_END
                && cookingTimer % COOKING_EAT_INTERVAL == 0) {
            level().playSound(
                            null,
                            blockPosition(),
                            SoundEvents.GENERIC_EAT,
                            SoundSource.NEUTRAL,
                            0.8F + 0.5F * random.nextInt(2),
                            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        }

        // Spawn the box + fart
        if (cookingTimer >= COOKING_SPAWN_TICK && !level().isClientSide()) {
            spawnPumbaaBox();
            cookingBox = false;
            cookingTimer = 0;
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
                    cookingBox = true;
                    cookingTimer = 0;
                } else {
                    sendMissingIngredientsDialogue(player);
                }
            }
            case USE_PUMBAA_BOX -> {
                talkCooldown = TALK_COOLDOWN_TICKS;
                // If the player lost their Pumbaa Box, let them re-craft with ingredients
                if (!playerHasPumbaaBox(player)) {
                    if (hasBoxIngredients(player)) {
                        consumeBoxIngredients(player);
                        ChatHelper.sendNpcMessage(player, "Pumbaa", "Stand back! Here's another one.");
                        cookingBox = true;
                        cookingTimer = 0;
                    } else {
                        sendMissingIngredientsDialogue(player);
                    }
                } else {
                    ChatHelper.sendNpcMessage(
                            player, "Timon", "You already have the box! Go place it near Rafiki's tree.");
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

        // Pumbaa jumps
        setDeltaMovement(0, 1.5, 0);

        // Spawn the box item
        var boxStack = new ItemStack(LionKingBlocks.PUMBAA_BOX.get());
        var item = new ItemEntity(level(), getX() + 0.5, getY() + 0.5, getZ() + 0.5, boxStack);
        level().addFreshEntity(item);

        // Fart sound + particles
        fart();
    }

    private void fart() {
        level().playSound(
                        null,
                        blockPosition(),
                        LionKingSoundEvents.FLATULENCE.get(),
                        SoundSource.NEUTRAL,
                        0.5F,
                        (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
        if (level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < FART_PARTICLE_COUNT; i++) {
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        getX() + (random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(),
                        getY() + 0.5 + random.nextFloat() * getBbHeight(),
                        getZ() + (random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(),
                        1,
                        random.nextGaussian() * 0.02,
                        random.nextGaussian() * 0.02,
                        random.nextGaussian() * 0.02,
                        0);
            }
        }
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

    // ── Pumbaa Box ingredient helpers ───────────────────────────────────────

    private static final int REQUIRED_BUG_COUNT = 16;

    private static boolean playerHasPumbaaBox(@NotNull Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(LionKingBlocks.PUMBAA_BOX.get().asItem())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasBoxIngredients(@NotNull Player player) {
        return countInInventory(player, LionKingItems.BUG.get()) >= REQUIRED_BUG_COUNT
                && countInInventory(player, Items.OAK_PLANKS) >= 1
                && countInInventory(player, LionKingItems.JAR_LAVA.get()) >= 1
                && countInInventory(player, LionKingItems.TERMITE_THROWN.get()) >= 1;
    }

    private static void consumeBoxIngredients(@NotNull Player player) {
        shrinkFromInventory(player, LionKingItems.BUG.get(), REQUIRED_BUG_COUNT);
        shrinkFromInventory(player, Items.OAK_PLANKS, 1);
        shrinkFromInventory(player, LionKingItems.JAR_LAVA.get(), 1);
        shrinkFromInventory(player, LionKingItems.TERMITE_THROWN.get(), 1);
    }

    private static int countInInventory(@NotNull Player player, Item item) {
        int total = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) total += stack.getCount();
        }
        return total;
    }

    private static void shrinkFromInventory(@NotNull Player player, Item item, int amount) {
        int remaining = amount;
        for (ItemStack stack : player.getInventory().items) {
            if (remaining <= 0) break;
            if (stack.is(item)) {
                int take = Math.min(remaining, stack.getCount());
                stack.shrink(take);
                remaining -= take;
            }
        }
    }
}
