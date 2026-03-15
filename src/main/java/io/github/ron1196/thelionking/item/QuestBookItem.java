package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.network.ClientWorldState;
import io.github.ron1196.thelionking.quest.questline.Questline;
import io.github.ron1196.thelionking.quest.questline.QuestlineRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class QuestBookItem extends Item {

    public QuestBookItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            Level level,
            @NotNull Player player,
            @NotNull InteractionHand hand
    ) {
        if (level.isClientSide()) {
            QuestBookClientHelper.openScreen();
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return hasUncheckedQuests();
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @Nullable Level level,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag
    ) {
        if (hasUncheckedQuests()) {
            tooltip.add(Component.literal("§eNew quests available"));
        }
    }

    private static boolean hasUncheckedQuests() {
        for (Questline quest : QuestlineRegistry.getOrdered()) {
            if (!ClientWorldState.isQuestChecked(quest.getId())) return true;
        }
        return false;
    }
}
