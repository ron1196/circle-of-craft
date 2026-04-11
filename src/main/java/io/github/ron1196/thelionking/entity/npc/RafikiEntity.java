package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.NpcInteraction;
import io.github.ron1196.thelionking.quest.actions.OutlandsQuestActions;
import io.github.ron1196.thelionking.quest.actions.RafikiQuestActions;
import io.github.ron1196.thelionking.quest.questline.OutlandsQuestline;
import io.github.ron1196.thelionking.quest.questline.QuestlineManager;
import io.github.ron1196.thelionking.quest.questline.QuestlineState;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import io.github.ron1196.thelionking.util.DirectionHelper;
import java.util.List;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RafikiEntity extends PathfinderMob {

    // ── Lion Dust ceremony constants ─────────────────────────────────────────
    private static final int CEREMONY_LINE_INTERVAL = 120;
    private static final int CEREMONY_DUST_DROP_LINE = 3;
    private static final int CEREMONY_DUST_COUNT = 4;
    private static final String[] CEREMONY_DIALOGUE = {
        "I have long known dat de great kings of de past are up dere in de stars, watching over us.",
        "But it is only recently dat I have discovered a way to bring dem back to de Pride Lands.",
        "Wit de materials you have given me, I can create a dust so magical dat you can use it to undo Scar's work and return our king from de stars!",
        "Here it is! Now, listen carefully and I will tell you what to do wit it.",
        "You need to use three of de dust and three silver ingots to craft a Star Altar.",
        "Den place it outside, and use de last dust on it. If you need more dust, bring me de two ingredients again."
    };

    // ── Side interaction constants ────────────────────────────────────────────
    private static final int RAFIKI_COIN_COST = 3;
    private static final int EXTRA_STICK_BONE_COST = 64;

    private final NpcBehavior questBehavior = new NpcBehavior(this, 10, this::onQuestCheck);

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
        questBehavior.tick();

        if (!level().isClientSide() && level() instanceof ServerLevel serverLevel) {
            WorldData worldData = WorldData.get(serverLevel);
            if (worldData.isCeremonyActive()) {
                processCeremonyTick(serverLevel, worldData);
            }
        }
    }

    private boolean onQuestCheck(@NotNull ServerLevel serverLevel, @NotNull QuestlineManager quests) {
        // Validate Rafiki quest world state (Scar spawning, portal, etc.)
        Stage rafikiStage = quests.getStage("rafiki", Stage.class);
        RafikiQuestActions.ensureWorldState(serverLevel, rafikiStage);

        // Validate Outlands quest world state (tree occupation, etc.)
        OutlandsQuestline.Stage outlandsStage = quests.getStage("outlands", OutlandsQuestline.Stage.class);
        if (OutlandsQuestActions.isTreeOccupationStage(outlandsStage)) {
            OutlandsQuestActions.ensureWorldState(serverLevel, outlandsStage);
            return true; // We may have been discarded
        }
        return false;
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
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        NpcInteraction ctx = NpcInteraction.tryCreate(player);
        if (ctx == null) return InteractionResult.SUCCESS;
        if (questBehavior.isOnCooldown()) return InteractionResult.SUCCESS;

        questBehavior.startCooldown(40);

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
                        "Now is your chance to put tings right! Go through dat portal and put an end to Zira's outlandish scheme! De ancestors believe in you!");
            } else {
                ChatHelper.sendNpcMessage(
                        player, "Rafiki", "Go! De Outlands await you! Put an end to Zira's madness! Hehe!");
            }
            return InteractionResult.SUCCESS;
        }

        // Intercept LION_DUST_CEREMONY — timed dialogue, not standard advancement
        Stage stage = ctx.stage("rafiki", Stage.class);
        if (stage == Stage.LION_DUST_CEREMONY) {
            handleCeremonyInteraction(player, ctx);
            return InteractionResult.SUCCESS;
        }

        // Standard path: try claim reward, then try advance
        // (Must run before DEFEAT_SCAR pre-empt so unclaimed rewards are still claimed)
        if (ctx.tryClaimOrAdvance(
                "rafiki",
                Stage.class,
                QuestTrigger.RAFIKI_TALK,
                s -> sendClaimDialogue(player, s),
                s -> sendStageDialogue(player, s))) {
            return InteractionResult.SUCCESS;
        }

        // Side interactions — NPC trades when holding the right item
        if (trySideInteraction(player, ctx)) {
            return InteractionResult.SUCCESS;
        }

        // DEFEAT_SCAR — Scar must be killed (SCAR_KILLED trigger), just give hints
        if (stage == Stage.DEFEAT_SCAR) {
            sendSpeech(player, CharacterSpeech.MENTION_SCAR);
            return InteractionResult.SUCCESS;
        }

        // Quest didn't advance — give contextual speech
        switch (stage) {
            case CRAFT_RAFIKI_STICK -> sendSpeech(player, CharacterSpeech.CRAFT_STICK);
            case RALLY_PUMBAA, COLLECT_BUGS -> sendSpeech(player, CharacterSpeech.FIND_PUMBAA);
            case COLLECT_BONES -> sendSpeech(player, CharacterSpeech.HYENA_BONES);
            case RETURN_AFTER_SCAR -> ChatHelper.sendNpcMessage(
                    player, "Rafiki", "Come back to old Rafiki! I have something to show you! Ohohoho!");
            case COLLECT_TERMITES -> sendSpeech(player, CharacterSpeech.TERMITES);
            case COLLECT_MANGOES -> sendSpeech(player, CharacterSpeech.MANGOES);
            case USE_STAR_ALTAR -> sendSpeech(player, CharacterSpeech.STAR_ALTAR);
            case COMPLETE -> sendSpeech(player, CharacterSpeech.COMPLETE_HINT);
            default -> {}
        }

        return InteractionResult.SUCCESS;
    }

    private void sendClaimDialogue(Player player, Stage stage) {
        String message =
                switch (stage) {
                    case RALLY_PUMBAA -> "Here you go! Old Rafiki's finest work! Ohohoho!";
                    default -> null;
                };
        if (message != null) ChatHelper.sendNpcMessage(player, "Rafiki", message);
    }

    private void sendStageDialogue(Player player, Stage newStage) {
        String message =
                switch (newStage) {
                    case CRAFT_RAFIKI_STICK -> "Ahh, welcome to de Pride Lands! I am Rafiki! Bring old Rafiki a stick, a mango, and a bug, and I will craft you a stick of great power! Ohohoho!";
                    case RALLY_PUMBAA -> "Excellent work! Now take dis stick — you will need it! But you cannot face Scar alone, oh no! Go find Pumbaa and convince him to help you!";
                    case COLLECT_BONES -> "Pumbaa is on your side now! Hehe! Bring me sixty-four hyena bones and we can take de fight to dat coward Scar!";
                    case DEFEAT_SCAR -> getScarHint(player);
                    case RETURN_AFTER_SCAR -> "Hah! You did it! Scar is no more! De ancestors are rejoicing! Come back to old Rafiki when you are ready!";
                    case COLLECT_TERMITES -> "Now, go through dat portal to de Outlands. Bring old Rafiki four termite dust, yes?";
                    case COLLECT_MANGOES -> "Very good! De spirits are pleased wit your progress! Now bring me four mango dust!";
                    case LION_DUST_CEREMONY -> "Wonderful! You have brought me everything I need! Come speak to old Rafiki once more and I will show you something incredible!";
                    case USE_STAR_ALTAR -> "Ahh, perfect! Now craft a Star Altar and use de Rafiki Dust on it. De great kings of de past are waiting!";
                    case COMPLETE -> "It is done! De spirits of de great kings smile upon you! Old Rafiki is very, VERY proud! Ohohoho!";
                    default -> null;
                };
        // COLLECT_BUGS, RETURN_TO_RAFIKI, and LION_DUST_CEREMONY transitions don't happen here
        if (message != null) ChatHelper.sendNpcMessage(player, "Rafiki", message);
    }

    private String getScarHint(Player player) {
        String base = "Excellent! Here is old Rafiki's stick — it is de ONLY weapon dat can harm Scar! "
                + "Watch de hyenas... dey always know where deir master hides. Follow dem, and you will find dat coward!";
        if (level() instanceof ServerLevel serverLevel) {
            List<ScarEntity> scars = serverLevel.getEntitiesOfClass(
                    ScarEntity.class, player.getBoundingBox().inflate(250));
            if (!scars.isEmpty()) {
                String direction = DirectionHelper.getCompassDirection(
                        player.blockPosition(), scars.get(0).blockPosition());
                base += " Oho! I hear he was seen lurking in de caves " + direction + "!";
            }
        }
        return base;
    }

    private void sendSpeech(Player player, CharacterSpeech speech) {
        CharacterSpeech.sendSpeech(player, speech);
    }

    // ── Lion Dust ceremony ───────────────────────────────────────────────────

    private void handleCeremonyInteraction(Player player, NpcInteraction ctx) {
        WorldData worldData = ctx.worldData();
        if (worldData.isCeremonyActive()) {
            ChatHelper.sendNpcMessage(player, "Rafiki", "Patience! De spirits are not to be rushed! Hehe!");
            return;
        }
        ChatHelper.sendNpcMessage(player, "Rafiki", "Ahh, you have brought me everything I need! Now watch closely...");
        worldData.setRafikiCeremonyTick(1);
    }

    private void processCeremonyTick(ServerLevel level, WorldData worldData) {
        int tick = worldData.getRafikiCeremonyTick();

        // Deliver dialogue lines at fixed intervals
        for (int i = 0; i < CEREMONY_DIALOGUE.length; i++) {
            int lineTick = 1 + i * CEREMONY_LINE_INTERVAL;
            if (tick == lineTick) {
                ChatHelper.broadcastNpcMessage(level, "Rafiki", CEREMONY_DIALOGUE[i]);

                // Drop Rafiki Dust on the fourth line
                if (i == CEREMONY_DUST_DROP_LINE) {
                    Player nearest = level.getNearestPlayer(this, 64.0);
                    if (nearest != null) {
                        nearest.addItem(new ItemStack(LionKingItems.RAFIKI_DUST.get(), CEREMONY_DUST_COUNT));
                    }
                }
                break;
            }
        }

        // Check if ceremony is complete (one tick after last line)
        int completionTick = 1 + (CEREMONY_DIALOGUE.length - 1) * CEREMONY_LINE_INTERVAL + 1;
        if (tick >= completionTick) {
            completeCeremony(level, worldData);
        } else {
            worldData.setRafikiCeremonyTick(tick + 1);
        }
    }

    private void completeCeremony(ServerLevel level, WorldData worldData) {
        worldData.setRafikiCeremonyTick(0);

        QuestlineManager quests = worldData.getQuestManager();
        QuestlineState state = quests.getState("rafiki");
        state.setCurrentStageId(Stage.USE_STAR_ALTAR.name());
        state.setChecked(false);
        worldData.setDirty();

        // Fire advancement trigger for nearest player
        Player nearest = level.getNearestPlayer(this, 64.0);
        if (nearest instanceof ServerPlayer serverPlayer) {
            LionKingCriteriaTriggers.QUEST_COMPLETE.trigger(serverPlayer);
        }

        quests.syncToAllPlayers(level.getServer());
    }

    // ── Side interactions (NPC trades) ───────────────────────────────────────

    private boolean trySideInteraction(Player player, NpcInteraction ctx) {
        ItemStack held = player.getMainHandItem();
        QuestlineManager quests = ctx.quests();

        // Rafiki Coin: 3 silver ingots → 1 Rafiki Coin (available after FIND_RAFIKI)
        if (quests.isStageAtOrPast("rafiki", Stage.CRAFT_RAFIKI_STICK)
                && held.is(LionKingItems.SILVER_INGOT.get())
                && held.getCount() >= RAFIKI_COIN_COST) {
            held.shrink(RAFIKI_COIN_COST);
            player.addItem(new ItemStack(LionKingItems.RAFIKI_COIN.get()));
            ChatHelper.sendNpcMessage(
                    player,
                    "Rafiki",
                    "Ahh, silver! Here, take dis coin. Throw it on de ground and it will bring you back to old Rafiki! Ohohoho!");
            return true;
        }

        // Quest Book replacement: book + lion fur → Quest Book (available after FIND_RAFIKI)
        if (quests.isStageAtOrPast("rafiki", Stage.CRAFT_RAFIKI_STICK)
                && held.is(Items.BOOK)
                && hasInventoryItem(player, LionKingItems.LION_FUR.get())) {
            held.shrink(1);
            consumeInventoryItem(player, LionKingItems.LION_FUR.get(), 1);
            player.addItem(new ItemStack(LionKingItems.QUEST_BOOK.get()));
            ChatHelper.sendNpcMessage(
                    player, "Rafiki", "You lost your Quest Book? Hehe! Silly creature! Here, take another one!");
            return true;
        }

        // Extra Rafiki Stick: 64 hyena bones → Rafiki Stick (available after COLLECT_BONES)
        if (quests.isStageAtOrPast("rafiki", Stage.DEFEAT_SCAR)
                && held.is(LionKingItems.HYENA_BONE.get())
                && held.getCount() >= EXTRA_STICK_BONE_COST) {
            held.shrink(EXTRA_STICK_BONE_COST);
            player.addItem(new ItemStack(LionKingItems.RAFIKI_STICK.get()));
            ChatHelper.sendNpcMessage(
                    player,
                    "Rafiki",
                    "More bones! Old Rafiki can always use more bones! Here is another stick for you!");
            return true;
        }

        // Repeatable Lion Dust: termite dust + mango dust → Rafiki Dust
        // (available after LION_DUST_CEREMONY)
        if (quests.isStageAtOrPast("rafiki", Stage.USE_STAR_ALTAR)) {
            if (held.is(LionKingItems.TERMITE_DUST.get()) && hasInventoryItem(player, LionKingItems.MANGO_DUST.get())) {
                held.shrink(1);
                consumeInventoryItem(player, LionKingItems.MANGO_DUST.get(), 1);
                player.addItem(new ItemStack(LionKingItems.RAFIKI_DUST.get()));
                ChatHelper.sendNpcMessage(
                        player, "Rafiki", "More ingredients! Dis old baboon never tires of making dust! Hehe!");
                return true;
            }
            if (held.is(LionKingItems.MANGO_DUST.get()) && hasInventoryItem(player, LionKingItems.TERMITE_DUST.get())) {
                held.shrink(1);
                consumeInventoryItem(player, LionKingItems.TERMITE_DUST.get(), 1);
                player.addItem(new ItemStack(LionKingItems.RAFIKI_DUST.get()));
                ChatHelper.sendNpcMessage(
                        player, "Rafiki", "More ingredients! Dis old baboon never tires of making dust! Hehe!");
                return true;
            }
        }

        return false;
    }

    // ── Inventory helpers ────────────────────────────────────────────────────

    private static boolean hasInventoryItem(Player player, Item item) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) return true;
        }
        return false;
    }

    private static void consumeInventoryItem(Player player, Item item, int count) {
        int remaining = count;
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
