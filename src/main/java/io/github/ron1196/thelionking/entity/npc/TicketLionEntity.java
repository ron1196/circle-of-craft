package io.github.ron1196.thelionking.entity.npc;

import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TicketLionEntity extends PathfinderMob {

    private int talkCooldown = 0;

    public TicketLionEntity(EntityType<? extends TicketLionEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (talkCooldown > 0) talkCooldown--;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;
        if (talkCooldown > 0) return InteractionResult.SUCCESS;

        talkCooldown = 40;

        ItemStack held = player.getItemInHand(hand);

        // Gold ingot exchange for ticket
        if (held.is(net.minecraft.world.item.Items.GOLD_INGOT)) {
            held.shrink(1);
            player.addItem(new ItemStack(Items.TICKET.get()));
            sendMessage(player, "Thank you. Use the ticket to open the portal in the room behind me.");
            return InteractionResult.SUCCESS;
        }

        sendMessage(player, "Bring me a gold ingot and I will exchange it for a Lion King Ticket.");
        return InteractionResult.SUCCESS;
    }

    private void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal("\u00a7e<Ticket Lion> \u00a7f" + message));
    }
}
