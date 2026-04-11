package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.data.WorldData;
import io.github.ron1196.thelionking.menu.TimonMerchantMenu;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.quest.NpcInteraction;
import io.github.ron1196.thelionking.quest.questline.RafikiQuestline;
import io.github.ron1196.thelionking.quest.stage.QuestTrigger;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class TimonEntity extends PathfinderMob {

    private static final int DEFAULT_COOLDOWN_TICKS = 120;

    private final NpcBehavior questBehavior = new NpcBehavior(this, 30, null);
    private boolean hasGivenFirstBugs = false;

    public TimonEntity(EntityType<? extends TimonEntity> type, Level level) {
        super(type, level);
        this.setCustomName(Component.literal("Timon"));
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
        return false; // Invulnerable NPC
    }

    @Override
    public void tick() {
        super.tick();
        questBehavior.tick();
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

    private static final int RALLY_COOLDOWN_TICKS = 40;

    private static final String[][] RALLY_INTRO_DIALOGUE = {
        {
            "Timon",
            "Whoa whoa WHOA! You want US to fight Scar?! That guy's got claws the size of my whole body! Are you NUTS?!"
        },
        {
            "Pumbaa",
            "Timon, we HAVE to help! Remember what Rafiki said? The Pride Lands need us! It's the right thing to do!"
        },
        {
            "Timon",
            "Ugh, fine, FINE! But Pumbaa here's gonna need some fuel first. Bring him four bugs and he'll be ready to rumble. Hakuna Matata, am I right?"
        }
    };

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;

        // Sneak+interact always opens the shop — no cooldown, no prerequisite
        if (player.isShiftKeyDown()) {
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, TimonMerchantMenu.PROVIDER);
            }
            return InteractionResult.SUCCESS;
        }

        if (questBehavior.isOnCooldown()) return InteractionResult.SUCCESS;

        // RALLY_PUMBAA — scripted intro dialogue
        NpcInteraction ctx = NpcInteraction.tryCreate(player);
        if (ctx != null) {
            RafikiQuestline.Stage rafikiStage = ctx.stage("rafiki", RafikiQuestline.Stage.class);
            if (rafikiStage == RafikiQuestline.Stage.RALLY_PUMBAA) {
                questBehavior.startCooldown(RALLY_COOLDOWN_TICKS);
                handleRallyPumbaaIntro(player, ctx);
                return InteractionResult.SUCCESS;
            }
            if (rafikiStage == RafikiQuestline.Stage.COLLECT_BUGS) {
                questBehavior.startCooldown(RALLY_COOLDOWN_TICKS);
                CharacterSpeech.sendSpeech(player, CharacterSpeech.TIMON_WAITING_BUGS);
                return InteractionResult.SUCCESS;
            }
        }

        questBehavior.startCooldown(DEFAULT_COOLDOWN_TICKS);

        // Accept bugs for a quick trade
        ItemStack held = player.getItemInHand(hand);
        if (held.is(LionKingItems.BUG.get()) && held.getCount() >= 5) {
            held.shrink(5);
            int reward = random.nextInt(4);
            switch (reward) {
                case 0 -> player.addItem(new ItemStack(LionKingItems.PUMBAA_BOMB.get(), 3));
                case 1 -> player.addItem(new ItemStack(LionKingItems.CRYSTAL.get(), 1));
                case 2 -> player.addItem(new ItemStack(LionKingItems.AMULET.get(), 1));
                case 3 -> player.giveExperiencePoints(50);
            }
            hasGivenFirstBugs = true;
            ChatHelper.sendNpcMessage(
                    player, "Timon", "Slimy yet satisfying, am I right? Here's a little somethin' for ya, kid!");
            return InteractionResult.SUCCESS;
        }

        CharacterSpeech.sendSpeech(player, hasGivenFirstBugs ? CharacterSpeech.MORE_BUGS : CharacterSpeech.BUGS);
        return InteractionResult.SUCCESS;
    }

    private void handleRallyPumbaaIntro(@NotNull Player player, @NotNull NpcInteraction ctx) {
        WorldData worldData = ctx.worldData();
        int talkIndex = worldData.getTimonRafikiTalkCount();

        if (talkIndex < RALLY_INTRO_DIALOGUE.length) {
            ChatHelper.sendNpcMessage(player, RALLY_INTRO_DIALOGUE[talkIndex][0], RALLY_INTRO_DIALOGUE[talkIndex][1]);
            worldData.incrementTimonRafikiTalkCount();
        }

        // After the last line, advance RALLY_PUMBAA → COLLECT_BUGS
        if (talkIndex >= RALLY_INTRO_DIALOGUE.length - 1) {
            ctx.quests().tryAdvance("rafiki", ctx.serverPlayer(), QuestTrigger.TIMON_TALK);
        }
    }
}
