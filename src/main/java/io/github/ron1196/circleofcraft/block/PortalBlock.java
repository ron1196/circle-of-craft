package io.github.ron1196.circleofcraft.block;

import io.github.ron1196.circleofcraft.data.PlayerData;
import io.github.ron1196.circleofcraft.entity.npc.SimbaEntity;
import io.github.ron1196.circleofcraft.network.PlayerDataSyncPacket;
import io.github.ron1196.circleofcraft.network.PortalOverlayPacket;
import io.github.ron1196.circleofcraft.world.dimension.Teleporter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortalBlock extends Block {

    private record PortalCountdown(int ticks, long lastTick) {}

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape X_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape Z_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public static final int PORTAL_WAIT_TICKS = 100;

    private static final int PARTICLES_PER_TICK = 8;
    private static final int SOUND_CHANCE = 50;

    private static final Map<UUID, PortalCountdown> PORTAL_TICKS = new ConcurrentHashMap<>();

    private final Supplier<Block> frameBlockSupplier;
    private final Supplier<SimpleParticleType> particleTypeSupplier;
    private final ResourceKey<Level> homeDimension;
    private final ResourceKey<Level> targetDimension;

    public PortalBlock(
            Properties properties,
            Supplier<Block> frameBlockSupplier,
            Supplier<SimpleParticleType> particleTypeSupplier,
            ResourceKey<Level> homeDimension,
            ResourceKey<Level> targetDimension) {
        super(properties);
        this.frameBlockSupplier = frameBlockSupplier;
        this.particleTypeSupplier = particleTypeSupplier;
        this.homeDimension = homeDimension;
        this.targetDimension = targetDimension;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    // ── Block overrides ──────────────────────────────────────────────────────

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(
            BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_AABB : X_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public void animateTick(
            @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextInt(SOUND_CHANCE) == 0) {
            level.playLocalSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.BLOCKS,
                    0.5F,
                    random.nextFloat() * 0.4F + 0.8F,
                    false);
        }

        for (int i = 0; i < PARTICLES_PER_TICK; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(particleTypeSupplier.get(), x, y, z, 0, 0, 0);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        if (isPortalIntact(level, pos, state.getValue(AXIS))) return state;
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        return Blocks.AIR.defaultBlockState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(
            @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!canTeleport(level, entity)) return;
        if (entity instanceof ServerPlayer player) {
            handlePlayerCountdown(player);
        } else {
            teleportEntity(entity, level);
        }
    }

    // ── Portal spawning ──────────────────────────────────────────────────────

    public boolean trySpawnPortal(LevelAccessor level, BlockPos pos) {
        return trySpawnPortalOnAxis(level, pos, Direction.Axis.X) || trySpawnPortalOnAxis(level, pos, Direction.Axis.Z);
    }

    private boolean trySpawnPortalOnAxis(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        PortalShape shape = new PortalShape(level, pos, axis, getFrameBlock(), this);
        if (!shape.isValid()) return false;
        shape.createPortalBlocks();
        return true;
    }

    // ── Teleportation ────────────────────────────────────────────────────────

    private boolean canTeleport(Level level, Entity entity) {
        if (level.isClientSide || entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) {
            return false;
        }
        if (entity.isOnPortalCooldown()) {
            entity.setPortalCooldown();
            return false;
        }
        return true;
    }

    private void handlePlayerCountdown(ServerPlayer player) {
        long currentTick = player.level().getGameTime();
        UUID uuid = player.getUUID();
        var countdown = PORTAL_TICKS.compute(uuid, (_uuid, existing) -> advanceOrReset(existing, currentTick));

        sendOverlayPacket(player, countdown.ticks);

        if (countdown.ticks < PORTAL_WAIT_TICKS) return;
        PORTAL_TICKS.remove(uuid);

        player.setPortalCooldown();
        sendOverlayPacket(player, 0);
        teleportPlayer(player);
    }

    private void sendOverlayPacket(ServerPlayer player, int ticks) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(this);
        PacketDistributor.sendToPlayer(player, new PortalOverlayPacket(ticks, id.getPath()));
    }

    private static PortalCountdown advanceOrReset(@Nullable PortalCountdown existing, long currentTick) {
        if (existing == null || existing.lastTick < currentTick - 1) {
            return new PortalCountdown(1, currentTick);
        }
        // Already counted this tick (player overlaps multiple portal blocks)
        if (existing.lastTick == currentTick) {
            return existing;
        }
        return new PortalCountdown(existing.ticks + 1, currentTick);
    }

    private static final int SIMBA_TELEPORT_RANGE = 20;

    private void teleportPlayer(ServerPlayer player) {
        if (inInvalidDimension(player.level())) {
            player.sendSystemMessage(Component.literal("Hakuna Matata! This portal doesn't work here, cheater!"));
            return;
        }
        ServerLevel destLevel = resolveDestination(player.level());
        if (destLevel == null) return;

        if (player.level().dimension() == homeDimension) {
            saveHomePortalLocation(player);
        }

        teleportNearbySimba(player, destLevel);

        player.changeDimension(destLevel, new Teleporter(this));
    }

    private void saveHomePortalLocation(ServerPlayer player) {
        PlayerData data = PlayerData.get(player);
        BlockPos pos = player.blockPosition();
        data.setHomePortalX(pos.getX());
        data.setHomePortalY(pos.getY());
        data.setHomePortalZ(pos.getZ());
        PacketDistributor.sendToPlayer(player, PlayerDataSyncPacket.of(data));
    }

    private void teleportNearbySimba(ServerPlayer player, ServerLevel destLevel) {
        for (Entity entity : player.level()
                .getEntities(
                        (Entity) null,
                        player.getBoundingBox().inflate(SIMBA_TELEPORT_RANGE),
                        e -> e instanceof SimbaEntity simba && simba.isOwnedBy(player) && simba.hasCharm())) {
            entity.setPortalCooldown();
            entity.changeDimension(destLevel, new Teleporter(this));
        }
    }

    private void teleportEntity(Entity entity, Level level) {
        if (inInvalidDimension(level)) return;
        entity.setPortalCooldown();
        ServerLevel destLevel = resolveDestination(level);
        if (destLevel == null) return;
        entity.changeDimension(destLevel, new Teleporter(this));
    }

    private boolean inInvalidDimension(Level level) {
        ResourceKey<Level> dim = level.dimension();
        return dim != homeDimension && dim != targetDimension;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private ServerLevel resolveDestination(Level level) {
        ResourceKey<Level> destination = getDestination(level);
        return level.getServer() != null ? level.getServer().getLevel(destination) : null;
    }

    private ResourceKey<Level> getDestination(Level level) {
        return level.dimension() == homeDimension ? targetDimension : homeDimension;
    }

    public Block getFrameBlock() {
        return frameBlockSupplier.get();
    }

    private boolean isPortalIntact(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        Block frameBlock = getFrameBlock();

        // Check all neighbors within the portal plane: along-axis + up/down
        Direction[] directions = axis == Direction.Axis.X
                ? new Direction[] {Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN}
                : new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN};

        for (Direction dir : directions) {
            BlockState neighbor = level.getBlockState(pos.relative(dir));
            if (neighbor.is(this) || neighbor.is(frameBlock)) continue;
            return false;
        }

        return true;
    }

    // ── Portal shape detection ───────────────────────────────────────────────

    public static class PortalShape {
        private static final int MIN_WIDTH = 2;
        private static final int MIN_HEIGHT = 3;
        private static final int MAX_WIDTH = 21;
        private static final int MAX_HEIGHT = 21;

        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Block frameBlock;
        private final Block portalBlock;
        private final BlockPos bottomLeft;
        private final int width;
        private final int height;

        public PortalShape(
                LevelAccessor level, BlockPos pos, Direction.Axis axis, Block frameBlock, Block portalBlock) {
            this.level = level;
            this.axis = axis;
            this.rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
            this.frameBlock = frameBlock;
            this.portalBlock = portalBlock;
            this.bottomLeft = findBottomLeft(pos);
            this.width = countInterior(bottomLeft, rightDir, MAX_WIDTH);
            this.height = countInterior(bottomLeft, Direction.UP, MAX_HEIGHT);
        }

        public boolean isValid() {
            if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) return false;
            return hasValidFrame() && hasEmptyInterior();
        }

        public void createPortalBlocks() {
            BlockState portalState = buildPortalState();
            forEachInterior((x, y) -> level.setBlock(interiorPos(x, y), portalState, 18));
        }

        private BlockState buildPortalState() {
            BlockState state = portalBlock.defaultBlockState();
            return state.hasProperty(PortalBlock.AXIS) ? state.setValue(PortalBlock.AXIS, axis) : state;
        }

        // ── Shape detection helpers ──────────────────────────────────────────

        private BlockPos findBottomLeft(BlockPos pos) {
            Direction leftDir = rightDir.getOpposite();
            BlockPos cursor = pos;
            for (int i = 0; i < MAX_WIDTH && isInterior(cursor.relative(leftDir)); i++) {
                cursor = cursor.relative(leftDir);
            }
            for (int i = 0; i < MAX_HEIGHT && isInterior(cursor.below()); i++) {
                cursor = cursor.below();
            }
            return cursor;
        }

        private int countInterior(BlockPos start, Direction dir, int max) {
            int count = 0;
            for (BlockPos cursor = start; count < max && isInterior(cursor); cursor = cursor.relative(dir)) {
                count++;
            }
            return count;
        }

        // ── Frame validation ─────────────────────────────────────────────────

        private boolean hasValidFrame() {
            Direction leftDir = rightDir.getOpposite();
            // Side columns
            for (int y = 0; y < height; y++) {
                if (isNotFrame(bottomLeft.above(y).relative(leftDir))) return false;
                if (isNotFrame(bottomLeft.above(y).relative(rightDir, width))) return false;
            }
            // Top and bottom rows (x = -1...width includes corners)
            for (int x = -1; x <= width; x++) {
                BlockPos col = bottomLeft.relative(rightDir, x);
                if (isNotFrame(col.below())) return false;
                if (isNotFrame(col.above(height))) return false;
            }
            return true;
        }

        private boolean hasEmptyInterior() {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (!isInterior(interiorPos(x, y))) return false;
                }
            }
            return true;
        }

        // ── Block checks ────────────────────────────────────────────────────

        private BlockPos interiorPos(int x, int y) {
            return bottomLeft.above(y).relative(rightDir, x);
        }

        private boolean isInterior(BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.isAir() || state.is(portalBlock);
        }

        private boolean isNotFrame(BlockPos pos) {
            return !level.getBlockState(pos).is(frameBlock);
        }

        private void forEachInterior(InteriorAction action) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    action.apply(x, y);
                }
            }
        }

        @FunctionalInterface
        private interface InteriorAction {
            void apply(int x, int y);
        }
    }
}
