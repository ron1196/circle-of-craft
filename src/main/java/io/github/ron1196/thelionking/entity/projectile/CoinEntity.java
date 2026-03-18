package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class CoinEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<Integer> DATA_COIN_TYPE =
            SynchedEntityData.defineId(CoinEntity.class, EntityDataSerializers.INT);

    public static final int TYPE_RAFIKI = 0;
    public static final int TYPE_ZIRA = 1;

    public CoinEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public CoinEntity(Level level, LivingEntity shooter, int coinType) {
        super(EntityTypes.COIN.get(), shooter, level);
        this.entityData.set(DATA_COIN_TYPE, coinType);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COIN_TYPE, TYPE_RAFIKI);
    }

    public int getCoinType() {
        return this.entityData.get(DATA_COIN_TYPE);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.RAFIKI_COIN.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (owner instanceof LivingEntity living) {
                living.teleportTo(this.getX(), this.getY(), this.getZ());
            }
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
