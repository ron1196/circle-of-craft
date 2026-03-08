package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class PumbaaBombEntity extends ThrowableItemProjectile {

    public PumbaaBombEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public PumbaaBombEntity(Level level, LivingEntity shooter) {
        super(LKEntityTypes.PUMBAA_BOMB.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return LKItems.PUMBAA_BOMB.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(),
                    5.0F, false, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
