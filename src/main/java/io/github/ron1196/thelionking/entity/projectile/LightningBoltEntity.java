package io.github.ron1196.thelionking.entity.projectile;

import io.github.ron1196.thelionking.registry.EntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Custom lightning bolt used by Rafiki's Stick, Lion Dust, Simba's Charm, and Zira events.
 * Extends vanilla LightningBolt for rendering/sound, but overrides tick for custom behavior:
 * - power 0: visual/sound effect only (no damage or fire)
 * - power > 0: sets fire in a radius, damages and ignites nearby entities
 * Skips the casting player and friendly mod NPCs.
 */
public class LightningBoltEntity extends LightningBolt {

    private int power;
    private Player castingPlayer;
    private boolean firedSpawned;

    public LightningBoltEntity(EntityType<? extends LightningBolt> type, Level level) {
        super(type, level);
    }

    public LightningBoltEntity(Level level, double x, double y, double z, int power, Player castingPlayer) {
        super(EntityTypes.LK_LIGHTNING_BOLT.get(), level);
        this.power = power;
        this.castingPlayer = castingPlayer;
        this.setPos(x, y, z);
        // Prevent vanilla lightning from setting fires and converting entities
        this.setVisualOnly(power == 0);
    }

    @Override
    public void tick() {
        // Spawn fires on first tick for powered bolts
        if (!firedSpawned && power > 0 && !level().isClientSide) {
            firedSpawned = true;
            spawnFires();
        }

        // Let vanilla handle rendering state, sounds, and lifecycle
        super.tick();

        // Apply custom damage each active tick
        if (!level().isClientSide && power > 0) {
            damageNearbyEntities();
        }
    }

    private void spawnFires() {
        BlockPos center = blockPosition();
        tryPlaceFire(center);
        for (int i = 0; i < power * 3; i++) {
            BlockPos firePos = center.offset(
                    random.nextInt(3) - 1,
                    random.nextInt(3) - 1,
                    random.nextInt(3) - 1
            );
            tryPlaceFire(firePos);
        }
    }

    private void tryPlaceFire(BlockPos pos) {
        Level level = level();
        if (!level.isEmptyBlock(pos)) {
            return;
        }
        level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
    }

    private void damageNearbyEntities() {
        double range = 3.0D;
        AABB area = new AABB(
                getX() - range, getY() - range, getZ() - range,
                getX() + range, getY() + 6.0D + range, getZ() + range
        );
        List<Entity> entities = level().getEntities(this, area);

        for (Entity entity : entities) {
            if (shouldSkipEntity(entity)) {
                continue;
            }
            DamageSource source = castingPlayer != null
                    ? level().damageSources().playerAttack(castingPlayer)
                    : level().damageSources().inFire();
            entity.hurt(source, (power * 3) + (random.nextInt(3) * 2));
            entity.setSecondsOnFire(power + random.nextInt(4));
        }
    }

    private boolean shouldSkipEntity(Entity entity) {
        if (castingPlayer != null && entity == castingPlayer) {
            return true;
        }
        if (entity.fireImmune()) {
            return true;
        }
        return isLKFriendlyNPC(entity);
    }

    private boolean isLKFriendlyNPC(Entity entity) {
        EntityType<?> type = entity.getType();
        return type == EntityTypes.RAFIKI.get()
                || type == EntityTypes.SIMBA.get()
                || type == EntityTypes.TIMON.get()
                || type == EntityTypes.PUMBAA.get()
                || type == EntityTypes.TICKET_LION.get()
                || type == EntityTypes.ZAZU.get();
    }
}
