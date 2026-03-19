package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class SpawnerBlockEntity extends BlockEntity {

  private ResourceLocation entityId = null;
  private int delay = -1;
  private int minSpawnDelay = 200;
  private int maxSpawnDelay = 800;
  private int spawnCount = 4;
  private int maxNearbyEntities = 6;
  private int requiredPlayerRange = 16;
  private int spawnRange = 4;

  public SpawnerBlockEntity(BlockPos pos, BlockState state) {
    super(BlockEntityTypes.LK_SPAWNER.get(), pos, state);
  }

  public void serverTick() {
    if (level == null || !(level instanceof ServerLevel serverLevel)) return;
    if (entityId == null) return;

    // Check for nearby players
    if (!level.hasNearbyAlivePlayer(
        worldPosition.getX() + 0.5,
        worldPosition.getY() + 0.5,
        worldPosition.getZ() + 0.5,
        requiredPlayerRange)) {
      return;
    }

    if (delay < 0) {
      delay = minSpawnDelay + level.random.nextInt(Math.max(1, maxSpawnDelay - minSpawnDelay));
    }

    if (delay > 0) {
      delay--;
      return;
    }

    Optional<EntityType<?>> optType = EntityType.byString(entityId.toString());
    if (optType.isEmpty()) return;
    EntityType<?> type = optType.get();

    boolean spawned = false;
    for (int i = 0; i < spawnCount; i++) {
      double x = worldPosition.getX() + (level.random.nextDouble() - 0.5) * spawnRange * 2;
      double y = worldPosition.getY() + level.random.nextInt(3) - 1;
      double z = worldPosition.getZ() + (level.random.nextDouble() - 0.5) * spawnRange * 2;

      // Check nearby entity count
      AABB area = new AABB(worldPosition).inflate(spawnRange * 2, 4, spawnRange * 2);
      long count = level.getEntitiesOfClass(Entity.class, area, e -> e.getType() == type).size();
      if (count >= maxNearbyEntities) break;

      Entity entity = type.create(serverLevel);
      if (entity == null) continue;

      entity.moveTo(x, y, z, level.random.nextFloat() * 360.0F, 0.0F);
      if (entity instanceof Mob mob) {
        if (!mob.checkSpawnRules(serverLevel, MobSpawnType.SPAWNER)) {
          entity.discard();
          continue;
        }
        mob.finalizeSpawn(
            serverLevel,
            serverLevel.getCurrentDifficultyAt(entity.blockPosition()),
            MobSpawnType.SPAWNER,
            null,
            null);
      }
      serverLevel.addFreshEntityWithPassengers(entity);
      spawned = true;
    }

    if (spawned) {
      delay = minSpawnDelay + level.random.nextInt(Math.max(1, maxSpawnDelay - minSpawnDelay));
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    if (entityId != null) tag.putString("EntityId", entityId.toString());
    tag.putShort("Delay", (short) delay);
    tag.putShort("MinSpawnDelay", (short) minSpawnDelay);
    tag.putShort("MaxSpawnDelay", (short) maxSpawnDelay);
    tag.putShort("SpawnCount", (short) spawnCount);
    tag.putShort("MaxNearbyEntities", (short) maxNearbyEntities);
    tag.putShort("RequiredPlayerRange", (short) requiredPlayerRange);
    tag.putShort("SpawnRange", (short) spawnRange);
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    if (tag.contains("EntityId")) entityId = new ResourceLocation(tag.getString("EntityId"));
    delay = tag.getShort("Delay");
    minSpawnDelay = tag.getShort("MinSpawnDelay");
    maxSpawnDelay = tag.getShort("MaxSpawnDelay");
    spawnCount = tag.getShort("SpawnCount");
    maxNearbyEntities = tag.getShort("MaxNearbyEntities");
    requiredPlayerRange = tag.getShort("RequiredPlayerRange");
    spawnRange = tag.getShort("SpawnRange");
  }
}
