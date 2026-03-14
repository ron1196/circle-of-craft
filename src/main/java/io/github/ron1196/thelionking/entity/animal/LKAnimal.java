package io.github.ron1196.thelionking.entity.animal;

import io.github.ron1196.thelionking.quest.AnimalQuestEntry;
import io.github.ron1196.thelionking.quest.LKAnimalQuest;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public abstract class LKAnimal extends Animal {

    private static final Random QUEST_RANDOM = new Random();
    private final Map<UUID, AnimalQuestEntry> animalQuests = new HashMap<>();

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
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

        UUID playerId = player.getUUID();
        AnimalQuestEntry entry = animalQuests.get(playerId);
        ItemStack held = player.getItemInHand(hand);

        if (entry != null) {
            if (held.is(entry.requiredItem()) && held.getCount() >= entry.requiredAmount()) {
                held.shrink(entry.requiredAmount());
                LKAnimalQuest.giveReward(serverPlayer, getAnimalTypeKey());
                player.sendSystemMessage(Component.literal(
                        LKAnimalQuest.getQuestEndMessage(getAnimalDisplayName())));
                animalQuests.remove(playerId);
            } else {
                player.sendSystemMessage(Component.literal(
                        LKAnimalQuest.getQuestStartMessage(
                                getAnimalDisplayName(),
                                entry.requiredItem().getDescription().getString(),
                                entry.requiredAmount())));
            }
            return InteractionResult.SUCCESS;
        }

        if (QUEST_RANDOM.nextInt(3) == 0) {
            Item[] requestItems = getQuestRequestItems();
            if (requestItems != null && requestItems.length > 0) {
                Item item = requestItems[QUEST_RANDOM.nextInt(requestItems.length)];
                int amount = 1 + QUEST_RANDOM.nextInt(5);
                animalQuests.put(playerId, new AnimalQuestEntry(item, amount));
                player.sendSystemMessage(Component.literal(
                        LKAnimalQuest.getQuestStartMessage(
                                getAnimalDisplayName(),
                                item.getDescription().getString(),
                                amount)));
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    protected String getAnimalTypeKey() {
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(getType());
        return key != null ? key.getPath() : "unknown";
    }

    protected Item[] getQuestRequestItems() {
        return new Item[]{
                LKItems.MANGO.get(),
                LKItems.BANANA.get(),
                LKItems.CORN.get(),
                LKItems.KIWANO.get(),
                Items.APPLE,
                Items.BREAD,
                Items.WHEAT
        };
    }

    protected String getAnimalDisplayName() {
        return getType().getDescription().getString();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        CompoundTag questsTag = new CompoundTag();
        for (Map.Entry<UUID, AnimalQuestEntry> e : animalQuests.entrySet()) {
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
                if (item != null) {
                    animalQuests.put(UUID.fromString(key), new AnimalQuestEntry(item, entryTag.getInt("Amount")));
                }
            }
        }
    }
}
