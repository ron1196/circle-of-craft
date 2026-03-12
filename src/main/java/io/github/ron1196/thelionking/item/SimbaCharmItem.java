package io.github.ron1196.thelionking.item;

import io.github.ron1196.thelionking.entity.LKLightningBoltEntity;
import io.github.ron1196.thelionking.registry.LKBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Simba's Charm — reactivated by placing an inactive charm on a Star Altar.
 * Spawns a visual lightning bolt and particle effects, returns an active charm.
 *
 * Damage 0 = active, Damage 1 = inactive.
 * In 1.20.1 we don't use damage values for subtypes; instead we check the item tag or
 * register two items. For simplicity, we use a single item and check for a custom NBT tag.
 */
public class SimbaCharmItem extends Item {

    public SimbaCharmItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public static boolean isActive(ItemStack stack) {
        return !stack.getOrCreateTag().getBoolean("Inactive");
    }

    public static ItemStack createActive() {
        return new ItemStack(io.github.ron1196.thelionking.registry.LKItems.SIMBA_CHARM.get());
    }

    public static ItemStack createInactive() {
        ItemStack stack = new ItemStack(io.github.ron1196.thelionking.registry.LKItems.SIMBA_CHARM.get());
        stack.getOrCreateTag().putBoolean("Inactive", true);
        return stack;
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
        if (!level.getBlockState(context.getClickedPos()).is(LKBlocks.STAR_ALTAR.get())) {
            return InteractionResult.PASS;
        }

        double x = context.getClickedPos().getX();
        double y = context.getClickedPos().getY();
        double z = context.getClickedPos().getZ();

        // Consume the inactive charm
        stack.shrink(1);

        // Drop an active charm with upward motion
        ItemStack activeCharm = createActive();
        ItemEntity item = new ItemEntity(level,
                x + 0.25D + (level.random.nextFloat() / 2.0F), y,
                z + 0.25D + (level.random.nextFloat() / 2.0F), activeCharm);
        item.setPickUpDelay(10);
        item.setDeltaMovement(0.0D, 0.4D + (level.random.nextFloat() / 10.0F), 0.0D);
        level.addFreshEntity(item);

        // Visual lightning bolt (power 0 = no damage)
        level.addFreshEntity(new LKLightningBoltEntity(level, x, y, z, 0, player));

        // Spawn star particles
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 64; i++) {
                double px = x - 0.5F + level.random.nextFloat() * 2.0F;
                double py = y - 0.5F + level.random.nextFloat() * 2.0F;
                double pz = z - 0.5F + level.random.nextFloat() * 2.0F;
                double dx = (level.random.nextFloat() - 0.5D) * 0.5D;
                double dy = (level.random.nextFloat() - 0.5D) * 0.5D;
                double dz = (level.random.nextFloat() - 0.5D) * 0.5D;
                serverLevel.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, dx, dy, dz, 0.1D);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
