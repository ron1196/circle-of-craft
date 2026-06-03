package io.github.ron1196.circleofcraft.block.entity;

import io.github.ron1196.circleofcraft.menu.GrindingBowlMenu;
import io.github.ron1196.circleofcraft.recipe.GrindingBowlRecipe;
import io.github.ron1196.circleofcraft.registry.BlockEntityTypes;
import io.github.ron1196.circleofcraft.registry.RecipeTypes;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrindingBowlBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int MAX_GRIND_TIME = 200;

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            syncToClient();
        }
    };

    private int grindTime = 0;
    private float stickRotation = 0;
    private float prevStickRotation = 0;
    private static final float STICK_ROTATION_SPEED = 8.0F;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> grindTime;
                case 1 -> MAX_GRIND_TIME;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                grindTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public GrindingBowlBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.GRINDING_BOWL.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.circleofcraft.grinding_bowl");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInv, @NotNull Player player) {
        return new GrindingBowlMenu(containerId, playerInv, inventory, data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("GrindTime", grindTime);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        grindTime = tag.getInt("GrindTime");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("GrindTime", grindTime);
        return tag;
    }

    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean isGrinding() {
        return grindTime > 0;
    }

    /** Returns 0.0 (just started) to 1.0 (about to finish). */
    public float getGrindProgress() {
        if (grindTime <= 0) return 0;
        return (float) grindTime / MAX_GRIND_TIME;
    }

    public float getStickRotation(float partialTick) {
        return prevStickRotation + (stickRotation - prevStickRotation) * partialTick;
    }

    public ItemStack getInputItem() {
        return inventory.getStackInSlot(SLOT_INPUT);
    }

    public static void clientTick(
            Level ignoredLevel, BlockPos ignoredPos, BlockState ignoredState, GrindingBowlBlockEntity entity) {
        entity.prevStickRotation = entity.stickRotation;
        if (entity.grindTime > 0) {
            entity.stickRotation += STICK_ROTATION_SPEED;
        }
    }

    public static void serverTick(
            Level level, BlockPos ignoredPos, BlockState ignoredState, GrindingBowlBlockEntity entity) {
        ItemStack input = entity.inventory.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) {
            entity.resetGrindTime();
            return;
        }

        Optional<GrindingBowlRecipe> recipe = findRecipe(level, input);
        if (recipe.isEmpty()) {
            entity.resetGrindTime();
            return;
        }

        ItemStack resultStack = recipe.get().getResult();
        ItemStack outputSlot = entity.inventory.getStackInSlot(SLOT_OUTPUT);
        if (!outputSlot.isEmpty()) {
            if (!outputSlot.is(resultStack.getItem()) || outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
                entity.resetGrindTime();
                return;
            }
        }

        entity.grindTime++;
        entity.setChanged();
        if (entity.grindTime == 1 || entity.grindTime % 20 == 0) {
            entity.syncToClient();
        }
        if (entity.grindTime < MAX_GRIND_TIME) {
            return;
        }

        entity.inventory.extractItem(SLOT_INPUT, 1, false);
        if (outputSlot.isEmpty()) {
            entity.inventory.setStackInSlot(SLOT_OUTPUT, resultStack.copy());
        } else {
            outputSlot.grow(1);
        }

        entity.resetGrindTime();
    }

    private static Optional<GrindingBowlRecipe> findRecipe(Level level, ItemStack input) {
        SimpleContainer container = new SimpleContainer(input);
        return level.getRecipeManager().getRecipeFor(RecipeTypes.GRINDING_TYPE.get(), container, level);
    }

    private void resetGrindTime() {
        if (grindTime == 0) return;
        grindTime = 0;
        setChanged();
        syncToClient();
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void drops() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
        }
    }
}
