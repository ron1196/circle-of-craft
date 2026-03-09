package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.entity.animal.ZazuEntity;
import io.github.ron1196.thelionking.registry.LKEntityTypes;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ZazuEggEntity extends ThrowableItemProjectile {

    public ZazuEggEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ZazuEggEntity(Level level, LivingEntity shooter) {
        super(LKEntityTypes.ZAZU_EGG.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return LKItems.ZAZU_EGG.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // 1/8 chance to spawn a Zazu
            if (this.random.nextInt(8) == 0) {
                ZazuEntity zazu = LKEntityTypes.ZAZU.get().create(this.level());
                if (zazu != null) {
                    zazu.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    this.level().addFreshEntity(zazu);
                }
            }
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
