package io.github.ron1196.circleofcraft.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class TicketItem extends Item {

    public TicketItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Portal activation will be implemented in Phase 7 (dimensions)
        // For now, the ticket is just an item obtained from the Ticket Lion
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
