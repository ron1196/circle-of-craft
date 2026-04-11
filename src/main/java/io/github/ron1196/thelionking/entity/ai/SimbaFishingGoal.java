package io.github.ron1196.thelionking.entity.ai;

import io.github.ron1196.thelionking.data.LionKingCriteriaTriggers;
import io.github.ron1196.thelionking.entity.npc.SimbaEntity;
import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * When Simba is standing in water, he periodically catches a random fish and drops it at his
 * position. Triggers the fishing animation and produces splash effects on each catch.
 */
public class SimbaFishingGoal extends Goal {

    private static final int MIN_CATCH_TICKS = 20 * 20; // 20 seconds
    private static final int MAX_CATCH_TICKS = 40 * 20; // 40 seconds
    private static final double FISH_LAUNCH_VELOCITY = 0.3;
    private static final int PICKUP_DELAY_TICKS = 10;

    private static final ItemStack[] FISH_LOOT = {
        new ItemStack(Items.COD), new ItemStack(Items.SALMON), new ItemStack(Items.TROPICAL_FISH)
    };

    private final SimbaEntity simba;
    private int ticksUntilCatch;

    public SimbaFishingGoal(@NotNull SimbaEntity simba) {
        this.simba = simba;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !simba.isOrderedToSit() && simba.isInWater() && !simba.level().isClientSide();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        ticksUntilCatch = randomCatchDelay();
        simba.setFishing(true);
        if (simba.getOwner() instanceof ServerPlayer owner) {
            LionKingCriteriaTriggers.SIMBA_IN_WATER.trigger(owner);
        }
    }

    @Override
    public void stop() {
        simba.setFishing(false);
    }

    @Override
    public void tick() {
        ticksUntilCatch--;
        if (ticksUntilCatch <= 0) {
            catchFish();
            ticksUntilCatch = randomCatchDelay();
        }
    }

    private void catchFish() {
        if (!(simba.level() instanceof ServerLevel serverLevel)) return;

        simba.triggerAnim("main", "animation.simba.fishing");

        ItemStack fishStack = FISH_LOOT[simba.getRandom().nextInt(FISH_LOOT.length)].copy();
        ItemEntity fishItem = new ItemEntity(serverLevel, simba.getX(), simba.getY(), simba.getZ(), fishStack);
        fishItem.setPickUpDelay(PICKUP_DELAY_TICKS);
        fishItem.setDeltaMovement(0, FISH_LAUNCH_VELOCITY + simba.getRandom().nextFloat() / 3.0, 0);
        serverLevel.addFreshEntity(fishItem);

        serverLevel.sendParticles(
                ParticleTypes.SPLASH, simba.getX(), simba.getY() + 0.5, simba.getZ(), 10, 0.3, 0.2, 0.3, 0.1);

        simba.playSound(SoundEvents.PLAYER_SPLASH, 0.6F, 1.0F);
    }

    private int randomCatchDelay() {
        return MIN_CATCH_TICKS + simba.getRandom().nextInt(MAX_CATCH_TICKS - MIN_CATCH_TICKS);
    }
}
