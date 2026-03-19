package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.menu.GrindingBowlMenu;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import io.github.ron1196.thelionking.registry.Items;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import java.util.HashMap;
import java.util.Map;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrindingBowlBlockEntity extends BlockEntity implements MenuProvider {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int MAX_GRIND_TIME = 200;

  private static final Map<Item, Item> RECIPES = new HashMap<>();

  private static Map<Item, Item> getRecipes() {
    if (RECIPES.isEmpty()) {
      // Bone grinding
      RECIPES.put(Items.HYENA_BONE.get(), Items.HYENA_BONE_SHARD.get());
      RECIPES.put(net.minecraft.world.item.Items.BONE, net.minecraft.world.item.Items.BONE_MEAL);

      // Fruit/plant grinding
      RECIPES.put(Items.MANGO.get(), Items.MANGO_DUST.get());
      RECIPES.put(Items.RHINO_HORN.get(), Items.GROUND_RHINO_HORN.get());
      RECIPES.put(Items.NUKA_SHARD.get(), Items.POISON.get());
      RECIPES.put(Items.CORN.get(), Items.CORN_KERNELS.get());
      RECIPES.put(Items.DRIED_MAIZE.get(), Items.CORN_KERNELS.get());

      // Stone grinding
      RECIPES.put(Items.PRIDESTONE_ITEM.get(), net.minecraft.world.item.Items.SAND);
      RECIPES.put(Items.CORRUPT_PRIDESTONE_ITEM.get(), net.minecraft.world.item.Items.SAND);
      RECIPES.put(
          net.minecraft.world.item.Items.COBBLESTONE, net.minecraft.world.item.Items.GRAVEL);
      RECIPES.put(net.minecraft.world.item.Items.GRAVEL, net.minecraft.world.item.Items.SAND);

      // Feather → dye recipes
      RECIPES.put(Items.FEATHER_BLUE.get(), net.minecraft.world.item.Items.BLUE_DYE);
      RECIPES.put(Items.FEATHER_YELLOW.get(), net.minecraft.world.item.Items.YELLOW_DYE);
      RECIPES.put(Items.FEATHER_RED.get(), net.minecraft.world.item.Items.RED_DYE);
      RECIPES.put(Items.FEATHER_BLACK.get(), net.minecraft.world.item.Items.BLACK_DYE);
      RECIPES.put(Items.FEATHER_PINK.get(), net.minecraft.world.item.Items.PINK_DYE);

      // Flower → dye recipes
      RECIPES.put(
          LionKingBlocks.WHITE_FLOWER.get().asItem(), net.minecraft.world.item.Items.WHITE_DYE);
      RECIPES.put(
          LionKingBlocks.BLUE_FLOWER.get().asItem(), net.minecraft.world.item.Items.BLUE_DYE);
      RECIPES.put(
          LionKingBlocks.PURPLE_FLOWER.get().asItem(), net.minecraft.world.item.Items.PURPLE_DYE);
      RECIPES.put(LionKingBlocks.RED_FLOWER.get().asItem(), net.minecraft.world.item.Items.RED_DYE);

      // Leaf → dye recipes
      RECIPES.put(
          LionKingBlocks.ACACIA_LEAVES.get().asItem(), net.minecraft.world.item.Items.GREEN_DYE);
      RECIPES.put(
          LionKingBlocks.RAINFOREST_LEAVES.get().asItem(),
          net.minecraft.world.item.Items.GREEN_DYE);
      RECIPES.put(
          LionKingBlocks.MANGO_LEAVES.get().asItem(), net.minecraft.world.item.Items.GREEN_DYE);

      // Termite grinding
      RECIPES.put(Items.TERMITE_THROWN.get(), Items.TERMITE_DUST.get());

      // Wheat → flour (vanilla compatibility)
      RECIPES.put(net.minecraft.world.item.Items.WHEAT, net.minecraft.world.item.Items.BREAD);
    }
    return RECIPES;
  }

  private final ItemStackHandler inventory =
      new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
          setChanged();
        }
      };

  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> inventory);

  private int grindTime = 0;

  private final ContainerData data =
      new ContainerData() {
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
    return Component.translatable("container.thelionking.grinding_bowl");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(
      int containerId, @NotNull Inventory playerInv, @NotNull Player player) {
    return new GrindingBowlMenu(containerId, playerInv, inventory, data);
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(
      @NotNull Capability<T> cap, @Nullable Direction side) {
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
      GrindingBowlBlockEntity entity) {
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
          level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
    }
  }
}
