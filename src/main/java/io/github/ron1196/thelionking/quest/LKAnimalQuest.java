package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public class LKAnimalQuest {

    private static final Random RANDOM = new Random();

    private static final String[] QUEST_START_PHRASES = {
            "I'm getting quite hungry. If you can bring me # %, there might be a reward for you!",
            "Bring me # % to eat and I'll give you something useful!",
            "I could really do with # %. Can you help me out?",
            "I've been looking for # %. Bring some and I'll make it worth your while!",
            "If you bring me # %, I'll have a reward ready for you.",
            "I fancy # % right about now. Could you fetch some?"
    };

    private static final String[] QUEST_END_PHRASES = {
            "Delicious! Here, take this as a reward.",
            "Thank you so much! Here's something for your trouble.",
            "That really hit the spot! Take this reward.",
            "Perfect! You've earned this."
    };

    private static final String[] NUMBER_WORDS = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight",
            "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen", "twenty", "twenty-one", "twenty-two",
            "twenty-three", "twenty-four", "twenty-five", "twenty-six", "twenty-seven",
            "twenty-eight", "twenty-nine", "thirty", "thirty-one", "thirty-two",
            "thirty-three", "thirty-four", "thirty-five", "thirty-six", "thirty-seven",
            "thirty-eight", "thirty-nine", "forty", "forty-one", "forty-two", "forty-three",
            "forty-four", "forty-five", "forty-six", "forty-seven", "forty-eight",
            "forty-nine", "fifty", "fifty-one", "fifty-two", "fifty-three", "fifty-four",
            "fifty-five", "fifty-six", "fifty-seven", "fifty-eight", "fifty-nine",
            "sixty", "sixty-one", "sixty-two", "sixty-three", "sixty-four"
    };

    private Item requiredItem;
    private int requiredAmount;
    private boolean hasQuest;

    public LKAnimalQuest() {
    }

    public void setQuest(Item item, int amount) {
        this.requiredItem = item;
        this.requiredAmount = amount;
        this.hasQuest = true;
    }

    public boolean hasQuest() {
        return hasQuest;
    }

    public boolean isRequiredItem(ItemStack stack) {
        return hasQuest && stack.is(requiredItem) && stack.getCount() >= requiredAmount;
    }

    public Item getRequiredItem() {
        return requiredItem;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public String getQuestStartMessage(String animalName, String itemName) {
        String phrase = QUEST_START_PHRASES[RANDOM.nextInt(QUEST_START_PHRASES.length)];
        String numberWord = requiredAmount >= 0 && requiredAmount < NUMBER_WORDS.length
                ? NUMBER_WORDS[requiredAmount] : String.valueOf(requiredAmount);
        phrase = phrase.replace("#", numberWord).replace("%", itemName);
        return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
    }

    public String getQuestEndMessage(String animalName) {
        String phrase = QUEST_END_PHRASES[RANDOM.nextInt(QUEST_END_PHRASES.length)];
        return "\u00a7e<" + animalName + "> \u00a7f" + phrase;
    }

    public void completeQuest() {
        this.hasQuest = false;
        this.requiredItem = null;
        this.requiredAmount = 0;
    }

    /**
     * Gives a reward to the player based on the type of animal that gave the quest.
     * Called when the player completes an animal's mini-quest by bringing the requested item.
     *
     * @param player    the player receiving the reward
     * @param animalType the registry name of the animal (e.g., "lion", "zebra", "giraffe")
     */
    public static void giveReward(ServerPlayer player, String animalType) {
        ItemStack reward = switch (animalType) {
            case "lion" -> new ItemStack(Items.GOLD_INGOT, 2 + RANDOM.nextInt(3));
            case "zebra" -> new ItemStack(Items.LEATHER, 3 + RANDOM.nextInt(3));
            case "giraffe" -> new ItemStack(LKItems.GIRAFFE_SADDLE.get(), 1);
            case "elephant" -> new ItemStack(Items.IRON_INGOT, 2 + RANDOM.nextInt(4));
            case "rhino" -> new ItemStack(Items.IRON_INGOT, 3 + RANDOM.nextInt(3));
            case "hippo" -> new ItemStack(Items.COD, 3 + RANDOM.nextInt(5));
            case "crocodile" -> new ItemStack(Items.GOLD_NUGGET, 5 + RANDOM.nextInt(6));
            case "warthog" -> new ItemStack(Items.CARROT, 4 + RANDOM.nextInt(5));
            case "meerkat" -> new ItemStack(LKItems.RAFIKI_COIN.get(), 1 + RANDOM.nextInt(2));
            case "ostrich" -> new ItemStack(Items.FEATHER, 4 + RANDOM.nextInt(5));
            case "flamingo" -> new ItemStack(Items.PINK_DYE, 3 + RANDOM.nextInt(4));
            case "gorilla" -> new ItemStack(Items.DIAMOND, 1);
            case "leopard" -> new ItemStack(LKItems.AMULET.get(), 1);
            case "peacock" -> new ItemStack(LKItems.PEACOCK_GEM.get(), 1 + RANDOM.nextInt(2));
            case "buffalo" -> new ItemStack(Items.LEATHER, 4 + RANDOM.nextInt(4));
            default -> new ItemStack(Items.GOLD_NUGGET, 3 + RANDOM.nextInt(4));
        };

        player.getInventory().placeItemBackInInventory(reward);
        player.displayClientMessage(
                Component.literal("\u00a7aYou received " + reward.getCount() + "x " + reward.getHoverName().getString() + " as a reward!"),
                false
        );
    }

    public void save(CompoundTag tag) {
        tag.putBoolean("HasQuest", hasQuest);
        if (hasQuest && requiredItem != null) {
            tag.putString("QuestItem", net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(requiredItem).toString());
            tag.putInt("QuestAmount", requiredAmount);
        }
    }

    public void load(CompoundTag tag) {
        hasQuest = tag.getBoolean("HasQuest");
        if (hasQuest && tag.contains("QuestItem")) {
            net.minecraft.resources.ResourceLocation itemId = new net.minecraft.resources.ResourceLocation(tag.getString("QuestItem"));
            requiredItem = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(itemId);
            requiredAmount = tag.getInt("QuestAmount");
            if (requiredItem == null) {
                hasQuest = false;
            }
        }
    }
}
