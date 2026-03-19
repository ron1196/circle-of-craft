package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import io.github.ron1196.thelionking.world.dimension.Teleporter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    protected static final VoxelShape X_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    // Countdown ticks per player while they stand in the portal.
    private static final Map<UUID, Integer> PORTAL_TICKS = new ConcurrentHashMap<>();

    // Saved entry position per (player, source dimension) for the return trip.
    private record ReturnKey(UUID uuid, ResourceKey<Level> dimension) {}

    private record SavedPosition(Vec3 pos, float yaw, float xRot) {}

    private static final Map<ReturnKey, SavedPosition> RETURN_POSITIONS = new ConcurrentHashMap<>();

    private final boolean isOutlands;

    public PortalBlock(Properties properties, boolean isOutlands) {
        super(properties);
        this.isOutlands = isOutlands;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

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
    public @NotNull BlockState updateShape(
            BlockState state,
            Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        Direction.Axis portalAxis = state.getValue(AXIS);
        if (direction.getAxis() == portalAxis) {
            // Check vertical neighbors
            return state;
        }
        if (!isValidPortalFrame(level, pos)) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    private boolean isValidPortalFrame(LevelAccessor level, BlockPos pos) {
        Block frameBlock = getFrameBlock();
        // Check we still have frame blocks around us
        for (Direction dir : Direction.values()) {
            if (dir == Direction.UP || dir == Direction.DOWN) continue;
            BlockPos neighbor = pos.relative(dir);
            BlockState state = level.getBlockState(neighbor);
            if (!state.is(this) && !state.is(frameBlock)) {
                return false;
            }
        }
        return true;
    }

    private Block getFrameBlock() {
        return isOutlands ? LionKingBlocks.OUTLANDS_PORTAL_FRAME.get() : LionKingBlocks.PRIDE_PORTAL_FRAME.get();
    }

    public boolean trySpawnPortal(LevelAccessor level, BlockPos pos) {
        PortalShape shape = new PortalShape(level, pos, Direction.Axis.X, getFrameBlock(), this);
        if (shape.isValid()) {
            shape.createPortalBlocks();
            return true;
        }
        shape = new PortalShape(level, pos, Direction.Axis.Z, getFrameBlock(), this);
        if (shape.isValid()) {
            shape.createPortalBlocks();
            return true;
        }
        return false;
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide || entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) return;
        if (entity.isOnPortalCooldown()) {
            entity.setPortalCooldown();
            return;
        }

        if (entity instanceof ServerPlayer player) {
            int ticks = PORTAL_TICKS.merge(player.getUUID(), 1, Integer::sum);
            if (ticks < player.getPortalWaitTime()) return;
            PORTAL_TICKS.remove(player.getUUID());
            player.setPortalCooldown();
            teleportPlayer(player);
        } else {
            // Non-player entities teleport instantly.
            entity.setPortalCooldown();
            ResourceKey<Level> destination = getDestination(level);
            ServerLevel destLevel =
                    level.getServer() != null ? level.getServer().getLevel(destination) : null;
            if (destLevel != null) {
                entity.changeDimension(destLevel, new Teleporter(this));
            }
        }
    }

    private void teleportPlayer(ServerPlayer player) {
        Level level = player.level();
        ResourceKey<Level> destination = getDestination(level);
        ServerLevel destLevel = player.server.getLevel(destination);
        if (destLevel == null) return;

        // Return the player to where they came from, or find/create a portal on first visit.
        ReturnKey returnKey = new ReturnKey(player.getUUID(), destination);
        SavedPosition savedPos = RETURN_POSITIONS.remove(returnKey);
        RETURN_POSITIONS.put(
                new ReturnKey(player.getUUID(), level.dimension()),
                new SavedPosition(player.position(), player.getYRot(), player.getXRot()));

        Teleporter teleporter = savedPos != null
                ? Teleporter.returning(savedPos.pos(), savedPos.yaw(), savedPos.xRot())
                : new Teleporter(this);
        player.changeDimension(destLevel, teleporter);
    }

    private ResourceKey<Level> getDestination(Level level) {
        if (isOutlands) {
            return level.dimension() == Dimensions.OUTLANDS_LEVEL ? Level.OVERWORLD : Dimensions.OUTLANDS_LEVEL;
        } else {
            return level.dimension() == Dimensions.PRIDE_LANDS_LEVEL ? Level.OVERWORLD : Dimensions.PRIDE_LANDS_LEVEL;
        }
    }

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

        /** Walks left then down from {@code pos} to find the bottom-left interior corner. */
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

        /** Counts consecutive interior blocks starting at {@code start} in {@code dir}. */
        private int countInterior(BlockPos start, Direction dir, int max) {
            int count = 0;
            for (BlockPos cursor = start; count < max && isInterior(cursor); cursor = cursor.relative(dir)) {
                count++;
            }
            return count;
        }

        private boolean isInterior(BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.isAir() || state.is(portalBlock);
        }

        private boolean isFrame(BlockPos pos) {
            return level.getBlockState(pos).is(frameBlock);
        }

        public boolean isValid() {
            if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) return false;

            // Side columns (left wall and right wall, y = 0..height-1)
            Direction leftDir = rightDir.getOpposite();
            for (int y = 0; y < height; y++) {
                if (!isFrame(bottomLeft.above(y).relative(leftDir))) return false;
                if (!isFrame(bottomLeft.above(y).relative(rightDir, width))) return false;
            }

            // Top and bottom rows, x = -1..width covers the corners too
            for (int x = -1; x <= width; x++) {
                BlockPos col = bottomLeft.relative(rightDir, x);
                if (!isFrame(col.below())) return false;
                if (!isFrame(col.above(height))) return false;
            }

            // Interior must be empty
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (!isInterior(bottomLeft.above(y).relative(rightDir, x))) return false;
                }
            }

            return true;
        }

        public void createPortalBlocks() {
            BlockState portalState = portalBlock.defaultBlockState();
            if (portalState.hasProperty(PortalBlock.AXIS)) {
                portalState = portalState.setValue(PortalBlock.AXIS, axis);
            }
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    level.setBlock(bottomLeft.above(y).relative(rightDir, x), portalState, 18);
                }
            }
        }
    }
}
