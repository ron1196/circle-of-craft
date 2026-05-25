package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.entity.ai.PumbaaFollowTimonGoal;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.NpcInteraction;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.registry.LionKingSoundEvents;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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

    public static final String REGISTRY_NAME = "pumbaa";

    private static final int TALK_COOLDOWN_TICKS = 40;
    private static final int COOKING_EAT_START = 20;
    private static final int COOKING_EAT_END = 48;
    private static final int COOKING_EAT_INTERVAL = 4;
    private static final int COOKING_SPAWN_TICK = 100;
    private static final int FART_PARTICLE_COUNT = 14;

    private final NpcBehavior questBehavior = new NpcBehavior(this, 30, null);
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
        questBehavior.tick();

        if (!level().isClientSide() && random.nextInt(1200) == 0) {
            fart();
        }

        if (cookingBox) {
            tickCookingAnimation();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        questBehavior.saveToNbt(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        questBehavior.loadFromNbt(tag);
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
        NpcInteraction ctx = NpcInteraction.tryCreate(player);
        if (ctx == null) return InteractionResult.SUCCESS;
        if (questBehavior.isOnCooldown()) return InteractionResult.SUCCESS;

        // RALLY_PUMBAA — haven't talked to Timon yet
        RafikiQuestline.Stage rafikiStage = ctx.stage(RafikiQuestline.QUEST_ID, RafikiQuestline.Stage.class);
        if (rafikiStage == RafikiQuestline.Stage.RALLY_PUMBAA) {
            questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
            sendRandomQuote(player);
            return InteractionResult.SUCCESS;
        }

        // COLLECT_BUGS — accept bugs after Timon intro
        if (rafikiStage == RafikiQuestline.Stage.COLLECT_BUGS) {
            questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
            handleCollectBugs(player, ctx);
            return InteractionResult.SUCCESS;
        }

        OutlandsQuestline.Stage stageKey = ctx.stage(OutlandsQuestline.QUEST_ID, OutlandsQuestline.Stage.class);

        switch (stageKey) {
            case TALK_TO_PUMBAA -> {
                questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
                handleIntroDialogue(player, ctx);
            }
            case GATHER_PUMBAA_INGREDIENTS -> {
                questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
                if (ctx.quests().tryAdvance(OutlandsQuestline.QUEST_ID, ctx.serverPlayer(), QuestTrigger.PUMBAA_TALK)) {
                    ChatHelper.sendNpcMessage(player, "Pumbaa", "Stand back! This is gonna be a big one!");
                    cookingBox = true;
                    cookingTimer = 0;
                } else {
                    sendMissingIngredientsDialogue(player);
                }
            }
            case USE_PUMBAA_BOX -> {
                questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
                // If the player lost their Pumbaa Box, let them re-craft with ingredients
                if (!playerHasPumbaaBox(player)) {
                    if (hasBoxIngredients(player)) {
                        consumeBoxIngredients(player);
                        ChatHelper.sendNpcMessage(
                                player, "Pumbaa", "Stand back! Here comes another one! I've been savin' this!");
                        cookingBox = true;
                        cookingTimer = 0;
                    } else {
                        sendMissingIngredientsDialogue(player);
                    }
                } else {
                    ChatHelper.sendNpcMessage(
                            player,
                            "Timon",
                            "You already GOT the box, pal! Go place it near Rafiki's tree! What are you waitin' for?");
                }
            }
            default -> {
                questBehavior.startCooldown(TALK_COOLDOWN_TICKS);
                sendRandomQuote(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private static final String[][] INTRO_DIALOGUE = {
        {"Pumbaa", "Well, hi there, kid! You don't look so good. Somethin' eatin' ya? Besides the bugs, I mean."},
        {"Timon", "Pumbaa, that's not how you — never mind. So what's the problem, pal?"},
        {"Pumbaa", "Wait — what's that you say? Outlanders have taken over Rafiki's tree?! Oh, that's terrible!"},
        {"Timon", "Outlanders?! Man, I HATE Outlanders. Almost as much as I hate hyenas, and I REALLY hate hyenas."},
        {
            "Timon",
            "Hold on! I got an idea! Pumbaa here could, uh... pass gas, and those Outlanders would evacuate that tree faster than a wildebeest stampede!"
        },
        {
            "Timon",
            "Bring Pumbaa some planks, sixteen bugs, a jar of lava and a thrown termite, and we'll cook up some weapons of gas destruction. Literally."
        }
    };

    private void handleIntroDialogue(@NotNull Player player, @NotNull NpcInteraction ctx) {
        int talkIndex = ctx.worldData().getPumbaaTalkCount();

        if (talkIndex < INTRO_DIALOGUE.length) {
            ChatHelper.sendNpcMessage(player, INTRO_DIALOGUE[talkIndex][0], INTRO_DIALOGUE[talkIndex][1]);
            ctx.worldData().incrementPumbaaTalkCount();
        }

        if (talkIndex >= INTRO_DIALOGUE.length - 1) {
            ctx.quests().tryAdvance(OutlandsQuestline.QUEST_ID, ctx.serverPlayer(), QuestTrigger.PUMBAA_TALK);
            ctx.worldData().resetPumbaaTalkCount();
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
            "I'm gonna need sixteen bugs to get myself goin', a jar of lava and a thrown termite "
                    + "to heat things up, and some planks to put it all in! It's a recipe!",
            "Listen, pal, the flatulence needs to be EXTREMELY powerful for there to be even a hope of "
                    + "this workin'. Get us those ingredients, pronto!",
            "Bring Pumbaa planks, sixteen bugs, a termite and a jar of lava and "
                    + "he'll give you the flatulence. Trust me, he's got PLENTY.",
            "Hey kid, we wanna help, we really do! But I just ain't got those ingredients yet! Hakuna Matata ain't gonna cut it here!"
        };
        int index = random.nextInt(speeches.length);
        String speaker = index % 2 == 0 ? "Pumbaa" : "Timon";
        ChatHelper.sendNpcMessage(player, speaker, speeches[index]);
    }

    private void sendRandomQuote(@NotNull Player player) {
        String[] speeches = {
            "They call me... MISTER PIG! Ahhh!",
            "Hakuna Matata! What a wonderful phrase! It means no worries!",
            "Are you talkin' to me? ARE you talkin' to me?!",
            "Timon! Timon! There's a human lookin' at me! What do I do?!",
            "I'm a sensitive soul, though I seem thick-skinned. And it hurt, that my friends never stood downwind!",
            "When I was a young warthooooog...",
            "Hakuna Matata! It's our problem-free philosophy!",
            "You know, kid, in times of danger, you just gotta look beyond what you see."
        };
        ChatHelper.sendNpcMessage(player, "Pumbaa", speeches[random.nextInt(speeches.length)]);
    }

    // ── Rafiki quest: RALLY_PUMBAA ────────────────────────────────────────

    private void handleCollectBugs(@NotNull Player player, @NotNull NpcInteraction ctx) {
        // tryAdvance checks for 4 bugs in inventory and consumes them
        if (!ctx.quests().tryAdvance(RafikiQuestline.QUEST_ID, ctx.serverPlayer(), QuestTrigger.PUMBAA_TALK)) {
            CharacterSpeech.sendSpeech(player, CharacterSpeech.PUMBAA_NEED_BUGS);
            return;
        }

        // Eating sounds
        for (int i = 0; i < 3; i++) {
            level().playSound(
                            null,
                            blockPosition(),
                            SoundEvents.GENERIC_EAT,
                            SoundSource.NEUTRAL,
                            0.8F + 0.5F * random.nextInt(2),
                            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        }

        // Fart effect
        fart();

        ctx.worldData().resetTimonRafikiTalkCount();

        ChatHelper.sendNpcMessage(
                player,
                "Pumbaa",
                "Ahh, slimy yet satisfying! Now THAT hit the spot! I'm ready to fight! HAKUNA MATATA!");
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
