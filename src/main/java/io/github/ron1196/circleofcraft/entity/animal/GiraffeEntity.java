package io.github.ron1196.circleofcraft.entity.animal;

import io.github.ron1196.circleofcraft.data.ModCriteriaTriggers;
import io.github.ron1196.circleofcraft.entity.ai.AmbientPanicGoal;
import io.github.ron1196.circleofcraft.registry.EntityTypes;
import io.github.ron1196.circleofcraft.registry.ModBlocks;
import io.github.ron1196.circleofcraft.registry.ModItems;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GiraffeEntity extends ModAnimal {

    public static final float BABY_SCALE = 0.35F; // calves ~1.8m vs adult ~5.5m
    public static final float SHADOW_RADIUS = 0.8F;

    private static final EntityDataAccessor<Boolean> DATA_SADDLED =
            SynchedEntityData.defineId(GiraffeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TIE =
            SynchedEntityData.defineId(GiraffeEntity.class, EntityDataSerializers.INT);

    private static final int NO_TIE = -1;
    private static final float RIDDEN_SPEED_MULTIPLIER = 1.0F;

    public enum TieColor {
        BASE(0, ModItems.GIRAFFE_TIE),
        WHITE(1, ModItems.GIRAFFE_TIE_WHITE),
        BLUE(2, ModItems.GIRAFFE_TIE_BLUE),
        YELLOW(3, ModItems.GIRAFFE_TIE_YELLOW),
        RED(4, ModItems.GIRAFFE_TIE_RED),
        PURPLE(5, ModItems.GIRAFFE_TIE_PURPLE),
        GREEN(6, ModItems.GIRAFFE_TIE_GREEN),
        BLACK(7, ModItems.GIRAFFE_TIE_BLACK);

        private final int id;
        private final Supplier<Item> item;

        TieColor(int id, Supplier<Item> item) {
            this.id = id;
            this.item = item;
        }

        public int getId() {
            return id;
        }

        public Item getItem() {
            return item.get();
        }

        public static @Nullable TieColor fromItem(Item item) {
            for (TieColor color : values()) {
                if (color.getItem() == item) return color;
            }
            return null;
        }
    }

    public GiraffeEntity(EntityType<? extends net.minecraft.world.entity.animal.Animal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SADDLED, false);
        builder.define(DATA_TIE, NO_TIE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AmbientPanicGoal(this));
        this.goalSelector.addGoal(
                2, new AvoidEntityGoal<>(this, LivingEntity.class, 12.0F, 1.0D, 1.5D, e -> e instanceof LionEntity));
        addTemptGoal(2, 1.0, ModBlocks.ACACIA_LEAVES.get().asItem());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ModAnimal.createLKAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    // ── Saddle & Tie ──

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLED, saddled);
    }

    public int getTie() {
        return this.entityData.get(DATA_TIE);
    }

    public void setTie(int tie) {
        this.entityData.set(DATA_TIE, tie);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Breeding food takes priority over saddle/mount
        if (isFood(stack)) {
            return super.mobInteract(player, hand);
        }

        // Apply giraffe saddle
        if (stack.is(ModItems.GIRAFFE_SADDLE.get()) && !isSaddled() && !isBaby()) {
            setSaddled(true);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                ModCriteriaTriggers.RIDE_GIRAFFE.trigger(serverPlayer);
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        // Apply tie (must be saddled, adult, no existing tie, holding a tie item)
        if (!isBaby() && isSaddled() && getTie() == NO_TIE) {
            TieColor tieColor = TieColor.fromItem(stack.getItem());
            if (tieColor != null) {
                setTie(tieColor.getId());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.5F, 1.0F);
                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }

        // Mount the saddled giraffe
        if (!level().isClientSide && isSaddled() && (getFirstPassenger() == null || getFirstPassenger() == player)) {
            player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    // ── Riding ──

    @Override
    protected @NotNull Vec3 getPassengerAttachmentPoint(
            @NotNull net.minecraft.world.entity.Entity passenger,
            @NotNull net.minecraft.world.entity.EntityDimensions dimensions,
            float partialTick) {
        return new Vec3(0.0, dimensions.height() * 0.93, 0.0);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.getEntity() == getFirstPassenger()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (isSaddled() && getFirstPassenger() instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVec) {
        super.tickRidden(player, travelVec);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player player, @NotNull Vec3 travelVec) {
        float forward = player.zza;
        float strafe = player.xxa * 0.5F;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        return new Vec3(strafe, 0.0, forward);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED) * RIDDEN_SPEED_MULTIPLIER;
    }

    // ── Save/Load ──

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", isSaddled());
        tag.putInt("Tie", getTie());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("Saddled"));
        setTie(tag.getInt("Tie"));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ModBlocks.ACACIA_LEAVES.get().asItem());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return EntityTypes.GIRAFFE.get().create(level);
    }

    @Override
    protected ItemStack getQuestReward() {
        return new ItemStack(ModItems.GIRAFFE_SADDLE.get(), 1);
    }
}
