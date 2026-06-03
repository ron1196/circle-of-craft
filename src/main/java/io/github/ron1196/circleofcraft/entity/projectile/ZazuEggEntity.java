package io.github.ron1196.circleofcraft.entity.projectile;

import io.github.ron1196.circleofcraft.entity.animal.ZazuEntity;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
        super(EntityTypes.ZAZU_EGG.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ZAZU_EGG.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // 1/8 chance to spawn a Zazu
            if (this.random.nextInt(8) == 0) {
                ZazuEntity zazu = EntityTypes.ZAZU.get().create(this.level());
                if (zazu != null) {
                    zazu.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    this.level().addFreshEntity(zazu);
                }
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
            for (int i = 0; i < 8; i++) {
                this.level()
                        .addParticle(
                                particle,
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                (this.random.nextFloat() - 0.5) * 0.08,
                                (this.random.nextFloat() - 0.5) * 0.08,
                                (this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }
}
