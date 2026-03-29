package io.github.ron1196.thelionking.block.entity;

import io.github.ron1196.thelionking.registry.BlockEntityTypes;
import io.github.ron1196.thelionking.registry.LionKingBlocks;
import io.github.ron1196.thelionking.registry.LionKingItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Outwater Pool block entity — collects thrown items and processes recipes.
 * <p>
 * All pool blocks in the same mound delegate to one "master" pool (the one nearest
 * to the Outlands Altar). Only the master holds items and processes recipes.
 * <p>
 * Recipes: 5 kivulite + 2 silver → Outlandish Helm,
 *          4 colored feathers (1 each) → Wayward Feather,
 *          Rafiki Coin → Zira Coin.
 */
public class OutlandsPoolBlockEntity extends BlockEntity {

    private static final int PROCESS_DELAY_COMPLETE = 40;
    private static final int PROCESS_DELAY_WAITING = 75;
    private static final int SEARCH_RADIUS = 15;
    private static final int SEARCH_HEIGHT = 10;

    private final List<ItemStack> items = new ArrayList<>();
    private int timer = -1;

    public OutlandsPoolBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.OUTLANDS_POOL.get(), pos, state);
    }

    /**
     * Called by any pool block when an item falls in.
     * Routes to the master pool if this isn't the master.
     */
    public void collectItem(ItemStack stack) {
        OutlandsPoolBlockEntity master = findMaster();
        if (master != null && master != this) {
            master.collectItem(stack);
            return;
        }

        items.add(stack);
        timer = hasCompleteRecipe() ? PROCESS_DELAY_COMPLETE : PROCESS_DELAY_WAITING;
        setChanged();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.2,
                    worldPosition.getZ() + 0.5,
                    8,
                    0.3,
                    0.4,
                    0.3,
                    0.02);
            serverLevel.sendParticles(
                    ParticleTypes.BUBBLE_POP,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1.0,
                    worldPosition.getZ() + 0.5,
                    5,
                    0.2,
                    0.2,
                    0.2,
                    0.05);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean isProcessing() {
        return timer > 0;
    }

    public void serverTick() {
        if (level == null) return;

        // All pool blocks spawn particles when the master is processing
        OutlandsPoolBlockEntity master = findMaster();
        if (master != null && master.isProcessing()) {
            spawnProcessingParticles();
        }

        // Only the master processes recipes
        if (items.isEmpty()) return;

        if (timer > 0) {
            timer--;
            if (timer % 20 == 0) setChanged();
        }

        if (timer == 0) {
            processRecipes();
            items.clear();
            timer = -1;
            setChanged();
        }
    }

    // ── Master pool resolution ──────────────────────────────────────────────

    /**
     * Find the master pool — the pool block directly below the altar.
     * Returns this if no altar/pool is found (standalone pool).
     */
    private OutlandsPoolBlockEntity findMaster() {
        if (level == null) return this;

        BlockPos altarPos = findAltar();
        if (altarPos == null) return this;

        for (int dy = 1; dy <= SEARCH_HEIGHT; dy++) {
            BlockPos below = altarPos.below(dy);
            if (level.getBlockEntity(below) instanceof OutlandsPoolBlockEntity pool) {
                return pool;
            }
        }

        return this;
    }

    @Nullable
    private BlockPos findAltar() {
        if (level == null) return null;

        for (int dy = 0; dy < SEARCH_HEIGHT; dy++) {
            for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
                for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                    BlockPos check = worldPosition.offset(dx, dy, dz);
                    if (level.getBlockState(check).is(LionKingBlocks.OUTLANDS_ALTAR.get())) {
                        return check;
                    }
                }
            }
        }

        return null;
    }

    // ── Particles ───────────────────────────────────────────────────────────

    private void spawnProcessingParticles() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY();
        double z = worldPosition.getZ() + 0.5;

        int maxTimer = hasCompleteRecipe() ? PROCESS_DELAY_COMPLETE : PROCESS_DELAY_WAITING;
        float progress = 1.0F - (float) timer / maxTimer;
        int bubbleFrequency = Math.max(1, (int) (6 - progress * 5));

        if (level.random.nextInt(bubbleFrequency) == 0) {
            double bx = x + (level.random.nextFloat() - 0.5) * 0.8;
            double bz = z + (level.random.nextFloat() - 0.5) * 0.8;
            serverLevel.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, bx, y + 0.9, bz, 1, 0, 0.1, 0, 0.02);
        }

        if (level.random.nextInt(Math.max(1, 4 - (int) (progress * 3))) == 0) {
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    x + (level.random.nextFloat() - 0.5) * 0.6,
                    y + 0.8 + progress * 0.4,
                    z + (level.random.nextFloat() - 0.5) * 0.6,
                    1,
                    0,
                    0.05,
                    0,
                    0.02);
        }

        if (timer <= 5) {
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y + 1.0, z, 2, 0.2, 0.3, 0.2, 0.01);
        }

        if (level.random.nextInt(60) == 0) {
            level.playSound(
                    null,
                    worldPosition,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    0.7F,
                    1.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
        }
    }

    // ── Recipe logic ────────────────────────────────────────────────────────

    private boolean hasCompleteRecipe() {
        int silver = 0, kivulite = 0;
        int fBlue = 0, fYellow = 0, fRed = 0, fBlack = 0;
        int coins = 0;

        for (ItemStack stack : items) {
            if (stack.is(LionKingItems.SILVER_INGOT.get())) silver += stack.getCount();
            else if (stack.is(LionKingItems.KIVULITE.get())) kivulite += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_BLUE.get())) fBlue += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_YELLOW.get())) fYellow += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_RED.get())) fRed += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_BLACK.get())) fBlack += stack.getCount();
            else if (stack.is(LionKingItems.RAFIKI_COIN.get())) coins += stack.getCount();
        }

        if (silver >= 2 && kivulite >= 5) return true;
        if (fBlue >= 1 && fYellow >= 1 && fRed >= 1 && fBlack >= 1) return true;
        return coins >= 1;
    }

    private void processRecipes() {
        if (level == null) return;

        BlockPos altarPos = findAltar();
        BlockPos spawnPos = altarPos != null ? altarPos.above() : worldPosition.above(2);

        int silver = 0, kivulite = 0;
        int featherBlue = 0, featherY = 0, featherRed = 0, featherBlack = 0;
        int rafikiCoins = 0;
        List<ItemStack> remaining = new ArrayList<>();

        for (ItemStack stack : items) {
            if (stack.is(LionKingItems.SILVER_INGOT.get())) silver += stack.getCount();
            else if (stack.is(LionKingItems.KIVULITE.get())) kivulite += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_BLUE.get())) featherBlue += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_YELLOW.get())) featherY += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_RED.get())) featherRed += stack.getCount();
            else if (stack.is(LionKingItems.FEATHER_BLACK.get())) featherBlack += stack.getCount();
            else if (stack.is(LionKingItems.RAFIKI_COIN.get())) rafikiCoins += stack.getCount();
            else remaining.add(stack);
        }

        boolean crafted = false;

        int helmCount = Math.min(silver / 2, kivulite / 5);
        if (helmCount > 0) {
            spawnResult(spawnPos, new ItemStack(LionKingItems.OUTLANDS_HELMET.get(), helmCount));
            silver -= helmCount * 2;
            kivulite -= helmCount * 5;
            crafted = true;
        }

        int featherCount = Math.min(Math.min(featherBlue, featherY), Math.min(featherRed, featherBlack));
        if (featherCount > 0) {
            spawnResult(spawnPos, new ItemStack(LionKingItems.WAYWARD_FEATHER.get(), featherCount));
            featherBlue -= featherCount;
            featherY -= featherCount;
            featherRed -= featherCount;
            featherBlack -= featherCount;
            crafted = true;
        }

        if (rafikiCoins > 0) {
            spawnResult(spawnPos, new ItemStack(LionKingItems.ZIRA_COIN.get(), rafikiCoins));
            crafted = true;
        }

        if (crafted) {
            level.playSound(
                    null,
                    spawnPos,
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    1.5F,
                    0.5F + level.random.nextFloat() * 0.2F);
        }

        // Spit back leftovers at the altar
        if (silver > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.SILVER_INGOT.get(), silver));
        if (kivulite > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.KIVULITE.get(), kivulite));
        if (featherBlue > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.FEATHER_BLUE.get(), featherBlue));
        if (featherY > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.FEATHER_YELLOW.get(), featherY));
        if (featherRed > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.FEATHER_RED.get(), featherRed));
        if (featherBlack > 0) spawnLeftover(spawnPos, new ItemStack(LionKingItems.FEATHER_BLACK.get(), featherBlack));
        for (ItemStack stack : remaining) {
            spawnLeftover(spawnPos, stack);
        }
    }

    // ── Item spawning ───────────────────────────────────────────────────────

    private void spawnResult(BlockPos pos, ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        entity.setDeltaMovement(0.0, 0.4 + level.random.nextFloat() * 0.1, 0.0);
        entity.setPickUpDelay(10);
        level.addFreshEntity(entity);
    }

    private void spawnLeftover(BlockPos pos, ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        double offsetX = (level.random.nextBoolean() ? 1 : -1) * 0.5;
        double offsetZ = (level.random.nextBoolean() ? 1 : -1) * 0.5;
        Vec3 spawnItem = new Vec3(pos.getX() + 0.5 + offsetX, pos.getY(), pos.getZ() + 0.5 + offsetZ);

        ItemEntity entity = new ItemEntity(level, spawnItem.x, spawnItem.y, spawnItem.z, stack);
        entity.setDeltaMovement(0.0, 0.3 + level.random.nextFloat() * 0.1, 0.0);
        entity.setPickUpDelay(10);
        level.addFreshEntity(entity);
    }

    // ── NBT ─────────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list = new ListTag();
        for (ItemStack stack : items) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);
            list.add(itemTag);
        }
        tag.put("CollectedItems", list);
        tag.putInt("Timer", timer);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        items.clear();
        ListTag list = tag.getList("CollectedItems", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = ItemStack.of(list.getCompound(i));
            if (!stack.isEmpty()) items.add(stack);
        }
        timer = tag.getInt("Timer");
    }
}
