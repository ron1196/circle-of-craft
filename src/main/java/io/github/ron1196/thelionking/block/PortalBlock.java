package io.github.ron1196.thelionking.block;

import io.github.ron1196.thelionking.registry.Blocks;
import io.github.ron1196.thelionking.world.dimension.Dimensions;
import io.github.ron1196.thelionking.world.dimension.Teleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    protected static final VoxelShape X_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    private final boolean isOutlands;

    public PortalBlock(Properties properties, boolean isOutlands) {
        super(properties);
        this.isOutlands = isOutlands;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public @NotNull VoxelShape getShape(
            BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
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
            @NotNull BlockPos neighborPos
    ) {
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
        return isOutlands ? Blocks.OUTLANDS_PORTAL_FRAME.get() : Blocks.PRIDE_PORTAL_FRAME.get();
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
    public void entityInside(
            @NotNull BlockState state,
            Level level,
            @NotNull BlockPos pos,
            @NotNull Entity entity
    ) {
        if (!level.isClientSide && !entity.isPassenger() && !entity.isVehicle()
                && entity.canChangeDimensions()) {
            if (entity.isOnPortalCooldown()) {
                entity.setPortalCooldown();
                return;
            }

            ResourceKey<Level> destination;
            if (isOutlands) {
                destination = level.dimension() == Dimensions.OUTLANDS_LEVEL
                        ? Level.OVERWORLD : Dimensions.OUTLANDS_LEVEL;
            } else {
                destination = level.dimension() == Dimensions.PRIDE_LANDS_LEVEL
                        ? Level.OVERWORLD : Dimensions.PRIDE_LANDS_LEVEL;
            }

            MinecraftServer server = level.getServer();
            if (server != null) {
                ServerLevel destLevel = server.getLevel(destination);
                if (destLevel != null) {
                    entity.setPortalCooldown();
                    entity.changeDimension(destLevel, new Teleporter(isOutlands));
                }
            }
        }
    }

    public static class PortalShape {
        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Block frameBlock;
        private final Block portalBlock;
        private BlockPos bottomLeft;
        private int width;
        private int height;

        public PortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis, Block frameBlock, Block portalBlock) {
            this.level = level;
            this.axis = axis;
            this.frameBlock = frameBlock;
            this.portalBlock = portalBlock;
            this.calculateShape(pos);
        }

        private void calculateShape(BlockPos pos) {
            // Find bottom-left corner
            Direction leftDir = axis == Direction.Axis.X ? Direction.WEST : Direction.NORTH;

            // Go left until we hit a frame block
            BlockPos cursor = pos;
            while (level.getBlockState(cursor.relative(leftDir)).isAir() ||
                    level.getBlockState(cursor.relative(leftDir)).is(portalBlock)) {
                cursor = cursor.relative(leftDir);
            }

            // Go down until we hit a frame block
            while (level.getBlockState(cursor.below()).isAir() ||
                    level.getBlockState(cursor.below()).is(portalBlock)) {
                cursor = cursor.below();
            }

            this.bottomLeft = cursor;

            // Calculate width (should be 2)
            Direction rightDir = leftDir.getOpposite();
            this.width = 0;
            cursor = bottomLeft;
            while (this.width < 4) {
                BlockState state = level.getBlockState(cursor);
                if (!state.isAir() && !state.is(portalBlock)) break;
                cursor = cursor.relative(rightDir);
                this.width++;
            }

            // Calculate height (should be 3)
            this.height = 0;
            cursor = bottomLeft;
            while (this.height < 5) {
                BlockState state = level.getBlockState(cursor);
                if (!state.isAir() && !state.is(portalBlock)) break;
                cursor = cursor.above();
                this.height++;
            }
        }

        public boolean isValid() {
            if (width != 2 || height != 3) return false;

            Direction leftDir = axis == Direction.Axis.X ? Direction.WEST : Direction.NORTH;
            Direction rightDir = leftDir.getOpposite();

            // Check frame blocks
            for (int y = 0; y < height; y++) {
                // Left column
                if (!level.getBlockState(bottomLeft.above(y).relative(leftDir)).is(frameBlock)) return false;
                // Right column
                if (!level.getBlockState(bottomLeft.above(y).relative(rightDir, width)).is(frameBlock)) return false;
            }

            for (int x = 0; x < width; x++) {
                BlockPos xPos = bottomLeft.relative(rightDir, x);
                // Bottom row
                if (!level.getBlockState(xPos.below()).is(frameBlock)) return false;
                // Top row
                if (!level.getBlockState(xPos.above(height)).is(frameBlock)) return false;
            }

            // Check corners
            if (!level.getBlockState(bottomLeft.below().relative(leftDir)).is(frameBlock)) return false;
            if (!level.getBlockState(bottomLeft.below().relative(rightDir, width)).is(frameBlock)) return false;
            if (!level.getBlockState(bottomLeft.above(height).relative(leftDir)).is(frameBlock)) return false;
            if (!level.getBlockState(bottomLeft.above(height).relative(rightDir, width)).is(frameBlock)) return false;

            // Check interior is all air or portal
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    BlockPos p = bottomLeft.above(y).relative(rightDir, x);
                    BlockState state = level.getBlockState(p);
                    if (!state.isAir() && !state.is(portalBlock)) return false;
                }
            }

            return true;
        }

        public void createPortalBlocks() {
            Direction rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
            BlockState portalState = portalBlock.defaultBlockState();
            if (portalState.hasProperty(PortalBlock.AXIS)) {
                portalState = portalState.setValue(PortalBlock.AXIS, axis);
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    BlockPos p = bottomLeft.above(y).relative(rightDir, x);
                    level.setBlock(p, portalState, 18);
                }
            }
        }
    }
}
