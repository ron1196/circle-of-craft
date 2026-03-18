package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.entity.hostile.TermiteEntity;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownTermiteEntity extends ThrowableItemProjectile {

    public ThrownTermiteEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownTermiteEntity(Level level, LivingEntity shooter) {
        super(EntityTypes.THROWN_TERMITE.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.BUG.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            TermiteEntity termite = EntityTypes.TERMITE.get().create(this.level());
            if (termite != null) {
                termite.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                this.level().addFreshEntity(termite);
            }
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
