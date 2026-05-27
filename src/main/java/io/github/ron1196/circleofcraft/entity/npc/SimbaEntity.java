package io.github.ron1196.circleofcraft.entity.npc;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.entity.ai.SimbaAttackGoal;
import io.github.ron1196.circleofcraft.entity.ai.SimbaFishingGoal;
import io.github.ron1196.circleofcraft.entity.ai.SimbaWanderGoal;
import io.github.ron1196.circleofcraft.item.SimbaCharmItem;
import io.github.ron1196.circleofcraft.menu.SimbaInventoryMenu;
import io.github.ron1196.circleofcraft.registry.ModItems;
import io.github.ron1196.circleofcraft.util.ChatHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimbaEntity extends TamableAnimal {

    public static final String REGISTRY_NAME = "simba";

    private static final EntityDataAccessor<Boolean> DATA_BABY =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HAS_CHARM =
            SynchedEntityData.defineId(SimbaEntity.class, EntityDataSerializers.BOOLEAN);

    public final ItemStackHandler inventory = new ItemStackHandler(9);

    public SimbaEntity(EntityType<? extends SimbaEntity> type, Level level) {
        super(type, level);
        this.setCustomName(net.minecraft.network.chat.Component.literal("Simba"));
        this.setCustomNameVisible(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY, false);
        this.entityData.define(DATA_HAS_CHARM, false);
    }

    public boolean hasCharm() {
        return this.entityData.get(DATA_HAS_CHARM);
    }

    public void setHasCharm(boolean hasCharm) {
        this.entityData.set(DATA_HAS_CHARM, hasCharm);
    }

    @Override
    public boolean canChangeDimensions() {
        return hasCharm();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new SimbaAttackGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.3D, 4.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new SimbaFishingGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new SimbaWanderGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public boolean isBaby() {
        return this.entityData.get(DATA_BABY);
    }

    public void setBaby(boolean baby) {
        this.entityData.set(DATA_BABY, baby);
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
        if (held.is(ModItems.SIMBA_CHARM.get()) && SimbaCharmItem.isActive(held) && !hasCharm()) {
            if (!level().isClientSide()) {
                held.shrink(1);
                setHasCharm(true);
                ChatHelper.sendNpcMessage(player, "Simba", "*accepts the charm and roars proudly*");
            }
            return InteractionResult.SUCCESS;
        }

        // Sneak+interact opens Simba's inventory
        if (player.isShiftKeyDown() && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
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

        // Toggle sitting
        toggleSitting(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable Entity changeDimension(@NotNull ServerLevel destination) {
        Entity result = super.changeDimension(destination);
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
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Baby", isBaby());
        tag.putBoolean("HasCharm", hasCharm());
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setBaby(tag.getBoolean("Baby"));
        setHasCharm(tag.getBoolean("HasCharm"));
        if (tag.contains("Inventory")) inventory.deserializeNBT(tag.getCompound("Inventory"));
    }
}
