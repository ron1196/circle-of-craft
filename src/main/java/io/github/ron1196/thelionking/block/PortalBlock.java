package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.world.dimension.Teleporter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import org.jetbrains.annotations.NotNull;

public class PortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape X_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape Z_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    // Countdown ticks per player while they stand in the portal.
    private static final Map<UUID, Integer> PORTAL_TICKS = new ConcurrentHashMap<>();

    private final boolean isOutlands;
    private final ResourceKey<Level> dimensionA;
    private final ResourceKey<Level> dimensionB;

    public PortalBlock(
            Properties properties, boolean isOutlands, ResourceKey<Level> dimensionA, ResourceKey<Level> dimensionB) {
        super(properties);
        this.isOutlands = isOutlands;
        this.dimensionA = dimensionA;
        this.dimensionB = dimensionB;
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

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState updateShape(
            BlockState state,
            Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        if (direction.getAxis() == state.getValue(AXIS)) return state;
        return isValidPortalFrame(level, pos) ? state : Blocks.AIR.defaultBlockState();
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
        int ticks = PORTAL_TICKS.merge(player.getUUID(), 1, Integer::sum);
        if (ticks < player.getPortalWaitTime()) return;
        PORTAL_TICKS.remove(player.getUUID());
        player.setPortalCooldown();
        teleportPlayer(player);
    }

    private void teleportPlayer(ServerPlayer player) {
        if (inInvalidDimension(player.level())) {
            player.sendSystemMessage(Component.literal("Hakuna Matata! This portal doesn't work here, cheater!"));
            return;
        }
        ServerLevel destLevel = resolveDestination(player.level());
        if (destLevel == null) return;
        player.changeDimension(destLevel, new Teleporter(this));
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
        return dim != dimensionA && dim != dimensionB;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private ServerLevel resolveDestination(Level level) {
        ResourceKey<Level> destination = getDestination(level);
        return level.getServer() != null ? level.getServer().getLevel(destination) : null;
    }

    private ResourceKey<Level> getDestination(Level level) {
        return level.dimension() == dimensionA ? dimensionB : dimensionA;
    }

    private Block getFrameBlock() {
        return isOutlands ? LionKingBlocks.OUTLANDS_PORTAL_FRAME.get() : LionKingBlocks.PRIDE_PORTAL_FRAME.get();
    }

    private boolean isValidPortalFrame(LevelAccessor level, BlockPos pos) {
        Block frameBlock = getFrameBlock();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockState state = level.getBlockState(pos.relative(dir));
            if (!state.is(this) && !state.is(frameBlock)) {
                return false;
            }
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
            // Top and bottom rows (x = -1..width includes corners)
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
