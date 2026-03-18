package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.Items;
import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OutlandsPoolBlockEntity extends BlockEntity {

    private final List<ItemStack> collectedItems = new ArrayList<>();
    private int processTimer = -1;
    private static final int PROCESS_DELAY = 75;

    public OutlandsPoolBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.OUTLANDS_POOL.get(), pos, state);
    }

    public void collectItem(ItemStack stack) {
        collectedItems.add(stack);
        if (processTimer < 0) processTimer = 0;
        setChanged();
    }

    public void serverTick() {
        if (level == null || collectedItems.isEmpty()) return;

        processTimer++;
        if (processTimer >= PROCESS_DELAY) {
            processRecipes();
            collectedItems.clear();
            processTimer = -1;
            setChanged();
        }
    }

    private void processRecipes() {
        if (level == null) return;
        BlockPos above = worldPosition.above();

        // Count ingredients
        int silver = 0, kivulite = 0;
        int featherBlue = 0, featherYellow = 0, featherRed = 0, featherBlack = 0;
        int rafikiCoins = 0;
        List<ItemStack> remaining = new ArrayList<>();

        for (ItemStack stack : collectedItems) {
            if (stack.is(Items.SILVER_INGOT.get())) silver += stack.getCount();
            else if (stack.is(Items.KIVULITE.get())) kivulite += stack.getCount();
            else if (stack.is(Items.FEATHER_BLUE.get())) featherBlue += stack.getCount();
            else if (stack.is(Items.FEATHER_YELLOW.get())) featherYellow += stack.getCount();
            else if (stack.is(Items.FEATHER_RED.get())) featherRed += stack.getCount();
            else if (stack.is(Items.FEATHER_BLACK.get())) featherBlack += stack.getCount();
            else if (stack.is(Items.RAFIKI_COIN.get())) rafikiCoins += stack.getCount();
            else remaining.add(stack);
        }

        // Recipe: 2 Silver + 5 Kivulite → Outlands Helmet
        int helmCount = Math.min(silver / 2, kivulite / 5);
        if (helmCount > 0) {
            spawnItem(above, new ItemStack(Items.OUTLANDS_HELMET.get(), helmCount));
            silver -= helmCount * 2;
            kivulite -= helmCount * 5;
        }

        // Recipe: 1 each of 4 feathers → Outlands Feather
        int featherCount = Math.min(Math.min(featherBlue, featherYellow), Math.min(featherRed, featherBlack));
        if (featherCount > 0) {
            spawnItem(above, new ItemStack(Items.WAYWARD_FEATHER.get(), featherCount));
            featherBlue -= featherCount;
            featherYellow -= featherCount;
            featherRed -= featherCount;
            featherBlack -= featherCount;
        }

        // Recipe: Rafiki Coin → Zira Coin (1:1)
        if (rafikiCoins > 0) {
            spawnItem(above, new ItemStack(Items.ZIRA_COIN.get(), rafikiCoins));
        }

        // Drop leftover ingredients
        if (silver > 0) spawnItem(above, new ItemStack(Items.SILVER_INGOT.get(), silver));
        if (kivulite > 0) spawnItem(above, new ItemStack(Items.KIVULITE.get(), kivulite));
        if (featherBlue > 0) spawnItem(above, new ItemStack(Items.FEATHER_BLUE.get(), featherBlue));
        if (featherYellow > 0) spawnItem(above, new ItemStack(Items.FEATHER_YELLOW.get(), featherYellow));
        if (featherRed > 0) spawnItem(above, new ItemStack(Items.FEATHER_RED.get(), featherRed));
        if (featherBlack > 0) spawnItem(above, new ItemStack(Items.FEATHER_BLACK.get(), featherBlack));
        for (ItemStack stack : remaining) {
            spawnItem(above, stack);
        }
    }

    private void spawnItem(BlockPos pos, ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        entity.setDeltaMovement(0.0, 0.3 + level.random.nextFloat() * 0.1, 0.0);
        entity.setPickUpDelay(10);
        level.addFreshEntity(entity);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list = new ListTag();
        for (ItemStack stack : collectedItems) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);
            list.add(itemTag);
        }
        tag.put("CollectedItems", list);
        tag.putInt("ProcessTimer", processTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        collectedItems.clear();
        ListTag list = tag.getList("CollectedItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = ItemStack.of(list.getCompound(i));
            if (!stack.isEmpty()) collectedItems.add(stack);
        }
        processTimer = tag.getInt("ProcessTimer");
    }
}
