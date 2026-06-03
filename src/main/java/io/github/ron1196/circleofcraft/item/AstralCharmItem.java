package io.github.ron1196.circleofcraft.item;

import io.github.ron1196.circleofcraft.entity.projectile.LightningBoltEntity;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModDataComponents;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Astral Charm — reactivated by placing an inactive charm on a Star Altar. Spawns a visual
 * lightning bolt and particle effects, returns an active charm.
 */
public class AstralCharmItem extends Item {

    private static final int PICKUP_DELAY_TICKS = 10;
    private static final double ITEM_UPWARD_SPEED = 0.4D;
    private static final double ITEM_UPWARD_JITTER_DIVISOR = 10.0D;
    private static final double ITEM_HORIZONTAL_OFFSET = 0.25D;
    private static final double ITEM_HORIZONTAL_JITTER_DIVISOR = 2.0D;
    private static final int STAR_PARTICLE_COUNT = 64;
    private static final double PARTICLE_SPREAD = 2.0D;
    private static final double PARTICLE_OFFSET = 0.5D;
    private static final double PARTICLE_VELOCITY_SPREAD = 0.5D;
    private static final double PARTICLE_SPEED = 0.1D;
    private static final int LIGHTNING_POWER = 0;

    public AstralCharmItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @Nullable Level level,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (isActive(stack)) {
            tooltip.add(Component.translatable("item.circleofcraft.astral_charm.active.hint")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("item.circleofcraft.astral_charm.inactive.hint")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public static boolean isActive(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ASTRAL_ACTIVE.get(), false);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return isActive(stack);
    }

    public static ItemStack createActive() {
        ItemStack stack = new ItemStack(ModItems.ASTRAL_CHARM.get());
        stack.set(ModDataComponents.ASTRAL_ACTIVE.get(), true);
        return stack;
    }

    public static ItemStack createInactive() {
        return new ItemStack(ModItems.ASTRAL_CHARM.get());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        // Must be an inactive charm used on a Star Altar
        if (isActive(stack)) {
            return InteractionResult.PASS;
        }
        if (!level.getBlockState(context.getClickedPos()).is(ModBlocks.STAR_ALTAR.get())) {
            return InteractionResult.PASS;
        }

        double x = context.getClickedPos().getX();
        double y = context.getClickedPos().getY();
        double z = context.getClickedPos().getZ();

        // Consume the inactive charm
        stack.shrink(1);

        // Drop an active charm with upward motion
        ItemStack activeCharm = createActive();
        ItemEntity item = new ItemEntity(
                level,
                x + ITEM_HORIZONTAL_OFFSET + (level.random.nextFloat() / ITEM_HORIZONTAL_JITTER_DIVISOR),
                y,
                z + ITEM_HORIZONTAL_OFFSET + (level.random.nextFloat() / ITEM_HORIZONTAL_JITTER_DIVISOR),
                activeCharm);
        item.setPickUpDelay(PICKUP_DELAY_TICKS);
        item.setDeltaMovement(0.0D, ITEM_UPWARD_SPEED + (level.random.nextFloat() / ITEM_UPWARD_JITTER_DIVISOR), 0.0D);
        level.addFreshEntity(item);

        // Visual lightning bolt (no damage)
        level.addFreshEntity(new LightningBoltEntity(level, x, y, z, LIGHTNING_POWER, player));

        // Spawn star particles
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < STAR_PARTICLE_COUNT; i++) {
                double px = x - PARTICLE_OFFSET + level.random.nextFloat() * PARTICLE_SPREAD;
                double py = y - PARTICLE_OFFSET + level.random.nextFloat() * PARTICLE_SPREAD;
                double pz = z - PARTICLE_OFFSET + level.random.nextFloat() * PARTICLE_SPREAD;
                double dx = (level.random.nextFloat() - PARTICLE_OFFSET) * PARTICLE_VELOCITY_SPREAD;
                double dy = (level.random.nextFloat() - PARTICLE_OFFSET) * PARTICLE_VELOCITY_SPREAD;
                double dz = (level.random.nextFloat() - PARTICLE_OFFSET) * PARTICLE_VELOCITY_SPREAD;
                serverLevel.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, dx, dy, dz, PARTICLE_SPEED);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
