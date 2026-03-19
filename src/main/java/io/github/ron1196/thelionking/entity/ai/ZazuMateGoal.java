package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.entity.animal.ZazuEntity;
import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Zazu periodically lays an egg (drops a zazu_egg item). Approximately every 5 minutes (6000
 * ticks).
 */
public class ZazuMateGoal extends Goal {

    private final ZazuEntity zazu;
    private int layTimer;
    private static final int LAY_INTERVAL = 6000; // ~5 minutes

    public ZazuMateGoal(ZazuEntity zazu) {
        this.zazu = zazu;
        this.layTimer = zazu.getRandom().nextInt(LAY_INTERVAL); // Randomize initial timer
    }

    @Override
    public boolean canUse() {
        return !zazu.isBaby();
    }

    @Override
    public boolean canContinueToUse() {
        return !zazu.isBaby();
    }

    @Override
    public void tick() {
        if (zazu.level().isClientSide()) {
            return;
        }
        layTimer++;
        if (layTimer < LAY_INTERVAL) {
            return;
        }
        layTimer = 0;
        ItemEntity egg = new ItemEntity(
                zazu.level(), zazu.getX(), zazu.getY(), zazu.getZ(), new ItemStack(LionKingItems.ZAZU_EGG.get()));
        egg.setDefaultPickUpDelay();
        zazu.level().addFreshEntity(egg);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
