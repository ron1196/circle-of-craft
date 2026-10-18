package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.data.PlayerData;
import io.github.ron1196.circleofcraft.entity.ai.SimbaAttackGoal;
import io.github.ron1196.circleofcraft.entity.ai.SimbaFishingGoal;
import io.github.ron1196.circleofcraft.entity.ai.SimbaWanderGoal;
import io.github.ron1196.circleofcraft.item.AstralCharmItem;
import io.github.ron1196.circleofcraft.menu.SimbaInventoryMenu;
import io.github.ron1196.circleofcraft.network.PlayerDataSyncPacket;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.registry.ModSoundEvents;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SimbaEntity extends TamableAnimal implements GeoEntity {

    public static final String REGISTRY_NAME = "simba";

    /** Three in-game days from cub to adult; feeding meat takes 10% off the remaining time each time. */
    public static final int GROW_UP_TICKS = 72000;

    private static final EntityDataAccessor<Float> DATA_GROWTH =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.FLOAT);
    private static final float CUB_START_SCALE = 0.5F;
    private static final float CUB_END_SCALE = 0.7F;
    private static final EntityDataAccessor<Boolean> DATA_HAS_CHARM =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.BOOLEAN);

    // Clip ground speeds at render scale (adult / cub): walk 0.39 / 0.25 blocks/s, run 2.17 / 1.67 blocks/s.
    // Walk plays at 2x and wandering is slowed to match; run is sped up to the 3.6 blocks/s follow speed.
    // AI ground speed is about 57.3 * (attribute * modifier)^2 blocks/s.
    private static final double WALK_ANIMATION_RATE = 2.0;
    private static final double ADULT_RUN_ANIMATION_RATE = 1.65;
    private static final double CUB_RUN_ANIMATION_RATE = 2.14;
    private static final double ADULT_STROLL_SPEED_MODIFIER = 0.47;
    private static final double CUB_STROLL_SPEED_MODIFIER = 0.37;
    private static final double RUN_THRESHOLD_BLOCKS_PER_TICK = 0.075;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public final ItemStackHandler inventory = new ItemStackHandler(9);

    public SimbaEntity(EntityType<? extends SimbaEntity> type, Level level) {
        super(type, level);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Simba"));
        this.setCustomNameVisible(true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        this.setAge(-GROW_UP_TICKS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GROWTH, 0.0F);
        builder.define(DATA_HAS_CHARM, false);
    }

    public boolean hasCharm() {
        return this.entityData.get(DATA_HAS_CHARM);
    }

    public void setHasCharm(boolean hasCharm) {
        this.entityData.set(DATA_HAS_CHARM, hasCharm);
    }

    @Override
    public boolean canChangeDimensions(@NotNull Level from, @NotNull Level to) {
        return hasCharm();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new SimbaAttackGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new SimbaFishingGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new SimbaWanderGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void setBaby(boolean baby) {
        setAge(baby ? -GROW_UP_TICKS : 0);
    }

    /** 0 for a newborn cub, rising to 1 as the cub reaches adulthood; synced because clients never see the age. */
    public float growth() {
        return isBaby() ? entityData.get(DATA_GROWTH) : 1.0F;
    }

    private void updateGrowth() {
        float growth = isBaby() ? Math.max(0.0F, 1.0F - (float) -getAge() / GROW_UP_TICKS) : 1.0F;
        if (Math.abs(growth - entityData.get(DATA_GROWTH)) > 0.005F) {
            entityData.set(DATA_GROWTH, growth);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && tickCount % 20 == 0) {
            updateGrowth();
        }
    }

    @Override
    public float getAgeScale() {
        return isBaby() ? CUB_START_SCALE + (CUB_END_SCALE - CUB_START_SCALE) * growth() : 1.0F;
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_GROWTH.equals(key)) {
            refreshDimensions();
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (!level().isClientSide() && !isBaby()) {
            playSound(ModSoundEvents.LION_ROAR.get(), 1.0F, 1.0F);
            if (getOwner() instanceof Player owner) {
                owner.sendSystemMessage(Component.literal("Simba has grown up!"));
            }
        }
    }

    public double strollSpeedModifier() {
        return isBaby() ? CUB_STROLL_SPEED_MODIFIER : ADULT_STROLL_SPEED_MODIFIER;
    }

    @Override
    public void registerControllers(@NotNull AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, state -> {
            String prefix = clipPrefix();
            double dx = getX() - xo;
            double dz = getZ() - zo;
            double speed = Math.sqrt(dx * dx + dz * dz);
            if (isInSittingPose()) {
                state.getController().setAnimationSpeed(1.0);
                return state.setAndContinue(RawAnimation.begin().thenLoop(prefix + "sit"));
            }
            if (speed > RUN_THRESHOLD_BLOCKS_PER_TICK) {
                state.getController().setAnimationSpeed(isBaby() ? CUB_RUN_ANIMATION_RATE : ADULT_RUN_ANIMATION_RATE);
                return state.setAndContinue(RawAnimation.begin().thenLoop(prefix + "run"));
            }
            if (speed > 1.0E-3) {
                state.getController().setAnimationSpeed(WALK_ANIMATION_RATE);
                return state.setAndContinue(RawAnimation.begin().thenLoop(prefix + "walk"));
            }
            state.getController().setAnimationSpeed(1.0);
            return state.setAndContinue(RawAnimation.begin().thenLoop(prefix + "idle"));
        }));
        controllers.add(new AnimationController<>(this, "attack", 2, state -> PlayState.STOP)
                .triggerableAnim("adult", playOnce("animation.simba_adult.attack"))
                .triggerableAnim("cub", playOnce("animation.simba_cub.attack")));
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        triggerAnim("attack", isBaby() ? "cub" : "adult");
        return super.doHurtTarget(target);
    }

    private static RawAnimation playOnce(String animation) {
        return RawAnimation.begin().then(animation, Animation.LoopType.PLAY_ONCE);
    }

    private String clipPrefix() {
        return isBaby() ? "animation.simba_cub." : "animation.simba_adult.";
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return null;
    }

    /** Toggles sitting state and notifies the player. Used by both mob interaction and keybind. */
    public void toggleSitting(Player player) {
        setOrderedToSit(!isOrderedToSit());
        this.navigation.stop();
        String msg = isOrderedToSit() ? "Simba sits down." : "Simba stands up and follows you.";
        player.sendSystemMessage(Component.literal(msg));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (level().isClientSide()) return InteractionResult.SUCCESS;

        if (!isTame()) {
            tame(player);
            player.sendSystemMessage(Component.literal("Simba is now following you!"));
            return InteractionResult.SUCCESS;
        }

        if (!isOwnedBy(player)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);
        if (held.is(ModItems.ASTRAL_CHARM.get()) && AstralCharmItem.isActive(held) && !hasCharm()) {
            if (!level().isClientSide()) {
                held.shrink(1);
                setHasCharm(true);
                ChatHelper.sendNpcMessage(player, "Simba", "*accepts the charm and roars proudly*");
            }
            return InteractionResult.SUCCESS;
        }

        // Sneak+interact opens Simba's inventory
        if (player.isShiftKeyDown() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("container.circleofcraft.simba_inventory");
                }

                @Override
                public @NotNull AbstractContainerMenu createMenu(
                        int containerId, @NotNull Inventory inv, @NotNull Player p) {
                    return new SimbaInventoryMenu(containerId, inv, inventory);
                }
            });
            return InteractionResult.SUCCESS;
        }

        if (isBaby() && held.is(ItemTags.MEAT)) {
            held.shrink(1);
            ageUp(getSpeedUpSecondsWhenFeeding(-getAge()), true);
            return InteractionResult.SUCCESS;
        }

        // Toggle sitting
        toggleSitting(player);
        return InteractionResult.SUCCESS;
    }

    /**
     * Authoritative check for "this player already has a Simba" — searches loaded levels rather
     * than trusting the persisted {@code hasSimba} flag, which can desync (missed death, old save).
     * A Simba sitting in an unloaded chunk won't be found, trading a rare duplicate for never being
     * locked out of re-summoning.
     */
    public static boolean playerHasLivingSimba(@NotNull ServerPlayer player) {
        UUID id = player.getUUID();
        MinecraftServer server = player.getServer();
        if (server == null) return false;
        for (ServerLevel level : server.getAllLevels()) {
            boolean found = !level.getEntities(
                            EntityTypeTest.forClass(SimbaEntity.class),
                            simba -> simba.isAlive() && id.equals(simba.getOwnerUUID()))
                    .isEmpty();
            if (found) return true;
        }
        return false;
    }

    @Override
    public @Nullable Entity changeDimension(@NotNull DimensionTransition transition) {
        Entity result = super.changeDimension(transition);
        if (result != null && getOwner() instanceof ServerPlayer owner) {
            ModCriteriaTriggers.TELEPORT_SIMBA.trigger(owner);
        }
        return result;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);
        if (level().isClientSide()) return;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            spawnAtLocation(stack);
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        if (hasCharm()) {
            spawnAtLocation(AstralCharmItem.createActive());
        }
        if (getOwner() instanceof ServerPlayer owner) {
            PlayerData data = PlayerData.get(owner);
            data.setHasSimba(false);
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(owner, PlayerDataSyncPacket.of(data));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasCharm", hasCharm());
        tag.put("Inventory", inventory.serializeNBT(this.registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Saves made before growing up existed store a Baby flag instead of an age; a cub restarts its growth.
        // Without either tag (e.g. /summon) the constructor's cub age stands.
        if (tag.contains("Baby")) {
            setBaby(tag.getBoolean("Baby"));
        } else if (!tag.contains("Age")) {
            setBaby(true);
        }
        updateGrowth();
        setHasCharm(tag.getBoolean("HasCharm"));
        if (tag.contains("Inventory")) inventory.deserializeNBT(this.registryAccess(), tag.getCompound("Inventory"));
    }
}
