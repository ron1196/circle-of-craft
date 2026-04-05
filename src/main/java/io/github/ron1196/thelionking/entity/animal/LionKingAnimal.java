package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.entity.ai.CrossTypeBreedGoal;
import io.github.ron1196.thelionking.entity.animal.favor.AnimalFavor;
import io.github.ron1196.thelionking.entity.animal.favor.AnimalFavorEntry;
import io.github.ron1196.thelionking.quest.CharacterSpeech;
import io.github.ron1196.thelionking.registry.LionKingItems;
import io.github.ron1196.thelionking.util.ChatHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public abstract class LionKingAnimal extends Animal {

    protected static final Random QUEST_RANDOM = new Random();
    private final Map<UUID, AnimalFavorEntry> animalQuests = new HashMap<>();

    protected LionKingAnimal(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new CrossTypeBreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    /**
     * Subclasses should call this in registerGoals() after super to add a tempt goal
     * for their specific breeding food items.
     */
    protected void addTemptGoal(int priority, double speed, Item... foodItems) {
        this.goalSelector.addGoal(priority, new TemptGoal(this, speed, Ingredient.of(foodItems), false));
    }

    public static AttributeSupplier.Builder createLKAnimalAttributes() {
        return Animal.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        // Amulet quest interactions take priority over breeding/taming
        if (isWearingAmulet(player)) {
            if (level().isClientSide()) return InteractionResult.SUCCESS;
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

            UUID playerId = player.getUUID();
            AnimalFavorEntry entry = animalQuests.get(playerId);

            // Active quest — check if player has the requested item
            if (entry != null) {
                if (held.is(entry.requiredItem()) && held.getCount() >= entry.requiredAmount()) {
                    held.shrink(entry.requiredAmount());
                    giveQuestReward(serverPlayer);
                    String questEndMessage = AnimalFavor.getQuestEndMessage(getAnimalDisplayName());
                    player.sendSystemMessage(Component.literal(questEndMessage));
                    animalQuests.remove(playerId);
                } else {
                    String questStartMessage = AnimalFavor.getQuestStartMessage(
                            getAnimalDisplayName(),
                            entry.requiredItem().getDescription().getString(),
                            entry.requiredAmount());
                    player.sendSystemMessage(Component.literal(questStartMessage));
                }
                return InteractionResult.SUCCESS;
            }

            // No active quest — chance to start one, otherwise speech
            if (QUEST_RANDOM.nextInt(3) == 0) {
                Item[] requestItems = getQuestRequestItems();
                if (requestItems != null && requestItems.length > 0) {
                    Item item = requestItems[QUEST_RANDOM.nextInt(requestItems.length)];
                    int amount = 1 + QUEST_RANDOM.nextInt(5);

                    String questStartMessage = AnimalFavor.getQuestStartMessage(
                            getAnimalDisplayName(), item.getDescription().getString(), amount);
                    player.sendSystemMessage(Component.literal(questStartMessage));

                    animalQuests.put(playerId, new AnimalFavorEntry(item, amount));
                    return InteractionResult.SUCCESS;
                }
            }

            sendAnimalSpeech(serverPlayer);
            return InteractionResult.SUCCESS;
        }

        // No amulet — let vanilla handle breeding/taming
        return super.mobInteract(player, hand);
    }

    protected ItemStack getQuestReward() {
        return new ItemStack(net.minecraft.world.item.Items.GOLD_NUGGET, 3 + QUEST_RANDOM.nextInt(4));
    }

    private void giveQuestReward(ServerPlayer player) {
        ItemStack reward = getQuestReward();
        int count = reward.getCount();
        String name = reward.getHoverName().getString();
        player.getInventory().placeItemBackInInventory(reward);
        String rewardMsg = String.format("§aYou received %dx %s as a reward!", count, name);
        player.displayClientMessage(Component.literal(rewardMsg), false);
        LionKingCriteriaTriggers.FEED_ANIMAL.trigger(player);
    }

    private static boolean isWearingAmulet(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() == LionKingItems.AMULET.get();
    }

    /**
     * Override to provide per-animal CharacterSpeech for amulet interaction.
     * Returns null if no speech is defined (falls back to generic).
     */
    protected @Nullable CharacterSpeech getCharacterSpeech() {
        return null;
    }

    private void sendAnimalSpeech(ServerPlayer player) {
        CharacterSpeech speech = isBaby() ? null : getCharacterSpeech();
        if (speech != null) {
            CharacterSpeech.sendSpeech(player, speech);
        } else {
            ChatHelper.sendNpcMessage(player, getAnimalDisplayName(), "...");
        }
    }

    protected Item[] getQuestRequestItems() {
        return new Item[] {
            LionKingItems.MANGO.get(),
            LionKingItems.BANANA.get(),
            LionKingItems.CORN.get(),
            LionKingItems.KIWANO.get(),
            net.minecraft.world.item.Items.APPLE,
            net.minecraft.world.item.Items.BREAD,
            net.minecraft.world.item.Items.WHEAT
        };
    }

    protected String getAnimalDisplayName() {
        return getType().getDescription().getString();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CompoundTag questsTag = new CompoundTag();
        for (Map.Entry<UUID, AnimalFavorEntry> e : animalQuests.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(e.getValue().requiredItem());
            if (itemKey != null) {
                entryTag.putString("Item", itemKey.toString());
                entryTag.putInt("Amount", e.getValue().requiredAmount());
                questsTag.put(e.getKey().toString(), entryTag);
            }
        }
        tag.put("AnimalQuests", questsTag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        animalQuests.clear();
        if (tag.contains("AnimalQuests")) {
            CompoundTag questsTag = tag.getCompound("AnimalQuests");
            for (String key : questsTag.getAllKeys()) {
                CompoundTag entryTag = questsTag.getCompound(key);
                ResourceLocation itemId = new ResourceLocation(entryTag.getString("Item"));
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                if (item == null) continue;
                AnimalFavorEntry questEntry = new AnimalFavorEntry(item, entryTag.getInt("Amount"));
                animalQuests.put(UUID.fromString(key), questEntry);
            }
        }
    }
}
