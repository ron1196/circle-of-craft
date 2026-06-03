package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.block.MountedShooterBlock;
import io.github.ron1196.circleofcraft.entity.projectile.DartEntity;
import io.github.ron1196.circleofcraft.entity.projectile.DartEntity.DartType;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MountedShooterBlockEntity extends BlockEntity {

    private static final int FIRE_ANIMATION_TICKS = 8;
    private static final int AUTO_FIRE_COOLDOWN = 40;
    private static final double AUTO_TARGET_RANGE = 8.0;
    private static final double LINE_WIDTH = 1.5;

    private ItemStack dartStack = ItemStack.EMPTY;
    private int fireCounter = 0;
    private FireMode fireMode = FireMode.REDSTONE;
    private int autoCooldown = 0;

    public enum FireMode {
        MANUAL("Manual — right-click to fire"),
        REDSTONE("Redstone — fires on signal"),
        AUTO("Auto — targets hostile mobs");

        private final String description;

        FireMode(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public FireMode next() {
            FireMode[] values = values();
            return values[(ordinal() + 1) % values.length];
        }

        public static FireMode byOrdinal(int ordinal) {
            FireMode[] values = values();
            return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : REDSTONE;
        }
    }

    public MountedShooterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.MOUNTED_SHOOTER.get(), pos, state);
    }

    public static void clientTick(MountedShooterBlockEntity be) {
        if (be.fireCounter > 0) {
            be.fireCounter--;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MountedShooterBlockEntity be) {
        if (be.fireCounter > 0) {
            be.fireCounter--;
        }
        if (be.autoCooldown > 0) {
            be.autoCooldown--;
        }

        if (be.fireMode == FireMode.AUTO && be.hasDarts() && be.autoCooldown <= 0) {
            be.tryAutoFire(level, pos, state);
        }
    }

    private void tryAutoFire(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(MountedShooterBlock.FACING);
        AABB searchBox = buildFrontLine(pos, facing);
        List<Monster> targets = level.getEntitiesOfClass(Monster.class, searchBox, Monster::isAlive);
        if (targets.isEmpty()) return;

        fireInDirection(level, pos, facing);
        autoCooldown = AUTO_FIRE_COOLDOWN;
    }

    private static AABB buildFrontLine(BlockPos pos, Direction facing) {
        Vec3 center = Vec3.atCenterOf(pos);
        double range = AUTO_TARGET_RANGE;
        return switch (facing) {
            case NORTH -> new AABB(
                    center.x - LINE_WIDTH,
                    center.y - LINE_WIDTH,
                    center.z - range,
                    center.x + LINE_WIDTH,
                    center.y + LINE_WIDTH,
                    center.z);
            case SOUTH -> new AABB(
                    center.x - LINE_WIDTH,
                    center.y - LINE_WIDTH,
                    center.z,
                    center.x + LINE_WIDTH,
                    center.y + LINE_WIDTH,
                    center.z + range);
            case WEST -> new AABB(
                    center.x - range,
                    center.y - LINE_WIDTH,
                    center.z - LINE_WIDTH,
                    center.x,
                    center.y + LINE_WIDTH,
                    center.z + LINE_WIDTH);
            case EAST -> new AABB(
                    center.x,
                    center.y - LINE_WIDTH,
                    center.z - LINE_WIDTH,
                    center.x + range,
                    center.y + LINE_WIDTH,
                    center.z + LINE_WIDTH);
            default -> new AABB(pos).inflate(range);
        };
    }

    public void fireInDirection(Level level, BlockPos pos, Direction facing) {
        if (!hasDarts()) return;
        DartType dartType = getDartTypeFromItem(dartStack);
        if (dartType == null) return;

        double x = pos.getX() + 0.5 + facing.getStepX() * 0.7;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5 + facing.getStepZ() * 0.7;

        DartEntity dart = new DartEntity(EntityTypes.DART.get(), level);
        dart.setPos(x, y, z);
        dart.setDartType(dartType);
        dart.setBaseDamage(dartType.getDamage());
        dart.shoot(facing.getStepX(), 0.05, facing.getStepZ(), 2.0F, 1.0F);
        level.addFreshEntity(dart);

        level.playSound(
                null,
                pos,
                SoundEvents.ARROW_SHOOT,
                SoundSource.BLOCKS,
                1.0F,
                1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + 0.25F);

        consumeOneDart();
        onFired();
    }

    // ── Dart storage ──────────────────────────────────────────────────────

    public boolean hasDarts() {
        return !dartStack.isEmpty();
    }

    public ItemStack getDartStack() {
        return dartStack;
    }

    public void loadDarts(ItemStack stack) {
        dartStack = stack.copy();
        setChanged();
        syncToClient();
    }

    public ItemStack unloadDarts() {
        ItemStack result = dartStack.copy();
        dartStack = ItemStack.EMPTY;
        setChanged();
        syncToClient();
        return result;
    }

    public void consumeOneDart() {
        dartStack.shrink(1);
        if (dartStack.isEmpty()) {
            dartStack = ItemStack.EMPTY;
        }
        setChanged();
        syncToClient();
    }

    // ── Fire mode ──────────────────────────────────────────────────────────

    public FireMode getFireMode() {
        return fireMode;
    }

    public FireMode cycleFireMode() {
        fireMode = fireMode.next();
        setChanged();
        syncToClient();
        return fireMode;
    }

    // ── Animation ─────────────────────────────────────────────────────────

    public void onFired() {
        fireCounter = FIRE_ANIMATION_TICKS;
        setChanged();
        syncToClient();
    }

    public float getFireCounter() {
        return fireCounter;
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    public static boolean isDartItem(Item item) {
        return item == ModItems.DART_BLUE.get()
                || item == ModItems.DART_RED.get()
                || item == ModItems.DART_YELLOW.get()
                || item == ModItems.DART_PINK.get()
                || item == ModItems.DART_BLACK.get();
    }

    private static DartType getDartTypeFromItem(ItemStack stack) {
        if (stack.is(ModItems.DART_BLUE.get())) return DartType.BLUE;
        if (stack.is(ModItems.DART_RED.get())) return DartType.RED;
        if (stack.is(ModItems.DART_YELLOW.get())) return DartType.YELLOW;
        if (stack.is(ModItems.DART_PINK.get())) return DartType.PINK;
        if (stack.is(ModItems.DART_BLACK.get())) return DartType.BLACK;
        return null;
    }

    private void syncToClient() {
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    // ── NBT ────────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("FireCounter", fireCounter);
        tag.putInt("FireMode", fireMode.ordinal());
        if (!dartStack.isEmpty()) {
            tag.put("DartStack", dartStack.save(registries));
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fireCounter = tag.getInt("FireCounter");
        fireMode = FireMode.byOrdinal(tag.getInt("FireMode"));
        dartStack = tag.contains("DartStack")
                ? ItemStack.parseOptional(registries, tag.getCompound("DartStack"))
                : ItemStack.EMPTY;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
