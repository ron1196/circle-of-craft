package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.quest.LKAnimalQuest;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class LKAnimal extends Animal {

    private static final Random QUEST_RANDOM = new Random();
    private final LKAnimalQuest animalQuest = new LKAnimalQuest();

    // Possible quest reward items
    private static final Item[] QUEST_FOOD_ITEMS = null; // Initialized lazily

    protected LKAnimal(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createLKAnimalAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        return null;
    }

    public LKAnimalQuest getAnimalQuest() {
        return animalQuest;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;

        ItemStack held = player.getItemInHand(hand);

        if (animalQuest.hasQuest()) {
            // Check if player has the required item
            if (animalQuest.isRequiredItem(held)) {
                held.shrink(animalQuest.getRequiredAmount());
                // Give reward
                ItemStack reward = getQuestReward();
                player.addItem(reward);
                player.sendSystemMessage(Component.literal(
                        animalQuest.getQuestEndMessage(getAnimalDisplayName())));
                animalQuest.completeQuest();
                return InteractionResult.SUCCESS;
            } else {
                // Remind player of the quest
                player.sendSystemMessage(Component.literal(
                        animalQuest.getQuestStartMessage(getAnimalDisplayName(),
                                animalQuest.getRequiredItem().getDescription().getString())));
                return InteractionResult.SUCCESS;
            }
        }

        // Assign a new quest with some probability
        if (QUEST_RANDOM.nextInt(3) == 0) {
            assignRandomQuest();
            if (animalQuest.hasQuest()) {
                player.sendSystemMessage(Component.literal(
                        animalQuest.getQuestStartMessage(getAnimalDisplayName(),
                                animalQuest.getRequiredItem().getDescription().getString())));
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    private void assignRandomQuest() {
        Item[] requestItems = getQuestRequestItems();
        if (requestItems == null || requestItems.length == 0) return;

        Item item = requestItems[QUEST_RANDOM.nextInt(requestItems.length)];
        int amount = 1 + QUEST_RANDOM.nextInt(5);
        animalQuest.setQuest(item, amount);
    }

    protected Item[] getQuestRequestItems() {
        return new Item[]{
                LKItems.MANGO.get(),
                LKItems.BANANA.get(),
                LKItems.CORN.get(),
                LKItems.KIWANO.get(),
                net.minecraft.world.item.Items.APPLE,
                net.minecraft.world.item.Items.BREAD,
                net.minecraft.world.item.Items.WHEAT
        };
    }

    protected ItemStack getQuestReward() {
        // Random reward from mod items
        Item[] rewards = {
                LKItems.SILVER_INGOT.get(),
                LKItems.PEACOCK_GEM.get(),
                LKItems.RAFIKI_COIN.get(),
                LKItems.CRYSTAL.get(),
                LKItems.LION_FUR.get()
        };
        return new ItemStack(rewards[QUEST_RANDOM.nextInt(rewards.length)], 1 + QUEST_RANDOM.nextInt(3));
    }

    protected String getAnimalDisplayName() {
        return getType().getDescription().getString();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CompoundTag questTag = new CompoundTag();
        animalQuest.save(questTag);
        tag.put("AnimalQuest", questTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("AnimalQuest")) {
            animalQuest.load(tag.getCompound("AnimalQuest"));
        }
    }
}
