package io.github.ron1196.thelionking.quest;

import io.github.ron1196.thelionking.data.LKLevelData;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LKQuestRafiki extends LKQuestBase {

    // Stage constants
    public static final int NOT_STARTED = 0;
    public static final int TALK_TO_RAFIKI = 1;
    public static final int COLLECT_BONES = 2;
    public static final int FIND_SIMBA = 3;
    public static final int DEFEAT_HYENAS = 4;
    public static final int DEFEAT_SCAR = 5;
    public static final int RETURN_TO_RAFIKI = 6;
    public static final int COMPLETE = 7;

    public LKQuestRafiki(int index) {
        super(index);
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public String[] getRequirements() {
        return null;
    }

    @Override
    public int getNumStages() {
        return 7;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(LKItems.STAFF.get());
    }

    @Override
    public String getObjectiveByStage(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "Find Rafiki at his tree and speak to him";
            case TALK_TO_RAFIKI -> "Bring Rafiki 5 hyena bones";
            case COLLECT_BONES -> "Find Simba somewhere in the Pride Lands";
            case FIND_SIMBA -> "Defeat 10 hyenas";
            case DEFEAT_HYENAS -> "Defeat Scar";
            case DEFEAT_SCAR -> "Return to Rafiki and report your victory";
            case RETURN_TO_RAFIKI -> "Receive your reward from Rafiki";
            case COMPLETE -> "Quest complete!";
            default -> "";
        };
    }

    /**
     * Returns a human-readable description of the current quest objective.
     */
    public String getQuestDescription(int stage) {
        return switch (stage) {
            case NOT_STARTED -> "You have not yet begun Rafiki's Quest. Seek out Rafiki at his tree in the centre of the Pride Lands.";
            case TALK_TO_RAFIKI -> "Rafiki has asked you to collect 5 hyena bones and bring them back to him.";
            case COLLECT_BONES -> "Rafiki senses a great warrior nearby. Find Simba somewhere in the Pride Lands.";
            case FIND_SIMBA -> "Simba needs your help to reclaim the Pride Lands. Defeat 10 hyenas to weaken Scar's forces.";
            case DEFEAT_HYENAS -> "The hyena forces are weakened. Now find and defeat Scar to restore peace to the Pride Lands.";
            case DEFEAT_SCAR -> "Scar has been defeated! Return to Rafiki at his tree to tell him the good news.";
            case RETURN_TO_RAFIKI -> "Speak with Rafiki to receive your well-earned reward.";
            case COMPLETE -> "You have completed Rafiki's Quest and restored peace to the Pride Lands!";
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
                // Triggered by interacting with Rafiki NPC — advance to stage 1
                // This is called from Rafiki's interaction handler
                progress(TALK_TO_RAFIKI);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fRafiki's Quest has begun! Collect 5 hyena bones."), false);
                return true;

            case TALK_TO_RAFIKI:
                // Check if player has 5 hyena bones
                if (countItem(player, LKItems.HYENA_BONE.get()) >= 5) {
                    removeItems(player, LKItems.HYENA_BONE.get(), 5);
                    progress(COLLECT_BONES);
                    player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fRafiki accepts the bones. Now find Simba!"), false);
                    data.setDirty();
                    return true;
                }
                break;

            case COLLECT_BONES:
                // Triggered when player finds and interacts with Simba NPC
                progress(FIND_SIMBA);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fYou found Simba! Help him by defeating 10 hyenas."), false);
                data.setDirty();
                return true;

            case FIND_SIMBA:
                // Tracked via kill counter in LKLevelData or entity death event
                // This is called when the kill count reaches 10
                progress(DEFEAT_HYENAS);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fThe hyenas are weakened! Now defeat Scar!"), false);
                data.setDirty();
                return true;

            case DEFEAT_HYENAS:
                // Triggered when Scar is killed
                progress(DEFEAT_SCAR);
                data.defeatedScar = true;
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fScar has been defeated! Return to Rafiki."), false);
                data.setDirty();
                return true;

            case DEFEAT_SCAR:
                // Triggered by interacting with Rafiki after defeating Scar
                progress(RETURN_TO_RAFIKI);
                player.displayClientMessage(Component.literal("\u00a7e[Quest] \u00a7fRafiki is pleased! Speak to him once more for your reward."), false);
                data.setDirty();
                return true;

            case RETURN_TO_RAFIKI:
                // Give reward and complete
                giveReward(player);
                progress(COMPLETE);
                player.displayClientMessage(Component.literal("\u00a76[Quest Complete] \u00a7fRafiki's Quest is complete! You have restored peace to the Pride Lands."), false);
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
        // Reward: Simba Charm, some gold, and a Staff
        player.getInventory().placeItemBackInInventory(new ItemStack(LKItems.SIMBA_CHARM.get(), 1));
        player.getInventory().placeItemBackInInventory(new ItemStack(Items.GOLD_INGOT, 16));
        player.getInventory().placeItemBackInInventory(new ItemStack(LKItems.STAFF.get(), 1));
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
