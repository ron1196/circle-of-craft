package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.quest.CharacterSpeech;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineManager;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline;
import io.github.ron1196.circleofcraft.quest.questline.RafikiQuestline.Stage;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Single source of truth for Rafiki's trades. Drives both gameplay (RafikiEntity#trySideInteraction)
 * and the JEI display (ModJeiPlugin#rafikiTrades).
 *
 * <p>Gameplay rule: input1 must be in the main hand at the listed count; for two-input trades the
 * second input must also be present in the player's inventory. {@code symmetric=true} means the two
 * inputs are interchangeable across hand and inventory.
 */
public final class RafikiTrades {

    public record Trade(
            @NotNull ItemStack input1,
            @NotNull ItemStack input2,
            @NotNull ItemStack output,
            @NotNull Stage minStage,
            boolean symmetric,
            @NotNull CharacterSpeech completionSpeech) {

        public boolean hasSecondInput() {
            return !input2.isEmpty();
        }
    }

    public static final List<Trade> ALL = List.of(
            new Trade(
                    new ItemStack(ModItems.SILVER_INGOT.get(), 3),
                    ItemStack.EMPTY,
                    new ItemStack(ModItems.RAFIKI_COIN.get()),
                    Stage.CRAFT_RAFIKI_STICK,
                    false,
                    CharacterSpeech.RAFIKI_COIN_SOLD),
            new Trade(
                    new ItemStack(Items.BOOK),
                    new ItemStack(ModItems.LION_FUR.get()),
                    new ItemStack(ModItems.QUEST_BOOK.get()),
                    Stage.CRAFT_RAFIKI_STICK,
                    false,
                    CharacterSpeech.RAFIKI_BOOK_RESOLD),
            new Trade(
                    new ItemStack(ModItems.HYENA_BONE.get(), 64),
                    ItemStack.EMPTY,
                    new ItemStack(ModItems.RAFIKI_STICK.get()),
                    Stage.DEFEAT_SCAR,
                    false,
                    CharacterSpeech.RAFIKI_EXTRA_STICK),
            new Trade(
                    new ItemStack(ModItems.TERMITE_DUST.get()),
                    new ItemStack(ModItems.MANGO_DUST.get()),
                    new ItemStack(ModItems.RAFIKI_DUST.get()),
                    Stage.USE_STAR_ALTAR,
                    true,
                    CharacterSpeech.RAFIKI_DUST_MADE));

    private RafikiTrades() {}

    /**
     * Attempts to execute any matching trade. Returns the executed trade so the caller can fire the
     * completion dialogue, or null if no trade applied.
     */
    @Nullable
    public static Trade tryExecute(@NotNull Player player, @NotNull QuestlineManager quests) {
        for (Trade trade : ALL) {
            if (!quests.isStageAtOrPast(RafikiQuestline.QUEST_ID, trade.minStage())) continue;
            if (execute(trade, player)) return trade;
        }
        return null;
    }

    private static boolean execute(Trade trade, Player player) {
        ItemStack held = player.getMainHandItem();

        if (!trade.hasSecondInput()) {
            if (!held.is(trade.input1().getItem())
                    || held.getCount() < trade.input1().getCount()) return false;
            held.shrink(trade.input1().getCount());
            player.addItem(trade.output().copy());
            return true;
        }

        // Two inputs — try the documented direction first, then the symmetric flip.
        if (tryConsume(player, held, trade.input1(), trade.input2())) {
            player.addItem(trade.output().copy());
            return true;
        }
        if (trade.symmetric() && tryConsume(player, held, trade.input2(), trade.input1())) {
            player.addItem(trade.output().copy());
            return true;
        }
        return false;
    }

    private static boolean tryConsume(Player player, ItemStack held, ItemStack handTemplate, ItemStack invTemplate) {
        if (!held.is(handTemplate.getItem()) || held.getCount() < handTemplate.getCount()) return false;
        if (!hasInventoryItem(player, invTemplate.getItem(), invTemplate.getCount())) return false;
        held.shrink(handTemplate.getCount());
        consumeInventoryItem(player, invTemplate.getItem(), invTemplate.getCount());
        return true;
    }

    private static boolean hasInventoryItem(Player player, Item item, int count) {
        int found = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(item)) {
                found += stack.getCount();
                if (found >= count) return true;
            }
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
