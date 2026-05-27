package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.menu.QuestBookMenu;
import io.github.ron1196.circleofcraft.network.ClientWorldState;
import io.github.ron1196.circleofcraft.quest.questline.Questline;
import io.github.ron1196.circleofcraft.quest.questline.QuestlineRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class QuestBookItem extends Item {

    public QuestBookItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("container.circleofcraft.quest_book");
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(
                        int containerId, @NotNull Inventory inv, @NotNull Player p) {
                    return new QuestBookMenu(containerId, inv);
                }
            });
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
            @NotNull TooltipFlag flag) {
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
