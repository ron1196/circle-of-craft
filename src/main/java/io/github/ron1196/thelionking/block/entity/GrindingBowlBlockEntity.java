package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.menu.GrindingBowlMenu;
import io.github.ron1196.thelionking.registry.LKBlockEntityTypes;
import io.github.ron1196.thelionking.registry.LKBlocks;
import io.github.ron1196.thelionking.registry.LKItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GrindingBowlBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int MAX_GRIND_TIME = 200;

    private static final Map<Item, Item> RECIPES = new HashMap<>();

    private static Map<Item, Item> getRecipes() {
        if (RECIPES.isEmpty()) {
            // Bone grinding
            RECIPES.put(LKItems.HYENA_BONE.get(), LKItems.HYENA_BONE_SHARD.get());
            RECIPES.put(Items.BONE, Items.BONE_MEAL);

            // Fruit/plant grinding
            RECIPES.put(LKItems.MANGO.get(), LKItems.MANGO_DUST.get());
            RECIPES.put(LKItems.RHINO_HORN.get(), LKItems.GROUND_RHINO_HORN.get());
            RECIPES.put(LKItems.NUKA_SHARD.get(), LKItems.POISON.get());
            RECIPES.put(LKItems.CORN.get(), LKItems.CORN_KERNELS.get());
            RECIPES.put(LKItems.DRIED_MAIZE.get(), LKItems.CORN_KERNELS.get());

            // Stone grinding
            RECIPES.put(LKItems.PRIDESTONE_ITEM.get(), Items.SAND);
            RECIPES.put(LKItems.CORRUPT_PRIDESTONE_ITEM.get(), Items.SAND);
            RECIPES.put(Items.COBBLESTONE, Items.GRAVEL);
            RECIPES.put(Items.GRAVEL, Items.SAND);

            // Feather → dye recipes
            RECIPES.put(LKItems.FEATHER_BLUE.get(), Items.BLUE_DYE);
            RECIPES.put(LKItems.FEATHER_YELLOW.get(), Items.YELLOW_DYE);
            RECIPES.put(LKItems.FEATHER_RED.get(), Items.RED_DYE);
            RECIPES.put(LKItems.FEATHER_BLACK.get(), Items.BLACK_DYE);
            RECIPES.put(LKItems.FEATHER_PINK.get(), Items.PINK_DYE);

            // Flower → dye recipes
            RECIPES.put(LKBlocks.WHITE_FLOWER.get().asItem(), Items.WHITE_DYE);
            RECIPES.put(LKBlocks.BLUE_FLOWER.get().asItem(), Items.BLUE_DYE);
            RECIPES.put(LKBlocks.PURPLE_FLOWER.get().asItem(), Items.PURPLE_DYE);
            RECIPES.put(LKBlocks.RED_FLOWER.get().asItem(), Items.RED_DYE);

            // Leaf → dye recipes
            RECIPES.put(LKBlocks.ACACIA_LEAVES.get().asItem(), Items.GREEN_DYE);
            RECIPES.put(LKBlocks.RAINFOREST_LEAVES.get().asItem(), Items.GREEN_DYE);
            RECIPES.put(LKBlocks.MANGO_LEAVES.get().asItem(), Items.GREEN_DYE);

            // Termite grinding
            RECIPES.put(LKItems.BUG.get(), LKItems.TERMITE_DUST.get());

            // Wheat → flour (vanilla compatibility)
            RECIPES.put(Items.WHEAT, Items.BREAD);
        }
        return RECIPES;
    }

    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> inventory);

    private int grindTime = 0;

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
        super(LKBlockEntityTypes.GRINDING_BOWL.get(), pos, state);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.thelionking.grinding_bowl");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInv, @NotNull Player player) {
        return new GrindingBowlMenu(containerId, playerInv, inventory, data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCap.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("GrindTime", grindTime);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        grindTime = tag.getInt("GrindTime");
    }

    public static void serverTick(
            Level ignoredLevel,
            BlockPos ignoredPos,
            BlockState ignoredState,
            GrindingBowlBlockEntity entity
    ) {
        ItemStack input = entity.inventory.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) {
            entity.resetGrindTime();
            return;
        }

        Item result = getRecipes().get(input.getItem());
        if (result == null) {
            entity.resetGrindTime();
            return;
        }

        ItemStack outputSlot = entity.inventory.getStackInSlot(SLOT_OUTPUT);
        if (!outputSlot.isEmpty()) {
            if (!outputSlot.is(result) || outputSlot.getCount() >= outputSlot.getMaxStackSize()) {
                entity.resetGrindTime();
                return;
            }
        }

        entity.grindTime++;
        entity.setChanged();
        if (entity.grindTime < MAX_GRIND_TIME) {
            return;
        }

        entity.inventory.extractItem(SLOT_INPUT, 1, false);
        if (outputSlot.isEmpty()) {
            entity.inventory.setStackInSlot(SLOT_OUTPUT, new ItemStack(result, 1));
        } else {
            outputSlot.grow(1);
        }

        entity.grindTime = 0;
        entity.setChanged();
    }

    private void resetGrindTime() {
        if (grindTime == 0) {
            return;
        }
        grindTime = 0;
        setChanged();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void drops() {
        if (level == null) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            Containers.dropItemStack(
                    level,
                    worldPosition.getX(),
                    worldPosition.getY(),
                    worldPosition.getZ(),
                    stack
            );
        }
    }
}
