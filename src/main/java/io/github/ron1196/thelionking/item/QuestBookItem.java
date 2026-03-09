package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.client.gui.QuestBookScreen;
import io.github.ron1196.thelionking.quest.LKQuestBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class QuestBookItem extends Item {

    public QuestBookItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            Minecraft.getInstance().setScreen(new QuestBookScreen());
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return LKQuestBase.anyUncheckedQuests();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (LKQuestBase.anyUncheckedQuests()) {
            tooltip.add(Component.literal("\u00a7eNew quests available"));
        }
    }
}
