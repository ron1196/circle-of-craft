package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKItems;
import io.github.ron1196.thelionking.world.dimension.LKDimensions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LKQuestOutlands extends LKQuestBase {

    // Stage constants
    public static final int NOT_STARTED = 0;
    public static final int ENTER_OUTLANDS = 1;
    public static final int FIND_ZIRA = 2;
    public static final int COLLECT_CORRUPT_STONE = 3;
    public static final int KILL_OUTLANDERS = 4;
    public static final int FIND_ALTAR = 5;
    public static final int PLACE_OFFERING = 6;
    public static final int DEFEAT_TERMITE_QUEEN = 7;
    public static final int DEFEAT_ZIRA = 8;
    public static final int RETURN_HOME = 9;
    public static final int COMPLETE = 10;

    public LKQuestOutlands(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return LKQuestBase.RAFIKI_QUEST.isComplete();
    }

    @Override
    public String[] getRequirements() {
        return new String[]{"Complete Rafiki's Quest"};
    }

    @Override
    public int getNumStages() {
        return 10;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.WAYWARD_FEATHER.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "Enter the Outlands dimension";
            case ENTER_OUTLANDS -> "Find Zira in the Outlands";
            case FIND_ZIRA -> "Collect 20 corrupt pridestone blocks";
            case COLLECT_CORRUPT_STONE -> "Defeat 15 outlanders";
            case KILL_OUTLANDERS -> "Find the Outlands Altar";
            case FIND_ALTAR -> "Place corrupt pridestone on the altar";
            case PLACE_OFFERING -> "Defeat the Termite Queen";
            case DEFEAT_TERMITE_QUEEN -> "Defeat Zira";
            case DEFEAT_ZIRA -> "Return to the Pride Lands";
            case RETURN_HOME -> "Receive your reward";
            case COMPLETE -> "Quest complete!";
            default -> "";
        };
    }

    /**
     * Returns a human-readable description of the current quest objective.
     */
    public String getQuestDescription(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "The Outlands beckon. Enter the Outlands dimension to begin this quest.";
            case ENTER_OUTLANDS -> "You have entered the Outlands. Seek out Zira, the exiled lioness.";
            case FIND_ZIRA -> "Zira demands tribute. Collect 20 corrupt pridestone blocks from the Outlands.";
            case COLLECT_CORRUPT_STONE -> "Prove your strength by defeating 15 outlanders roaming the Outlands.";
            case KILL_OUTLANDERS -> "The outlanders are subdued. Locate the ancient Outlands Altar deep in the wastes.";
            case FIND_ALTAR -> "Place your corrupt pridestone offering on the Outlands Altar.";
            case PLACE_OFFERING -> "The altar awakens the Termite Queen! Defeat her to proceed.";
            case DEFEAT_TERMITE_QUEEN -> "The Termite Queen is slain. Now confront Zira and put an end to her schemes.";
            case DEFEAT_ZIRA -> "Zira has been defeated! Return to the Pride Lands through the portal.";
            case RETURN_HOME -> "You have returned home. Speak to Rafiki for your reward.";
            case COMPLETE -> "You have completed the Outlands quest and ended Zira's reign of terror!";
            default -> "";
        };
    }

    /**
     * Checks conditions for the current stage and advances if met.
     * Returns true if the stage was advanced.
     */
    public boolean tryAdvanceStage(ServerPlayer player, LKLevelData data) {
        int stage = getQuestStage();

        switch (stage) {
            case NOT_STARTED:
                // Check if player is in the Outlands dimension
                if (player.level().dimension() == LKDimensions.OUTLANDS_LEVEL) {
                    progress(ENTER_OUTLANDS);
                    player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fYou have entered the Outlands. Find Zira!"), false);
                    data.setDirty();
                    return true;
                }
                break;

            case ENTER_OUTLANDS:
                // Triggered by interacting with Zira NPC
                progress(FIND_ZIRA);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fZira demands 20 corrupt pridestone. Start mining!"), false);
                data.setDirty();
                return true;

            case FIND_ZIRA:
                // Check if player has 20 corrupt pridestone
                if (countItem(player, LKBlocks.CORRUPT_PRIDESTONE.get().asItem()) >= 20) {
                    removeItems(player, LKBlocks.CORRUPT_PRIDESTONE.get().asItem(), 20);
                    progress(COLLECT_CORRUPT_STONE);
                    player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fCorrupt pridestone collected! Now defeat 15 outlanders."), false);
                    data.setDirty();
                    return true;
                }
                break;

            case COLLECT_CORRUPT_STONE:
                // Tracked via kill counter — called when count reaches 15
                progress(KILL_OUTLANDERS);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fThe outlanders are subdued. Find the Outlands Altar!"), false);
                data.setDirty();
                return true;

            case KILL_OUTLANDERS:
                // Triggered when player finds and interacts with the altar
                progress(FIND_ALTAR);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fYou found the altar! Place corrupt pridestone on it."), false);
                data.setDirty();
                return true;

            case FIND_ALTAR:
                // Triggered by placing offering on altar block
                progress(PLACE_OFFERING);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fThe altar rumbles... The Termite Queen awakens!"), false);
                data.setDirty();
                return true;

            case PLACE_OFFERING:
                // Triggered when Termite Queen is killed
                progress(DEFEAT_TERMITE_QUEEN);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fThe Termite Queen is slain! Now defeat Zira!"), false);
                data.setDirty();
                return true;

            case DEFEAT_TERMITE_QUEEN:
                // Triggered when Zira is killed
                progress(DEFEAT_ZIRA);
                data.outlandersHostile = false;
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fZira has fallen! Return to the Pride Lands."), false);
                data.setDirty();
                return true;

            case DEFEAT_ZIRA:
                // Check if player returned to Pride Lands
                if (player.level().dimension() == LKDimensions.PRIDE_LANDS_LEVEL
                        || player.level().dimension() == net.minecraft.world.level.Level.OVERWORLD) {
                    progress(RETURN_HOME);
                    player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fWelcome home! Speak to Rafiki for your reward."), false);
                    data.setDirty();
                    return true;
                }
                break;

            case RETURN_HOME:
                // Triggered by speaking to Rafiki — give reward and complete
                giveReward(player);
                progress(COMPLETE);
                player.displayClientMessage(Component.literal("\u00a76[Quest Complete] \u00a7fThe Outlands quest is complete! Zira's threat has been ended."), false);
                data.setDirty();
                return true;

            default:
                break;
        }
        return false;
    }

    /**
     * Gives the completion reward to the player.
     */
    private void giveReward(ServerPlayer player) {
        player.getInventory().placeItemBackInInventory(new ItemStack(LKItems.WAYWARD_FEATHER.get(), 3));
        player.getInventory().placeItemBackInInventory(new ItemStack(LKItems.KIVULITE.get(), 8));
        player.getInventory().placeItemBackInInventory(new ItemStack(Items.DIAMOND, 8));
        player.getInventory().placeItemBackInInventory(new ItemStack(LKItems.AMULET.get(), 1));
    }

    /**
     * Counts how many of a given item the player has in their inventory.
     */
    private int countItem(ServerPlayer player, net.minecraft.world.item.Item item) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /**
     * Removes a specific number of items from the player's inventory.
     */
    private void removeItems(ServerPlayer player, net.minecraft.world.item.Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                int toRemove = Math.min(remaining, stack.getCount());
                stack.shrink(toRemove);
                remaining -= toRemove;
            }
        }
    }
}
