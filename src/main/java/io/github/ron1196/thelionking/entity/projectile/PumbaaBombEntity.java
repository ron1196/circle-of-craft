package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.network.FlatulencePacket;
import io.github.ron1196.thelionking.network.Networking;
import io.github.ron1196.thelionking.registry.EntityTypes;
import io.github.ron1196.thelionking.registry.LionKingItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PumbaaBombEntity extends ThrowableItemProjectile {

    private static final float EXPLOSION_RADIUS = 5.0F;
    private static final double FLATULENCE_RANGE = 15.0;

    public PumbaaBombEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public PumbaaBombEntity(Level level, LivingEntity shooter) {
        super(EntityTypes.PUMBAA_BOMB.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return LionKingItems.PUMBAA_BOMB.get();
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide) {
            return;
        }
        this.level()
                .explode(
                        this.getOwner(),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        EXPLOSION_RADIUS,
                        false,
                        Level.ExplosionInteraction.TNT);
        sendFlatulenceToNearbyPlayers();
        this.discard();
    }

    private void sendFlatulenceToNearbyPlayers() {
        AABB range = new AABB(
                getX() - FLATULENCE_RANGE,
                getY() - FLATULENCE_RANGE,
                getZ() - FLATULENCE_RANGE,
                getX() + FLATULENCE_RANGE,
                getY() + FLATULENCE_RANGE,
                getZ() + FLATULENCE_RANGE);
        for (Player player : level().getEntitiesOfClass(Player.class, range)) {
            if (player instanceof ServerPlayer serverPlayer) {
                Networking.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new FlatulencePacket());
            }
        }
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }
}
