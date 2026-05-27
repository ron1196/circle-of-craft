package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.CircleOfCraftMod;
import io.github.ron1196.circleofcraft.block.BugTrapBlock;
import io.github.ron1196.circleofcraft.entity.animal.BugEntity;
import io.github.ron1196.circleofcraft.menu.BugTrapMenu;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BugTrapBlockEntity extends BlockEntity implements MenuProvider {

    public static final int TRAP_INTERVAL = 600; // 30 seconds — bait→bug attract roll cadence

    public static final TagKey<Item> BAIT_PREFERRED = bait("preferred");
    public static final TagKey<Item> BAIT_DECENT = bait("decent");
    public static final TagKey<Item> BAIT_POOR = bait("poor");

    private static final float WEIGHT_PREFERRED = 1.0F;
    private static final float WEIGHT_DECENT = 0.5F;
    private static final float WEIGHT_POOR = 0.2F;
    private static final float ATTRACT_PER_WEIGHT = 0.25F;

    private static TagKey<Item> bait(String tier) {
        return TagKey.create(Registries.ITEM, CircleOfCraftMod.id("bait/" + tier));
    }

    private static final int SPAWN_RADIUS_XZ = 8;
    private static final int SPAWN_RADIUS_Y = 2;
    private static final int SPAWN_ATTEMPTS = 16;
    private static final double SCAN_RANGE = 2.0;
    private static final double FACE_CONSUME_DISTANCE = 1.0;
    private static final double FACE_CONSUME_DISTANCE_SQR = FACE_CONSUME_DISTANCE * FACE_CONSUME_DISTANCE;
    private static final int CONSUME_SOUND_STRIDE = 8;
    private static final int CLOSURE_HOLD_TICKS = 20;
    private static final int CLOSURE_TRIGGER_TICK = 4;
    private static final double CLOSURE_TRIGGER_DISTANCE_SQR = 0.6 * 0.6;

    private final ItemStackHandler items = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 4) return false; // output slot
            return baitWeight(stack) > 0.0F;
        }
    };

    private int trapTimer = 0;
    private int closureTimer = 0;

    public int getTicksUntilNextAttract() {
        if (!hasBait()) return -1;
        return Math.max(0, TRAP_INTERVAL - trapTimer);
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? trapTimer : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) trapTimer = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public BugTrapBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.BUG_TRAP.get(), pos, state);
    }

    public void serverTick() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        if (hasBait()) {
            trapTimer++;
            if (trapTimer >= TRAP_INTERVAL) {
                trapTimer = 0;
                tryAttractBug(serverLevel);
            }
        } else {
            trapTimer = 0;
        }

        consumeNearbyBugs(serverLevel);
        tickClosureTimer(serverLevel);
    }

    private void tickClosureTimer(@NotNull ServerLevel serverLevel) {
        if (closureTimer <= 0) return;
        closureTimer--;
        int newLevel = levelForTimer(closureTimer);
        if (closureTimer == 0) {
            setClosedFaceAndLevel(serverLevel, BugTrapBlock.ClosedFace.NONE, 0);
        } else {
            setClosureLevel(serverLevel, newLevel);
        }
    }

    private static int levelForTimer(int timer) {
        if (timer <= 0) return 0;
        if (timer <= OPEN_ANIM_TICKS) return 1;
        if (timer >= CLOSURE_HOLD_TICKS - CLOSE_ANIM_TICKS) return 1;
        return 2;
    }

    private static final int CLOSE_ANIM_TICKS = 2;
    private static final int OPEN_ANIM_TICKS = 2;

    private static BugTrapBlock.ClosedFace closedFaceFor(@NotNull Direction face) {
        return switch (face) {
            case NORTH -> BugTrapBlock.ClosedFace.NORTH;
            case EAST -> BugTrapBlock.ClosedFace.EAST;
            case SOUTH -> BugTrapBlock.ClosedFace.SOUTH;
            case WEST -> BugTrapBlock.ClosedFace.WEST;
            default -> BugTrapBlock.ClosedFace.NONE;
        };
    }

    private void setClosedFaceAndLevel(
            @NotNull ServerLevel serverLevel, BugTrapBlock.@NotNull ClosedFace face, int level) {
        BlockState current = getBlockState();
        if (current.getValue(BugTrapBlock.CLOSED_FACE) == face
                && current.getValue(BugTrapBlock.CLOSURE_LEVEL) == level) {
            return;
        }
        serverLevel.setBlock(
                worldPosition,
                current.setValue(BugTrapBlock.CLOSED_FACE, face).setValue(BugTrapBlock.CLOSURE_LEVEL, level),
                3);
    }

    private void setClosureLevel(@NotNull ServerLevel serverLevel, int level) {
        BlockState current = getBlockState();
        if (current.getValue(BugTrapBlock.CLOSURE_LEVEL) == level) return;
        serverLevel.setBlock(worldPosition, current.setValue(BugTrapBlock.CLOSURE_LEVEL, level), 3);
    }

    private boolean hasBait() {
        for (int i = 0; i < 4; i++) {
            if (!items.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    private float totalBaitWeight() {
        float total = 0.0F;
        for (int i = 0; i < 4; i++) {
            total += baitWeight(items.getStackInSlot(i));
        }
        return total;
    }

    private static float baitWeight(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return 0.0F;
        if (stack.is(BAIT_PREFERRED)) return WEIGHT_PREFERRED;
        if (stack.is(BAIT_DECENT)) return WEIGHT_DECENT;
        if (stack.is(BAIT_POOR)) return WEIGHT_POOR;
        return 0.0F;
    }

    private void tryAttractBug(@NotNull ServerLevel serverLevel) {
        float chance = ATTRACT_PER_WEIGHT * totalBaitWeight();
        if (serverLevel.random.nextFloat() >= chance) return;

        for (int attempts = 0; attempts < SPAWN_ATTEMPTS; attempts++) {
            int dx = serverLevel.random.nextInt(SPAWN_RADIUS_XZ * 2 + 1) - SPAWN_RADIUS_XZ;
            int dy = serverLevel.random.nextInt(SPAWN_RADIUS_Y * 2 + 1) - SPAWN_RADIUS_Y;
            int dz = serverLevel.random.nextInt(SPAWN_RADIUS_XZ * 2 + 1) - SPAWN_RADIUS_XZ;
            BlockPos spawnPos = worldPosition.offset(dx, dy, dz);
            if (!serverLevel.isEmptyBlock(spawnPos)) continue;
            BlockState below = serverLevel.getBlockState(spawnPos.below());
            if (!below.is(Blocks.GRASS_BLOCK) && !below.is(Blocks.DIRT)) continue;

            BugEntity bug = EntityTypes.BUG.get().create(serverLevel);
            if (bug == null) return;
            bug.moveTo(
                    spawnPos.getX() + 0.5,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5,
                    serverLevel.random.nextFloat() * 360.0F,
                    0.0F);
            bug.finalizeSpawn(
                    serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos), MobSpawnType.NATURAL, null, null);
            Direction face = chooseNearestBaitedFace(bug.position());
            if (face == null) return;
            bug.targetTrap = worldPosition.immutable();
            bug.targetFace = face;
            serverLevel.addFreshEntity(bug);
            return;
        }
    }

    @Nullable
    private Direction chooseNearestBaitedFace(@NotNull Vec3 from) {
        Direction best = null;
        double bestDistSqr = Double.MAX_VALUE;
        for (Direction face : Direction.Plane.HORIZONTAL) {
            int slot = BugEntity.slotForFace(face);
            if (slot < 0 || items.getStackInSlot(slot).isEmpty()) continue;
            BlockPos approach = worldPosition.relative(face);
            double d = from.distanceToSqr(approach.getX() + 0.5, approach.getY(), approach.getZ() + 0.5);
            if (d < bestDistSqr) {
                bestDistSqr = d;
                best = face;
            }
        }
        return best;
    }

    private void consumeNearbyBugs(@NotNull ServerLevel serverLevel) {
        AABB area = new AABB(worldPosition).inflate(SCAN_RANGE);
        List<BugEntity> nearby = serverLevel.getEntitiesOfClass(BugEntity.class, area);
        if (nearby.isEmpty()) return;

        Vec3 trapCenter = Vec3.atCenterOf(worldPosition);
        Vec3 trapBottomCenter = Vec3.atBottomCenterOf(worldPosition);
        for (BugEntity bug : nearby) {
            if (bug.targetTrap == null || !bug.targetTrap.equals(worldPosition)) continue;
            if (bug.targetFace == null) continue;
            int slot = BugEntity.slotForFace(bug.targetFace);
            if (slot < 0) continue;
            if (bug.position().distanceToSqr(trapBottomCenter) > FACE_CONSUME_DISTANCE_SQR) continue;

            if (bug.trapTick < 0) {
                if (items.getStackInSlot(slot).isEmpty()) continue;
                items.getStackInSlot(slot).shrink(1);
                bug.trapTick = 0;
                syncToClient();
            }

            bug.trapTick++;

            if (bug.trapTick % CONSUME_SOUND_STRIDE == 0 && bug.trapTick < 30) {
                serverLevel.playSound(
                        null,
                        worldPosition,
                        SoundEvents.SILVERFISH_HURT,
                        SoundSource.BLOCKS,
                        0.4F,
                        0.8F + serverLevel.random.nextFloat() * 0.4F);
            }

            if (bug.trapTick >= 34) {
                Vec3 toward = trapCenter.subtract(bug.position());
                double dist = toward.length();
                if (dist > 0.0) {
                    double pull = (1.0 - dist) * (1.0 - dist) * 0.06;
                    if (pull > 0.0) {
                        Vec3 motion = bug.getDeltaMovement().add(toward.scale(pull / dist));
                        bug.setDeltaMovement(motion);
                    }
                }
            }

            boolean atFace = bug.position().distanceToSqr(trapBottomCenter) <= CLOSURE_TRIGGER_DISTANCE_SQR;
            BlockState state = getBlockState();
            int curLevel = state.getValue(BugTrapBlock.CLOSURE_LEVEL);

            if (bug.trapTick >= CLOSURE_TRIGGER_TICK && atFace) {
                if (state.getValue(BugTrapBlock.CLOSED_FACE) == BugTrapBlock.ClosedFace.NONE) {
                    setClosedFaceAndLevel(serverLevel, closedFaceFor(bug.targetFace), 1);
                    closureTimer = CLOSURE_HOLD_TICKS;
                }
                if (curLevel >= 2) {
                    addBugToOutput();
                    bug.discard();
                    syncToClient();
                }
            } else if (bug.trapTick >= BugEntity.CONSUME_DURATION_TICKS) {
                addBugToOutput();
                bug.discard();
                syncToClient();
            }
        }
    }

    private void addBugToOutput() {
        ItemStack output = items.getStackInSlot(4);
        if (output.isEmpty()) {
            items.setStackInSlot(4, new ItemStack(ModItems.BUG.get()));
        } else if (output.is(ModItems.BUG.get()) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            setChanged();
        }
    }

    public ItemStackHandler getInventory() {
        return items;
    }

    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (!stack.isEmpty()) drops.add(stack);
        }
        return drops;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.putInt("TrapTimer", trapTimer);
        tag.putInt("ClosureTimer", closureTimer);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("Items"));
        trapTimer = tag.getInt("TrapTimer");
        closureTimer = tag.getInt("ClosureTimer");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("Items", items.serializeNBT());
        return tag;
    }

    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(
            @NotNull net.minecraft.network.Connection net,
            net.minecraft.network.protocol.game.@NotNull ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            load(tag);
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.circleofcraft.bug_trap");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInv, @NotNull Player player) {
        return new BugTrapMenu(containerId, playerInv, this, data);
    }
}
