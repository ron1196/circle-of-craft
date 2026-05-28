package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.block.FilledVaseBlock;
import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.data.WorldData;
import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.quest.NpcInteraction;
import io.github.ron1196.circleofcraft.quest.actions.OutlandsQuestActions;
import io.github.ron1196.circleofcraft.quest.actions.RafikiQuestActions;
import io.github.ron1196.circleofcraft.quest.questline.OutlandsQuestline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineState;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.circleofcraft.quest.stage.QuestTrigger;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import io.github.ron1196.circleofcraft.util.DirectionHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RafikiEntity extends PathfinderMob {

    public static final String REGISTRY_NAME = "rafiki";

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

    // ── Vase heart-particle constants ─────────────────────────────────────────
    // Old mod scanned a 33×11×33 box (-16..16 X/Z, -5..5 Y) every tick and rolled 1-in-150.
    // We scan every 20 ticks and use a 1-in-8 dice roll → ~1 heart per 160 ticks (~8s), close to the old 7.5s cadence.
    private static final int HEART_VASE_SCAN_INTERVAL = 20;
    private static final int HEART_VASE_RADIUS_HORIZONTAL = 16;
    private static final int HEART_VASE_RADIUS_VERTICAL = 5;
    private static final int HEART_VASE_CHANCE_DENOMINATOR = 8;
    private static final int HEART_BASE_COUNT = 5;
    private static final int HEART_PER_EXTRA_VASE = 2;
    // Caps the vase count we bother tallying — more vases past this don't grow the heart burst further.
    private static final int HEART_VASE_TALLY_CAP = 8;

    private final NpcBehavior questBehavior = new NpcBehavior(this, 16, this::onQuestCheck);

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
            tickVaseHearts(serverLevel);
        }
    }

    /**
     * Periodically emits heart particles when at least one filled vase block sits within a
     * 33×11×33 box around Rafiki — ambient flavor ported from the old mod. Heart count and chime
     * loudness scale with the number of nearby vases, capped at {@link #HEART_VASE_TALLY_CAP}.
     */
    private void tickVaseHearts(@NotNull ServerLevel serverLevel) {
        if (serverLevel.getGameTime() % HEART_VASE_SCAN_INTERVAL != 0) return;
        if (random.nextInt(HEART_VASE_CHANCE_DENOMINATOR) != 0) return;
        int vaseCount = countFilledVasesNearby(serverLevel, HEART_VASE_TALLY_CAP);
        if (vaseCount == 0) return;
        int heartCount = HEART_BASE_COUNT + (vaseCount - 1) * HEART_PER_EXTRA_VASE;
        for (int i = 0; i < heartCount; i++) {
            double dx = random.nextGaussian() * 0.02D;
            double dy = random.nextGaussian() * 0.02D;
            double dz = random.nextGaussian() * 0.02D;
            serverLevel.sendParticles(
                    ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 1, dx, dy, dz, 0.0D);
        }
        float volume = 0.5F + (vaseCount - 1) * 0.05F;
        serverLevel.playSound(
                null,
                blockPosition(),
                SoundEvents.NOTE_BLOCK_CHIME.value(),
                SoundSource.NEUTRAL,
                volume,
                1.0F + (random.nextFloat() - 0.5F) * 0.2F);
    }

    private boolean hasFilledVaseNearby(@NotNull ServerLevel serverLevel) {
        return countFilledVasesNearby(serverLevel, 1) > 0;
    }

    /** Counts filled vases inside the heart-scan box, early-exiting once {@code cap} are found. */
    private int countFilledVasesNearby(@NotNull ServerLevel serverLevel, int cap) {
        BlockPos center = blockPosition();
        Iterable<BlockPos> box = BlockPos.betweenClosed(
                center.offset(
                        -HEART_VASE_RADIUS_HORIZONTAL, -HEART_VASE_RADIUS_VERTICAL, -HEART_VASE_RADIUS_HORIZONTAL),
                center.offset(HEART_VASE_RADIUS_HORIZONTAL, HEART_VASE_RADIUS_VERTICAL, HEART_VASE_RADIUS_HORIZONTAL));
        int count = 0;
        for (BlockPos pos : box) {
            if (serverLevel.getBlockState(pos).getBlock() instanceof FilledVaseBlock) {
                if (++count >= cap) return count;
            }
        }
        return count;
    }

    private boolean onQuestCheck(@NotNull ServerLevel serverLevel, @NotNull QuestlineManager quests) {
        // Validate Rafiki quest world state (Scar spawning, portal, etc.)
        Stage rafikiStage = quests.getStage(RafikiQuestline.QUEST_ID, Stage.class);
        RafikiQuestActions.ensureWorldState(serverLevel, rafikiStage);

        // Validate Outlands quest world state (tree occupation, etc.)
        OutlandsQuestline.Stage outlandsStage =
                quests.getStage(OutlandsQuestline.QUEST_ID, OutlandsQuestline.Stage.class);
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
            player.addItem(new ItemStack(ModItems.QUEST_BOOK.get()));
            ctx.syncPlayerData();
        }

        // Handle Outlands quest — RAFIKI_RETURNS stage
        OutlandsQuestline.Stage outlandsStage = ctx.stage(OutlandsQuestline.QUEST_ID, OutlandsQuestline.Stage.class);
        if (outlandsStage == OutlandsQuestline.Stage.RAFIKI_RETURNS) {
            if (ctx.quests().tryAdvance(OutlandsQuestline.QUEST_ID, ctx.serverPlayer(), QuestTrigger.RAFIKI_TALK)) {
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
        Stage stage = ctx.stage(RafikiQuestline.QUEST_ID, Stage.class);
        if (stage == Stage.LION_DUST_CEREMONY) {
            handleCeremonyInteraction(player, ctx);
            return InteractionResult.SUCCESS;
        }

        // Standard path: try claim reward, then try advance
        // (Must run before DEFEAT_SCAR pre-empt so unclaimed rewards are still claimed)
        if (ctx.tryClaimOrAdvance(
                RafikiQuestline.QUEST_ID,
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

        // Occasional flower chit-chat overrides the stage-specific speech
        // (rare during questing, common post-completion).
        if (tryFlowerChitChat(player, stage)) {
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

    /**
     * Occasionally veers Rafiki off-topic into flower chit-chat — praises nearby vases or wishes
     * for some when none are around. Rare during questing (1-in-5), frequent post-completion (1-in-2).
     * Returns true if a speech was sent.
     */
    private boolean tryFlowerChitChat(Player player, Stage stage) {
        int chanceDenominator = stage == Stage.COMPLETE ? 2 : 5;
        if (random.nextInt(chanceDenominator) != 0) return false;
        if (!(level() instanceof ServerLevel serverLevel)) return false;
        CharacterSpeech speech =
                hasFilledVaseNearby(serverLevel) ? CharacterSpeech.FLOWERS : CharacterSpeech.ASK_FOR_FLOWERS;
        sendSpeech(player, speech);
        return true;
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
                        nearest.addItem(new ItemStack(ModItems.RAFIKI_DUST.get(), CEREMONY_DUST_COUNT));
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
        QuestlineState state = quests.getState(RafikiQuestline.QUEST_ID);
        state.setCurrentStageId(Stage.USE_STAR_ALTAR.name());
        state.setChecked(false);
        worldData.setDirty();

        // Fire advancement trigger for nearest player
        Player nearest = level.getNearestPlayer(this, 64.0);
        if (nearest instanceof ServerPlayer serverPlayer) {
            ModCriteriaTriggers.QUEST_COMPLETE.trigger(serverPlayer);
        }

        quests.syncToAllPlayers(level.getServer());
    }

    // ── Side interactions (NPC trades) ───────────────────────────────────────

    private boolean trySideInteraction(Player player, NpcInteraction ctx) {
        RafikiTrades.Trade trade = RafikiTrades.tryExecute(player, ctx.quests());
        if (trade == null) return false;
        sendSpeech(player, trade.completionSpeech());
        return true;
    }
}
