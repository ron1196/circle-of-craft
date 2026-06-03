package io.github.ron1196.circleofcraft.entity.projectile;

import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class TermiteThrownEntity extends ThrowableItemProjectile {

    public TermiteThrownEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public TermiteThrownEntity(Level level, LivingEntity shooter) {
        super(EntityTypes.TERMITE_THROWN.get(), shooter, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.TERMITE_THROWN.get();
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) return;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.8F, Level.ExplosionInteraction.BLOCK);
        this.discard();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }
}
